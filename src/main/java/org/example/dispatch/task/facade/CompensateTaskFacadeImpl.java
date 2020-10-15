package org.example.dispatch.task.facade;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.example.dispatch.request.CommonResponse;
import org.example.dispatch.request.EnumResponseType;
import org.example.dispatch.request.TaskRequest;
import org.example.dispatch.request.TaskRequestConvert;
import org.example.dispatch.task.Task;
import org.example.dispatch.task.intf.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * 任务补偿实现类
 *
 * @author tangaq
 * @date 2020/10/14
 */
public class CompensateTaskFacadeImpl extends AbstractCompensateTaskFacade<TaskRequest> implements TaskFacade {
    @Autowired
    @Qualifier("obtainTask")
    private TaskService taskService;

    @Override
    public CommonResponse normal(TaskRequest request) {
        CommonResponse response = new CommonResponse();
        Task task = TaskRequestConvert.convert(request);
        taskService.receiveTask(task);
        response.setResponseType(EnumResponseType.SUCCESS);
        return response;
    }

    @Override
    public CommonResponse receiveTask(@NotNull(message = "request为空") @Valid TaskRequest taskRequest) {
        return facadeManager(taskRequest);
    }

}
