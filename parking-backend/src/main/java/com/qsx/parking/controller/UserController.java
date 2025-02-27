package com.qsx.parking.controller;

import com.qsx.parking.dto.req.UserLoginReqDTO;
import com.qsx.parking.dto.req.UserRegisterReqDTO;
import com.qsx.parking.service.UserService;
import com.qsx.parking.framework.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户服务控制层
 * <p>
 * 作者：Qsx
 * 开发时间：2025-02-25
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "用户管理")
public class UserController {

    private final UserService userService;

    @Operation(summary = "用户登录")
    @PostMapping("api/user/login")
    public Result<Object> login(@RequestBody UserLoginReqDTO requestParam) {
        return Result.success(userService.userLogin(requestParam));
    }

    @Operation(summary = "检查用户名是否已被占用")
    @PostMapping("api/user/check/username")
    public Result<Object> checkUsername(String username) {
        userService.checkUsername(username);
        return Result.success();
    }

    @Operation(summary = "用户注册")
    @PostMapping("api/user/register")
    public Result<Object> register(@RequestBody UserRegisterReqDTO requestParam) {
        userService.userRegister(requestParam);
        return Result.success();
    }

    @Operation(summary = "检查用户是否登录")
    @GetMapping("api/user/check/login")
    public Result<Object> checkLogin(@RequestHeader String token){
        userService.checkLogin(token);
        return Result.success();
    }
}
