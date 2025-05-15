package com.demo.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author peter
 * @date 2025/5/6
 */
@Api(tags = "路由跳转接口")
@Controller
public class RouteController {

    // 首页
    @ApiOperation(value = "首页")
    @GetMapping("/")
    public String index() {
        return "upload";
    }
}
