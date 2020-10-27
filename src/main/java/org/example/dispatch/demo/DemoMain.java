package org.example.dispatch.demo;

import org.example.dispatch.event.EventBus;

/**
 * @author tangaq
 * @date 2020/10/26
 */
public class DemoMain {
    public static void main(String[] args) {
        DemoEvent demoEvent = new DemoEvent();
        DemoSubscriber demoSubscriber = new DemoSubscriber();
        EventBus.registry(demoEvent.getClass(), demoSubscriber);

        DemoEvent2 demoEvent2 = new DemoEvent2();
        DemoSubscriber2 demoSubscriber2 = new DemoSubscriber2();
        EventBus.registry(demoEvent2.getClass(), demoSubscriber2);

        EventBus.post(demoEvent);
        EventBus.post(demoEvent2);

    }
}
