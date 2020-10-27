package org.example.dispatch.demo;

import org.example.dispatch.event.Event;
import org.example.dispatch.event.subscribe.AbstractSubscriber;

/**
 * @author tangaq
 * @date 2020/10/26
 */
public class DemoSubscriber2 extends AbstractSubscriber {
    @Override
    public void onEvent(Event event) {
        System.out.println("TestSubscriber2 消费");
    }

}
