package org.example.dispatch.event;

import org.example.dispatch.task.Task;

/**
 * 任务事件
 *
 * @author tangaq
 * @date 2020/10/15
 */
public class TaskEvent implements Event {
    private final Task task;

    public TaskEvent(Task task) {
        this.task = task;
    }

    public Task getTask() {
        return task;
    }

}
