package org.example.dispatch.task.facade;

import javax.validation.Valid;

import org.example.dispatch.request.CommonResponse;
import org.jetbrains.annotations.NotNull;

/**
 * 定义facade抽象模板
 *
 * @author tangaq
 * @date 2020/10/14
 */
public abstract class AbstractCompensateTaskFacade<k> {

    CommonResponse facadeManager(@NotNull @Valid k request) {
        return this.normal(request);
    }

    protected abstract CommonResponse normal(k request);
}
