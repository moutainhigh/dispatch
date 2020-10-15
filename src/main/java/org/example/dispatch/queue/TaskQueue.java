package org.example.dispatch.queue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;

import org.example.dispatch.event.EventBus;
import org.example.dispatch.event.TaskEvent;
import org.example.dispatch.task.Task;
import org.example.dispatch.utils.MapDbUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author tangaq
 * @date 2020/10/13
 */
public class TaskQueue implements Queue, InitializingBean {
    private PriorityBlockingQueue<Task> taskPriorityBlockingQueue;
    private boolean close = false;

    private boolean fileEnable;

    private String fileName;

    @Autowired
    private EventBus eventBus;

    public TaskQueue(int queueSize, String fileName) {
        this.taskPriorityBlockingQueue = new PriorityBlockingQueue<>(queueSize);
        init(fileName, true);
    }

    public TaskQueue(int queueSize, String fileName, boolean fileEnable) {
        this.taskPriorityBlockingQueue = new PriorityBlockingQueue<>(queueSize);
        init(fileName, fileEnable);
    }

    @Override
    public void put(final Task task) {
        if (!this.close) {
            this.taskPriorityBlockingQueue.put(task);
            List<Task> tasks = new ArrayList<>();
            tasks.add(task);
            MapDbUtil.addData(tasks);
            eventBus.post(new TaskEvent(getFirst()));
        } else {
            throw new RuntimeException("关闭了队列，JVM 将关闭");
        }
    }

    @Override
    public void remove(String key) {

    }

    @Override
    public Task getFirst() {
        Task task = null;
        try {
            task = this.taskPriorityBlockingQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return task;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        MapDbUtil.setFileEnable(fileEnable);
        List<Task> tasks = MapDbUtil.fetchTask(fileName);
        for (Task task : tasks) {
            put(task);
        }
    }

    private void init(String fileName, boolean fileEnable) {
        this.fileName = fileName;
        this.fileEnable = fileEnable;
        Runtime.getRuntime()
            .addShutdownHook(new Thread(() -> {
                try {
                    Thread.sleep(5000);
                    close = true;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }));
    }
}
