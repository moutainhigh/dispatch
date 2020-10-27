package org.example.dispatch.event;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.example.dispatch.event.subscribe.AbstractSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 简单事件
 *
 * @author tangaq
 * @date 2020/10/13
 */
public class EventBus {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventBus.class);

    private static final ConcurrentMap<Class<? extends Event>, AbstractSubscriber> SUBSCRIBER_MAP
        = new ConcurrentHashMap<>();

    /**
     * 注册订阅者
     *
     * @param eventClass 消息
     * @param subscriber 消息订阅
     */
    public static void registry(Class<? extends Event> eventClass, AbstractSubscriber subscriber) {
        SUBSCRIBER_MAP.putIfAbsent(eventClass, subscriber);
        if (LOGGER.isWarnEnabled()) {
            LOGGER.debug("Register subscribe: {} of event: {}.", subscriber, eventClass);
        }
    }

    public static void unRegistry(Class<? extends Event> eventClass, AbstractSubscriber subscriber) {
        SUBSCRIBER_MAP.remove(eventClass);
    }

    /**
     * 向事件总线投递一个消息
     *
     * @param event 消息
     */
    public static void post(final Event event) {
        AbstractSubscriber subscriber = SUBSCRIBER_MAP.get(event.getClass());
        if (subscriber == null) {
            throw new RuntimeException("The subscriber of event not found, event: " + event.getClass());
        }
        if (subscriber.isSync()) {
            // 同步
            handleEvent(subscriber, event);
        } else {
            // 异步
        }
    }

    private static void handleEvent(final AbstractSubscriber subscriber, final Event event) {
        subscriber.onEvent(event);
    }

}
