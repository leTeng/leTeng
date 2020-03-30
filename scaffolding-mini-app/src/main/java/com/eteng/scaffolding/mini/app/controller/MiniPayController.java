package com.eteng.scaffolding.mini.app.controller;

import com.eteng.scaffolding.common.dto.CommonResult;
import com.eteng.scaffolding.mini.app.pojo.dto.LoginResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @FileName null.java
 * @Author eTeng
 * @Date 2019/12/13 15:32
 * @Description
 */
@RestController
@RequestMapping("/mini/app/pay")
@Api(tags = "小程序支付接口")
public class MiniPayController {

    @GetMapping("/pay")
    @ApiOperation("小程序支付接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appid",value = "小程序appid",required = true),
            @ApiImplicitParam(name = "code",value = "微信授权码",required = true)
    })
    public CommonResult<LoginResult> login(@PathVariable String appid,String code){
            throw new RuntimeException("未知错误");
//        return CommonResult.success(LoginResult.builder());
    }
}
