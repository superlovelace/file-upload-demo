package com.demo.utils;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 分块上传工具类
 * @author peter
 */
@Component
@RequiredArgsConstructor
public class UploadUtilsByRedis {

    private final StringRedisTemplate stringRedisTemplate;

    private final RedissonClient redissonClient;


    /**
     * 文件上传分片信息存储：前缀key
     */
    private static final String PREFIX_KEY = "file_uploaded:";

    /**
     * 文件上传分片信息存储：后缀key[分片存储名称]
     */
    private static final String SUFFIX_KEY_NAME = ":name";

    /**
     * 文件上传分片信息存储：后缀key[分片存储状态]
     */
    private static final String SUFFIX_KEY_STATUS = ":status";

    private static final DefaultRedisScript<Long> INITIAL_CHUNK;
    private static final DefaultRedisScript<Long> UPDATE_CHUNK;

    // lua脚本初始化
    static {
        INITIAL_CHUNK = new DefaultRedisScript<>();
        INITIAL_CHUNK.setLocation(new ClassPathResource("/scripts/initial_chunk.lua"));
        INITIAL_CHUNK.setResultType(Long.class);

        UPDATE_CHUNK = new DefaultRedisScript<>();
        UPDATE_CHUNK.setLocation(new ClassPathResource("/scripts/update_chunk.lua"));
        UPDATE_CHUNK.setResultType(Long.class);
    }

    /**
     * 获取分块上传文件信息
     * @param key sha256文件指纹
     * @return 文件信息
     */
    public Map<String, Object> getChunkInfo(String key) {
        Map<String, Object> map = new HashMap<>();
        if (!isExist(key)) {
            return Collections.emptyMap();
        }
        String name = stringRedisTemplate.opsForValue().get(PREFIX_KEY+key+SUFFIX_KEY_NAME);
        String status =  stringRedisTemplate.opsForValue().get(PREFIX_KEY+key+SUFFIX_KEY_STATUS);
        map.put("sha256", key);
        map.put("name", name);
        map.put("status", status);
        return map;
    }

    /**
     * 判断文件所有分块是否已上传
     * @param key sha256文件指纹
     * @return true:所有分块已上传
     */
    public boolean isUploaded(String key) {
        if (isExist(key)) {
            String status = stringRedisTemplate.opsForValue().get(PREFIX_KEY+key+SUFFIX_KEY_STATUS);

            return status != null && !status.contains("0");
        }
        return false;
    }

    /**
     * 判断文件是否有分块已上传
     * @param key sha256文件指纹
     * @return true:有分块已上传
     */
    private boolean isExist(String key) {

        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(PREFIX_KEY + key + SUFFIX_KEY_NAME));
    }

    /**
     * 为文件添加上传分块记录
     * @param key sha256文件指纹
     * @param chunk 分块序号
     */
    public void addChunk(String key, int chunk){

        if (chunk==0){
            chunk = 1;
        }else {
            chunk++;
        }

        // 3. 执行脚本
        Long result = stringRedisTemplate.execute(
                UPDATE_CHUNK,
                Arrays.asList( PREFIX_KEY + key + SUFFIX_KEY_NAME,PREFIX_KEY + key + SUFFIX_KEY_STATUS),
                String.valueOf(chunk),
                String.valueOf(TimeUnit.DAYS.toMillis(1))
        );



        if (result == null || result != 1) {
            throw new IllegalArgumentException("Lua脚本执行失败");
        }

    }

    /**
     * 从map中删除键为key的键值对
     * @param key sha256文件指纹
     */
    public void removeKey(String key) {
        if (isExist(key)) {
            stringRedisTemplate.delete(Arrays.asList(PREFIX_KEY+key+SUFFIX_KEY_NAME, PREFIX_KEY+key+SUFFIX_KEY_STATUS));
        }
    }

    /**
     * 获取随机生成的文件名
     * @param key  文件SHA256指纹
     * @param chunks 总块数
     * @return 生成的随机文件名
     */
    public String getFileName(String key, int chunks) {
        if (!isExist(key)) {
            synchronized (UploadUtilsByRedis.class) {
                if (!isExist(key)) {
                    Value2 value2 = new Value2(chunks);

                    Long result = stringRedisTemplate.execute(
                            INITIAL_CHUNK,
                            Arrays.asList( PREFIX_KEY + key + SUFFIX_KEY_NAME,
                                    PREFIX_KEY + key + SUFFIX_KEY_STATUS),
                            value2.getName(),
                            value2.getStatus(),
                            String.valueOf(TimeUnit.DAYS.toMillis(1))
                    );

                    if (result == null || result != 1) {
                        throw new IllegalArgumentException("Lua脚本执行失败");
                    }
                }
            }
        }
        return  stringRedisTemplate.opsForValue().get(PREFIX_KEY+key+SUFFIX_KEY_NAME);
    }

    public Map<String, Value> getChunkMapInfo() {
        Value value = new Value(100);
        String key = "test";
        stringRedisTemplate.opsForHash().put(PREFIX_KEY,key, value);
        Value value1 = (Value) stringRedisTemplate.opsForHash().get(PREFIX_KEY,key);
        Map<String, Value> map = new HashMap<>();
        map.put(key, value1);
        return map;
    }
}
