package org.example.dispatch.event.subscribe;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.example.dispatch.event.Event;
import org.example.dispatch.event.TaskEvent;
import org.example.dispatch.queue.Queue;
import org.example.dispatch.task.Task;
import org.example.dispatch.utils.MapDbUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;

import com.esotericsoftware.reflectasm.MethodAccess;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningScheduledExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

/**
 * 任务事件订阅
 *
 * @author tangaq
 * @date 2020/10/13
 */
public class TaskSubSubscriber extends AbstractSubscriber implements ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(TaskSubSubscriber.class);

    private ListeningScheduledExecutorService listeningScheduledExecutorService;

    /** 记录次数 */
    private final Map<String, AtomicInteger> indexMap = Maps.newConcurrentMap();

    private ApplicationContext applicationContext;

    @Autowired
    @Qualifier("taskQueue")
    @Lazy
    private Queue queue;

    @Autowired
    private TaskSubSubscriber taskSubSubscriber;

    public TaskSubSubscriber() {
        init(400);
    }

    public TaskSubSubscriber(int coreThreadSize) {
        this.init(coreThreadSize);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private void init(int coreThreadSize) {
        ScheduledThreadPoolExecutor scheduler = new ScheduledThreadPoolExecutor(coreThreadSize, r -> {
            Thread thread = new Thread(r);
            thread.setName("dispatch-task-" + thread.getName());
            return thread;
        });
        listeningScheduledExecutorService = MoreExecutors.listeningDecorator(scheduler);
    }

    @Override
    public void onEvent(Event event) {
        Class<?> eventClass = event.getClass();
        if (eventClass == TaskEvent.class) {
            TaskEvent taskEvent = (TaskEvent) event;
            this.handleTask(taskEvent.getTask());
        }
    }

    private void handleTask(Task task) {
        try {
            if (task != null) {
                if (null == indexMap.get(task.getUuid())) {
                    indexMap.put(task.getUuid(), new AtomicInteger(0));
                }

                long delay = task.getDelayExecTime() + task.getApartExecTime() * indexMap.get(task.getUuid())
                    .intValue();
                logger.debug("{}下次补偿间隔时间{}s", task.getUuid(), delay / 1000);
                ListenableFuture explosion = listeningScheduledExecutorService.schedule(new ScheduledCallable(task),
                    delay, TimeUnit.MILLISECONDS);
                Futures.addCallback(explosion, new FutureCallback<Object>() {
                    @Override
                    public void onSuccess(@Nullable Object result) { // result为目标方法的返回值
                        // 设置线程上下文
                        try {
                            if (Objects.equals(result, false)) {
                                logger.info("{}的{}/{}次补偿判定失败，继续补偿", task.getUuid(), indexMap.get(task.getUuid())
                                    .intValue() + 1, task.getExecNum());
                                continueLoop(task, result);
                            } else {
                                logger.info("{} 的{}/{}次补偿判定成功，开始回调正常处理", task.getUuid(), indexMap.get(task.getUuid())
                                    .intValue() + 1, task.getExecNum());
                                MapDbUtil.deleteData(task.getUuid());
                            }
                        } finally {
                            // 移除上下文
                        }

                    }

                    @Override
                    public void onFailure(Throwable t) {
                        try {
                            if (task.isErrorLoop()) {
                                logger.info("{} 的{}/{}次补偿失败，继续补偿", task.getUuid(), indexMap.get(task.getUuid())
                                    .intValue() + 1, task.getExecNum());
                                // 异常需要循环
                                continueLoop(task, t);
                            } else {
                                logger.info("{} 的{}/{}次补偿失败，开始回调异常处理", task.getUuid(), indexMap.get(task.getUuid())
                                    .intValue() + 1, task.getExecNum());
                                // 异常直接回调
                                // if (task.getErrorTaskCallBackClass() != null) {
                                //     callBack(task.getErrorTaskCallBackClass(), task.getErrorTaskCallBackMethod(), t, task);
                                // }
                                MapDbUtil.deleteData(task.getUuid());
                            }
                        } finally {
                            // 移除上下文
                        }
                    }
                }, MoreExecutors.directExecutor());
            }
        } catch (Exception e) {
            if (logger.isWarnEnabled()) {
                logger.warn("TaskSubSubscriber error:", e);
            }
        }
    }

    /**
     * 循环执行任务
     *
     * @param task
     * @param result
     */
    private void continueLoop(Task task, Object result) {
        if (task.getExecNum() - 1 > indexMap.get(task.getUuid())
            .intValue()) { // 未超过次数
            this.queue.put(task);
            indexMap.put(task.getUuid(), new AtomicInteger(indexMap.get(task.getUuid())
                .addAndGet(1)));
        } else {
            logger.info("{}的{}/{}次补偿次数用尽,开始回调异常处理", task.getUuid(), indexMap.get(task.getUuid())
                .intValue() + 1, task.getExecNum());
        }
        MapDbUtil.deleteData(task.getUuid());
    }

    private class ScheduledCallable implements Callable<Object> {
        private final Task task;

        public ScheduledCallable(Task task) {
            this.task = task;
        }

        @Override
        public Object call() throws Exception {
            // 设置公共上下文
            try {
                return execute();
            } finally {
                // 移除线程上下文
            }
        }

        /**
         * 执行业务方法
         *
         * @return 返回业务结果
         */
        private Object execute() {
            Object result;
            Object targetObject = null;
            Class<?> clazz;
            if (task.getTargetClass() != null && task.getTargetClass()
                .contains(".")) {
                try {
                    clazz = this.getClass()
                        .getClassLoader()
                        .loadClass(task.getTargetClass());
                    targetObject = clazz.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException("无法实例化对象:" + task.getTargetClass());
                }
            } else if (task.getTargetClazz() != null) {
                clazz = task.getTargetClazz();
                try {
                    targetObject = clazz.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                // spring 按name获取bean
                targetObject = applicationContext.getBean(task.getTargetClass());
                clazz = targetObject.getClass();
            }

            MethodAccess methodAccess = MethodAccess.get(clazz);
            result = methodAccess.invoke(targetObject, task.getTargetMethod(), task.getParameters());
            return result;
        }
    }
}
