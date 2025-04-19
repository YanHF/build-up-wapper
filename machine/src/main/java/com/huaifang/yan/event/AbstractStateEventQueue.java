/**
 * LY.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.huaifang.yan.event;


import com.huaifang.yan.FSMUtils;
import com.huaifang.yan.OrderStates;
import com.huaifang.yan.annotation.State;
import com.huaifang.yan.core.Actor;
import com.huaifang.yan.core.StateContext;
import com.huaifang.yan.core.StateToString;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 抽象状态事件队列
 * 
 * @author allen
 * @version $Id: AbstractStateEventQueue.java, v 0.1 2017年9月3日 下午8:58:55 allen Exp $
 */
public abstract class AbstractStateEventQueue<T, BizType, StateValue> implements StateEventQueue<T, BizType, StateValue> {
    /** logger */
    protected final Logger                    logger          = LoggerFactory.getLogger(getClass());

    /**锁*/
    private final ReadWriteLock               lock            = new ReentrantReadWriteLock();

    /** 状态监听器容器 */
    private final Map<String, ListenerKeeper> keepers         = new HashMap<>();

    /** Actor comparator */
    protected ActorComparator                 actorComparator = new ActorComparator();

    /**
     * 获取状态字符串转化器
     * 
     * @return 状态字符串转化器
     */
    protected abstract StateToString<T, BizType, StateValue> getStateToString();

    /**
     * 获取Actor注册校验器
     * 
     * @return Actor注册校验器
     */
    protected abstract ActorRegistryValidator getActorRegistryValidator();

