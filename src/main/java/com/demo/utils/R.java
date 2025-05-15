package com.demo.utils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 统一返回结果
 * @author peter
 */
@Data
@ApiModel(description = "统一返回结果")
public class R<T> {

    @ApiModelProperty(value = "响应码",name = "code",example = "200")
    private int code;

    @ApiModelProperty(value = "响应信息",name = "msg",example = "请求成功")
    private String msg;

    @ApiModelProperty(value = "响应数据",name = "data",example = "null")
    private T data;

    public static R<Void> ok() {
        return ok(null);
    }

    public static <T> R<T> ok(T data) {
        return new R<>(200, "OK", data);
    }

    public static <T> R<T> success(int code, String msg,T data) {
        return new R<>(code, msg, data);
    }

    public static <T> R<T> error(String msg) {
        return new R<>(500, msg, null);
    }

    public static <T> R<T> error(int code, String msg) {
        return new R<>(code, msg, null);
    }

    public R() {
    }

    public R(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public boolean success(){
        return code == 200;
    }
}
