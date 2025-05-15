package com.demo.utils;

import lombok.Data;

/**
 * 文件分块上传存储信息
 * @author peter
 */
@Data
public class Value {
    // 文件分块名
    private String name;
    // 分块上传状态
    private boolean[] status;


    Value(int n) {
        this.name = FileUtils.generateFileName();
        this.status = new boolean[n];
    }
}