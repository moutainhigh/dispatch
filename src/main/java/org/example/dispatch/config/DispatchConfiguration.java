package org.example.dispatch.config;

import org.example.dispatch.event.EventBus;
import org.example.dispatch.event.TaskEvent;
import org.example.dispatch.event.subscribe.TaskSubSubscriber;
import org.example.dispatch.queue.Queue;
import org.example.dispatch.queue.TaskQueue;
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
    public TaskSubSubscriber taskSubSubscriber() {
        TaskSubSubscriber taskSubSubscriber = new TaskSubSubscriber(dispatchProperties.getScheduledThreadSize());
        // todo 一次性加载所有订阅者???模式
        EventBus.registry(TaskEvent.class, taskSubSubscriber);
        return taskSubSubscriber;
    }

    @Bean
    @Lazy
    public Queue taskQueue() {
        return new TaskQueue(dispatchProperties.getTaskQueueSize(), dispatchProperties.getFileName(),
            dispatchProperties.isFileEnable());
    }

}