    /** 
     * @see StateEventRegistration#register(Actor)
     */
    @SuppressWarnings("unchecked")
    @Override
    public void register(Actor<StateEvent<T, BizType, StateValue>, T, BizType, StateValue> actor) {
        Actor<StateEvent<T, BizType, StateValue>, T, BizType, StateValue> proxy = (Actor<StateEvent<T, BizType, StateValue>, T, BizType, StateValue>) FSMUtils.getTarget(actor);
        State anno = proxy.getClass().getDeclaredAnnotation(State.class);
        if (anno == null) {
            throw new IllegalStateException("Cannot register Actor, please make sure place annotation @State on serivce: " + actor.getClass().getName());
        }

        boolean validAnnotation = getActorRegistryValidator().validate(anno);
        if (!validAnnotation) {
            throw new IllegalStateException("Cannot register Actor, please make sure annotation value is correct: " + anno.from() + "@" + anno.to());
        }

        try {
            lock.writeLock().lock();
            List<String> transitIds = getTransitId(anno);
            for (String transitId : transitIds) {
                ListenerKeeper keeper = keepers.get(transitId);
                if (keeper == null) {
                    keeper = new ListenerKeeper();
                }
                keeper.actors.add(actor);
                keepers.put(transitId, keeper);
                logger.info("Register state actor {}-{} ", transitId, actor.getClass().getName());
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    /** 
     * @see StateEventRegistration#unregister(Actor)
     */
    @SuppressWarnings("unchecked")
    @Override
    public void unregister(Actor<StateEvent<T, BizType, StateValue>, T, BizType, StateValue> actor) {
        Actor<StateEvent<T, BizType, StateValue>, T, BizType, StateValue> proxy = (Actor<StateEvent<T, BizType, StateValue>, T, BizType, StateValue>) FSMUtils.getTarget(actor);
        State anno = proxy.getClass().getDeclaredAnnotation(State.class);
        if (anno == null) {
            throw new IllegalStateException("Cannot unregister Actor, please make sure place annotation @State on serivce: " + actor.getClass().getName());
        }

        try {
            lock.writeLock().lock();
            List<String> transitIds = getTransitId(anno);
            for (String transitId : transitIds) {
                ListenerKeeper keeper = keepers.get(transitId);
                if (keeper == null) {
                    logger.warn("Cannot find actor container by transit id {} ", transitId);
                    return;
                }
                keeper.actors.remove(actor);
                logger.info("Unregister state actor {}-{} ", transitId, actor.getClass().getName());
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    /** 
     * @see StateEventRegistration#unregisterAll()
     */
    @Override
    public void unregisterAll() {
        try {
            lock.writeLock().lock();
            keepers.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }

    /** 
     * @see StateEventQueue#getAllActors()
     */
    @Override
    public Collection<Actor<StateEvent<T, BizType, StateValue>, T, BizType, StateValue>> getAllActors() {
        Collection<Actor<StateEvent<T, BizType, StateValue>, T, BizType, StateValue>> all = new LinkedList<>();
        for (ListenerKeeper keeper : keepers.values()) {
            all.addAll(keeper.retrieveListeners());
        }
        return Collections.unmodifiableCollection(all);
    }

    /** 
     * @see StateEventQueue#getAllActors(StateEvent)
     */
    @Override
    public Collection<Actor<StateEvent<T, BizType, StateValue>, T, BizType, StateValue>> getAllActors(StateEvent<T, BizType, StateValue> event) {
        LinkedList<Actor<StateEvent<T, BizType, StateValue>, T, BizType, StateValue>> allActors = new LinkedList<>();

        // 根据状态流转ID获取对应的监听器
        StateContext<T, BizType, StateValue> context = event.getContext();
        String transitId = getTransitId(context);
        ListenerKeeper keeper = keepers.get(transitId);
        if (keeper == null) {
            return allActors;
        }

        for (Actor<StateEvent<T, BizType, StateValue>, T, BizType, StateValue> actor : keeper.actors) {
            // Check whether the event is suppored by the actor
            if (actor.isSupported(event)) {
                allActors.add(actor);
            }
        }
        Collections.sort(allActors, actorComparator);

        return allActors;
    }

    /**
     * Get transit id
     *
     * @param context
     * @return
     */
    protected String getTransitId(StateContext<T, BizType, StateValue> context) {
        String fromBiz = StringUtils.defaultIfBlank(getStateToString().bizToString(context.getFromBiz()), FSMUtils.PLACEHOLD);
        String toBiz = StringUtils.defaultIfBlank(getStateToString().bizToString(context.getToBiz()), FSMUtils.PLACEHOLD);
        String fromState = StringUtils.defaultIfBlank(getStateToString().stateToString(context.getFromState()), FSMUtils.PLACEHOLD);
        String toState = StringUtils.defaultIfBlank(getStateToString().stateToString(context.getToState()), FSMUtils.PLACEHOLD);
        String transitId = fromBiz + FSMUtils.SPLIT1 + toBiz + FSMUtils.SPLIT0 + fromState + FSMUtils.SPLIT1 + toState;
        return transitId;
    }

    /**
     * Get transit id<p>s
     * biz1^biz2|state1^state2
     *
     * @param anno {@link State}标注
     * @return transit id
     */
    protected List<String> getTransitId(State anno) {
        OrderStates[] froms = anno.from();
        OrderStates[] tos = anno.to();

        List<String> transitIds = new ArrayList<>();
        for (OrderStates from : froms) {
            for (OrderStates to : tos) {
                String fromBiz = StringUtils.defaultIfBlank(anno.fromBiz(), FSMUtils.PLACEHOLD);
                String toBiz = StringUtils.defaultIfBlank(anno.toBiz(), FSMUtils.PLACEHOLD);
                String fromState = from != null ? from.name() : FSMUtils.PLACEHOLD;
                String toState = to != null ? to.name() : FSMUtils.PLACEHOLD;
                String transitId = fromBiz + FSMUtils.SPLIT1 + toBiz + FSMUtils.SPLIT0 + fromState + FSMUtils.SPLIT1 + toState;
                transitIds.add(transitId);
            }
        }
        return transitIds;
    }

    /**
     * Actor comparator
     *
     * @author allen
     * @version $Id: AbstractStateEventQueue.java, v 0.1 2017年9月4日 上午10:06:01 allen Exp $
     */
    private class ActorComparator implements Comparator<Actor<StateEvent<T, BizType, StateValue>, T, BizType, StateValue>> {

        /**
         * @see Comparator#compare(Object, Object)
         */
        @Override
        public int compare(Actor<StateEvent<T, BizType, StateValue>, T, BizType, StateValue> o1, Actor<StateEvent<T, BizType, StateValue>, T, BizType, StateValue> o2) {
            if (o1.getOrder() > o2.getOrder()) {
                return 1;
            }

            if (o1.getOrder() < o2.getOrder()) {
                return -1;
            }

            return 0;
        }

    }

    /**
     * 事件监听器容器
     * 
     * @author allen
     * @version $Id: AbstractStateEventQueue.java, v 0.1 2017年9月3日 下午9:01:08 allen Exp $
     */
    private class ListenerKeeper {
        /**注册事件容器*/
        public final Set<Actor<StateEvent<T, BizType, StateValue>, T, BizType, StateValue>> actors;

        /**
         * 构造方法
         */
        public ListenerKeeper() {
            actors = new LinkedHashSet<>();
        }

        /**
         * 获取容器注册的监听器
         * 
         * @return Actor集合
         */
        public Collection<Actor<StateEvent<T, BizType, StateValue>, T, BizType, StateValue>> retrieveListeners() {
            LinkedList<Actor<StateEvent<T, BizType, StateValue>, T, BizType, StateValue>> allActors = new LinkedList<>();
            allActors.addAll(actors);
            return Collections.unmodifiableCollection(allActors);
        }
    }

}
