package org.example.dispatch.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 补偿基础配置: dispatch.xxx
 *
 * @author tangaq
 * @date 2020/10/14
 */
@ConfigurationProperties("dispatch")
public class DispatchProperties {
    private int taskQueueSize = 1000;

    private int scheduledThreadSize = 400;

    private String fileName = "dispatch.db";

    private boolean fileEnable = true;

    public int getTaskQueueSize() {
        return taskQueueSize;
    }

    public void setTaskQueueSize(int taskQueueSize) {
        this.taskQueueSize = taskQueueSize;
    }

    public int getScheduledThreadSize() {
        return scheduledThreadSize;
    }

    public void setScheduledThreadSize(int scheduledThreadSize) {
        this.scheduledThreadSize = scheduledThreadSize;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isFileEnable() {
        return fileEnable;
    }

    public void setFileEnable(boolean fileEnable) {
        this.fileEnable = fileEnable;
    }
}
