package org.example.dispatch.queue;

import org.example.dispatch.task.Task;

/**
 * @author tangaq
 * @date 2020/10/13
 */
public interface Queue {
    void put(Task task);

    void remove(String key);

    Task getFirst();
}
