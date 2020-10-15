package org.example.dispatch.task;

import java.io.Serializable;

import org.example.dispatch.request.BaseRequest;
import org.jetbrains.annotations.NotNull;

/**
 * 补偿任务
 *
 * @author tangaq
 * @date 2020/10/13
 */
public class Task implements Comparable, Serializable {
    private static final long serialVersionUID = 8395557509791742032L;

    private BaseRequest hsjryRequest;

    private String uuid;

    /** 目标执行类名 */
    private String targetClass;

    /** 目标执行类 */
    private Class<?> targetClazz;

    /** 目标执行对象 */
    private Object targetObject;
    /** 目标执行方法 */
    private String targetMethod;
    /** 参数 */
    private Object[] parameters;

    /** 延迟执行时间 */
    private long delayExecTime;

    /** 间隔执行时间 */
    private long apartExecTime;

    /** 最多执行时间 */
    private int execNum;

    /** 返回对象 */
    private Object result;

    /**
     * 如果为true 则 失败继续往队列里插入，如果为false 则调用 回调接口
     */
    private boolean errorLoop;

    public BaseRequest getHsjryRequest() {
        return hsjryRequest;
    }

    public void setHsjryRequest(BaseRequest hsjryRequest) {
        this.hsjryRequest = hsjryRequest;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(String targetClass) {
        this.targetClass = targetClass;
    }

    public Class<?> getTargetClazz() {
        return targetClazz;
    }

    public void setTargetClazz(Class<?> targetClazz) {
        this.targetClazz = targetClazz;
    }

    public Object getTargetObject() {
        return targetObject;
    }

    public void setTargetObject(Object targetObject) {
        this.targetObject = targetObject;
    }

    public String getTargetMethod() {
        return targetMethod;
    }

    public void setTargetMethod(String targetMethod) {
        this.targetMethod = targetMethod;
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

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public boolean isErrorLoop() {
        return errorLoop;
    }

    public void setErrorLoop(boolean errorLoop) {
        this.errorLoop = errorLoop;
    }

    @Override
    public int compareTo(@NotNull Object o) {
        return 0;
    }
}
