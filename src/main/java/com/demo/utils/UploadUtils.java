package com.demo.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 分块上传工具类
 * @author peter
 */
public class UploadUtils {

    private UploadUtils() {
        throw new IllegalStateException("Utility class");
    }

    // 存储分块上传文件信息
    protected static final Map<String, Value> CHUNK_MAP = new ConcurrentHashMap<>();

    /**
     * 获取分块上传文件信息
     * @param key sha256文件指纹
     * @return 文件信息
     */
    public static Map<String, Object> getChunkInfo(String key) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, CHUNK_MAP.get(key).getName());
        map.put("status", CHUNK_MAP.get(key).getStatus());
        return map;
    }

    /**
     * 判断文件所有分块是否已上传
     * @param key sha256文件指纹
     * @return true:所有分块已上传
     */
    public static boolean isUploaded(String key) {
        if (isExist(key)) {
            for (boolean b : CHUNK_MAP.get(key).getStatus()) {
                if (!b) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 判断文件是否有分块已上传
     * @param key sha256文件指纹
     * @return true:有分块已上传
     */
    private static boolean isExist(String key) {
        return CHUNK_MAP.containsKey(key);
    }

    /**
     * 为文件添加上传分块记录
     * @param key sha256文件指纹
     * @param chunk 分块序号
     */
    public static void addChunk(String key, int chunk) {
        CHUNK_MAP.get(key).getStatus()[chunk] = true;
    }

    /**
     * 从map中删除键为key的键值对
     * @param key sha256文件指纹
     */
    public static void removeKey(String key) {
        if (isExist(key)) {
            CHUNK_MAP.remove(key);
        }
    }

    /**
     * 获取随机生成的文件名
     * @param key  文件SHA256指纹
     * @param chunks 总块数
     * @return 生成的随机文件名
     */
    public static String getFileName(String key, int chunks) {
        if (!isExist(key)) {
            synchronized (UploadUtils.class) {
                if (!isExist(key)) {
                    CHUNK_MAP.put(key, new Value(chunks));
                }
            }
        }
        return CHUNK_MAP.get(key).getName();
    }

    public static Map<String, Value> getChunkMapInfo() {
        return CHUNK_MAP;
    }
}
