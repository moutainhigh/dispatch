package org.example.dispatch.subscribe;

import org.example.dispatch.event.Event;

import com.google.common.eventbus.Subscribe;

/**
 * 事件订阅
 *
 * @author tangaq
 * @date 2020/10/15
 */
public abstract class Subscriber {
    @Subscribe
    public abstract void onEvent(Event event);
}
