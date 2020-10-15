package org.example.dispatch.event;

import java.util.concurrent.Executors;

import org.example.dispatch.subscribe.Subscriber;

import com.google.common.eventbus.AsyncEventBus;

/**
 * 简单事件
 *
 * @author tangaq
 * @date 2020/10/13
 */
public class EventBus {
    private final com.google.common.eventbus.EventBus eventBus;

    public EventBus(Subscriber subscriber) {
        eventBus = new AsyncEventBus(Executors.newCachedThreadPool());
        eventBus.register(subscriber);
    }

    public void post(Event event) {
        eventBus.post(event);
    }

}
