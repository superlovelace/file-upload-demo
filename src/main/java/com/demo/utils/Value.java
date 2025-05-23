package com.demo.utils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 文件分块上传存储信息
 * @author peter
 * 此类只供 大文件分片记录在ConcurrentHashMap时使用
 */
@Data
@NoArgsConstructor // 添加 Lombok 无参构造
@AllArgsConstructor // 可选：全参构造
@JsonIgnoreProperties(ignoreUnknown = true) // 忽略未知字段
public class Value implements Serializable {
    private String name;
    private boolean[] status;

    // 标注自定义构造函数的 JSON 映射
    @JsonCreator
    public Value(@JsonProperty("n") int n) {
        this.name = FileUtils.generateFileName();
        this.status = new boolean[n];
    }

    private static final long serialVersionUID = 1L;


}