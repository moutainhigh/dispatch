package org.example.dispatch.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

import org.example.dispatch.task.Task;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * mapdb
 *
 * @author tangaq
 * @date 2020/10/13
 */
public class MapDbUtil {
    private static final Logger logger = LoggerFactory.getLogger(MapDbUtil.class);
    private static final String OBJECT_NAME = "dispatch";
    private static final Object LOCK = new Object();
    private static DB db;
    private static String fileName = null;
    /**
     * 是否开启文件存储
     */
    private static boolean fileEnable = true;

    /**
     * 获取所有任务信息
     *
     * @param fileName
     * @return
     */
    public static List<Task> fetchTask(String fileName) {
        if (!fileEnable) {
            return new ArrayList<>();
        }
        List<Task> tasks = new ArrayList<>();
        ConcurrentMap<String, byte[]> mapObjects = getDbObject(fileName);
        for (String key : mapObjects.keySet()) {
            Object object = Byte2ObjectUtil.byteToObject(mapObjects.get(key));
            if (object instanceof Task) {
                Task task = (Task) object;
                tasks.add(task);
            }
        }
        db.commit();
        return tasks;
    }

    /**
     * 根据key删除任务
     *
     * @param key
     */
    public static void deleteData(String key) {
        if (!fileEnable) {
            return;
        }
        ConcurrentMap<String, byte[]> mapObjects = getDbObject(MapDbUtil.fileName);
        mapObjects.remove(key);
        db.commit();
    }

    /**
     * 添加任务到mapdb文件
     *
     * @param tasks
     */
    public static void addData(List<Task> tasks) {
        if (!fileEnable) {
            return;
        }
        ConcurrentMap<String, byte[]> mapObjects = getDbObject(MapDbUtil.fileName);
        for (Task task : tasks) {
            mapObjects.put(task.getUuid(), Byte2ObjectUtil.objectToByte(task));
        }
        db.commit();
    }

    /**
     * 创建内存数据库
     *
     * @param fileName
     */
    public static void initDb(String fileName) {
        if (!fileEnable) {
            return;
        }
        synchronized (LOCK) {
            if (db == null || db.isClosed()) {
                MapDbUtil.fileName = fileName;
                db = DBMaker.fileDB(fileName)
                    .fileMmapEnable()
                    .closeOnJvmShutdown()
                    .checksumHeaderBypass()
                    .make();
            }
        }
    }

    /**
     * 获取数据存储的对象
     *
     * @param fileName
     * @return
     */
    private static ConcurrentMap<String, byte[]> getDbObject(String fileName) {
        if (db == null || db.isClosed()) {
            initDb(fileName);
        }
        return db.hashMap(OBJECT_NAME, Serializer.STRING, Serializer.BYTE_ARRAY)
            .createOrOpen();
    }

    /**
     * 关闭数据库
     */
    public static void closeDb() {
        if (db != null && !db.isClosed()) {
            synchronized (LOCK) {
                if (db != null && !db.isClosed()) {
                    db.close();
                }
            }
        }
    }

    public static void setFileEnable(boolean fileEnable) {
        logger.info("dispatch补偿机持久化开关[{}]", fileEnable);
        MapDbUtil.fileEnable = fileEnable;
    }

    public static void main(String[] args) {
        List<Task> tasks = MapDbUtil.fetchTask("dispatch.db");
        ConcurrentMap<String, byte[]> dbObject = MapDbUtil.getDbObject("dispatch.db");
        System.out.println();
    }
}
