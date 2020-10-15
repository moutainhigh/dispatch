package org.example.dispatch.config;

import org.example.dispatch.event.EventBus;
import org.example.dispatch.queue.Queue;
import org.example.dispatch.queue.TaskQueue;
import org.example.dispatch.subscribe.Subscriber;
import org.example.dispatch.subscribe.TaskSubSubscriber;
import org.example.dispatch.task.facade.CompensateTaskFacadeImpl;
import org.example.dispatch.task.facade.TaskFacade;
import org.example.dispatch.task.intf.TaskServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * 加载配置
 *
 * @author tangaq
 * @date 2020/10/14
 */
@Configuration
@EnableConfigurationProperties(DispatchProperties.class)
public class DispatchConfiguration {
    @Autowired
    private DispatchProperties dispatchProperties;

    @Bean
    public TaskFacade taskFacade() {
        return new CompensateTaskFacadeImpl();
    }

    @Bean
    public TaskServiceImpl obtainTask() {
        return new TaskServiceImpl();
    }

    // 同时加载application
    @Bean
    public Subscriber subscriber() {
        return new TaskSubSubscriber(dispatchProperties.getScheduledThreadSize());
    }

    @Bean
    @Lazy
    public Queue taskQueue() {
        return new TaskQueue(dispatchProperties.getTaskQueueSize(), dispatchProperties.getFileName(),
            dispatchProperties.isFileEnable());
    }

    @Bean
    public EventBus eventBus() {
        return new EventBus(subscriber());
    }
}
