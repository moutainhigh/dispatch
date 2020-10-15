package org.example.dispatch.request;

import java.io.Serializable;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author tangaq
 * @date 2020/10/14
 */
public class TaskRequest implements Serializable {
    @Valid
    @NotNull(message = "基础变量为空")
    private BaseRequest request;

    @NotBlank(message = "目标方法为空")
    private String targetMethod;

    @Valid
    @NotNull(message = "目标对象为空")
    private Object targetObject;

    private String targetClass;

    /** 参数 */
    private Object[] parameters;

    /** 延迟执行时间 */
    private long delayExecTime;

    /** 间隔执行时间 */
    private long apartExecTime;

    /** 最多执行时间 */
    private int execNum;

    /** 如果为true 则 失败继续往队列里插入，如果为false 则调用 回调接口 */
    private boolean errorLoop;

    public BaseRequest getRequest() {
        return request;
    }

    public void setRequest(BaseRequest request) {
        this.request = request;
    }

    public String getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(String targetClass) {
        this.targetClass = targetClass;
    }

    public String getTargetMethod() {
        return targetMethod;
    }

    public void setTargetMethod(String targetMethod) {
        this.targetMethod = targetMethod;
    }

    public Object getTargetObject() {
        return targetObject;
    }

    public void setTargetObject(Object targetObject) {
        this.targetObject = targetObject;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    public long getDelayExecTime() {
        return delayExecTime;
    }

    public void setDelayExecTime(long delayExecTime) {
        this.delayExecTime = delayExecTime;
    }

    public long getApartExecTime() {
        return apartExecTime;
    }

    public void setApartExecTime(long apartExecTime) {
        this.apartExecTime = apartExecTime;
    }

    public int getExecNum() {
        return execNum;
    }

    public void setExecNum(int execNum) {
        this.execNum = execNum;
    }

    public boolean isErrorLoop() {
        return errorLoop;
    }

    public void setErrorLoop(boolean errorLoop) {
        this.errorLoop = errorLoop;
    }
}
