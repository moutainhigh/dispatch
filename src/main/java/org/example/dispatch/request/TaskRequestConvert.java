package org.example.dispatch.request;

import java.util.UUID;

import org.example.dispatch.task.Task;

/**
 * @author tangaq
 * @date 2020/10/14
 */
public class TaskRequestConvert {
    public static Task convert(TaskRequest taskRequest) {
        Task task = new Task();
        task.setUuid(UUID.randomUUID()
            .toString());
        task.setHsjryRequest(taskRequest.getRequest());
        task.setTargetClass(taskRequest.getTargetClass());
        task.setTargetMethod(taskRequest.getTargetMethod());
        task.setTargetObject(taskRequest.getTargetObject());
        task.setParameters(taskRequest.getParameters());
        task.setApartExecTime(taskRequest.getApartExecTime());
        task.setDelayExecTime(taskRequest.getDelayExecTime());
        task.setExecNum(taskRequest.getExecNum());
        task.setErrorLoop(taskRequest.isErrorLoop());
        return task;
    }
}
