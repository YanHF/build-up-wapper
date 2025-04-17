package com.huaifang.yan.machine;

import com.huaifang.yan.model.OrderEvents;
import com.huaifang.yan.model.OrderStates;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;

@Configuration
@EnableStateMachine
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<OrderStates, OrderEvents> {

    @Override
    public void configure(StateMachineStateConfigurer<OrderStates, OrderEvents> states) throws Exception {
        states
                .withStates()
                .initial(OrderStates.PENDING_PAYMENT)
                .states(EnumSet.allOf(OrderStates.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<OrderStates, OrderEvents> transitions) throws Exception {
        transitions
                .withExternal()
                .source(OrderStates.PENDING_PAYMENT).target(OrderStates.PAID).event(OrderEvents.PAY)
                .and()
                .withExternal()
                .source(OrderStates.PAID).target(OrderStates.SHIPPED).event(OrderEvents.SHIP)
                .and()
                .withExternal()
                .source(OrderStates.SHIPPED).target(OrderStates.RECEIVED).event(OrderEvents.RECEIVE)
                .and()
                .withExternal()
                .source(OrderStates.RECEIVED).target(OrderStates.COMPLETED).event(OrderEvents.COMPLETE)
                .and()
                .withExternal()
                .source(OrderStates.PENDING_PAYMENT).target(OrderStates.CANCELLED).event(OrderEvents.CANCEL)
                .and()
                .withExternal()
                .source(OrderStates.PAID).target(OrderStates.CANCELLED).event(OrderEvents.CANCEL);
    }
}

