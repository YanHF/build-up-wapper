package com.huaifang.yan;

import com.huaifang.yan.annotation.State;
import com.huaifang.yan.core.Actor;
import com.huaifang.yan.event.StateEventRegistration;
import com.huaifang.yan.model.OrderVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
@Slf4j
@Service("orderStateMachineStarter")
public class OrderStateMachineStarter implements ApplicationContextAware, ApplicationListener<ApplicationReadyEvent> {

    private ApplicationContext applicationContext;

    @Resource(name = "orderStateEventQueue")
    private StateEventRegistration<OrderVo, String, OrderStates> stateEventRegistration;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        this.registerActors();
    }

    /**
     * Register state actors from spring container
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected void registerActors() {
        Map<String, Actor> actors = applicationContext.getBeansOfType(Actor.class, false, true);

        if (actors.isEmpty()) {
            log.info("No state actor register");
            return;
        }

        for (Actor actor : actors.values()) {
            Actor proxy = (Actor) FSMUtils.getTarget(actor);
            State anno = proxy.getClass().getDeclaredAnnotation(State.class);
            if (anno == null) {
                throw new IllegalStateException("State annotation should be place on this class " + actor.getClass().getName());
            }
            stateEventRegistration.register(actor);
        }
    }


}
