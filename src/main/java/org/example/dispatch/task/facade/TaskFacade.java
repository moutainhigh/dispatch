package org.example.dispatch.task.facade;

import org.example.dispatch.request.CommonResponse;
import org.example.dispatch.request.TaskRequest;

/**
 * 任务补偿rpc接口
 *
 * @author tangaq
 * @date 2020/10/14
 */
public interface TaskFacade {
    CommonResponse receiveTask(TaskRequest taskRequest);
}
