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
 * 此类只供 大文件分片记录在redis时使用
 */
@Data
@NoArgsConstructor // 添加 Lombok 无参构造
@AllArgsConstructor // 可选：全参构造
@JsonIgnoreProperties(ignoreUnknown = true) // 忽略未知字段
public class Value2 implements Serializable {
    private String name;
    private String status;

    // 标注自定义构造函数的 JSON 映射
    @JsonCreator
    public Value2(@JsonProperty("n") int n) {
        this.name = FileUtils.generateFileName();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append("0");
        }
        this.status = sb.toString();
    }

    private static final long serialVersionUID = 1L;


}