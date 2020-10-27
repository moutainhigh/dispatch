package org.example.dispatch.event.subscribe;

import org.example.dispatch.event.Event;

/**
 * 事件订阅
 *
 * @author tangaq
 * @date 2020/10/15
 */
public abstract class AbstractSubscriber {
    /** 接受到消息是否同步 */
    protected boolean sync = true;

    protected AbstractSubscriber() {
    }

    protected AbstractSubscriber(boolean sync) {
        this.sync = sync;
    }

    public boolean isSync() {
        return sync;
    }

    public abstract void onEvent(Event event);

}
