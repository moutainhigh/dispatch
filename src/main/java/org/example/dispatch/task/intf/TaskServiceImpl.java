package org.example.dispatch.task.intf;

import org.example.dispatch.queue.Queue;
import org.example.dispatch.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * @author tangaq
 * @date 2020/10/14
 */
public class TaskServiceImpl implements TaskService {
    private static Logger log = LoggerFactory.getLogger(TaskServiceImpl.class);
    @Autowired
    @Qualifier("taskQueue")
    private Queue queue;

    @Override
    public void receiveTask(Task task) {
        log.info("登记{}任务", task.getUuid());
        queue.put(task);
    }
}
