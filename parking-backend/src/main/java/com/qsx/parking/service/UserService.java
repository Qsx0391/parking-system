package com.qsx.parking.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qsx.parking.dao.entity.UserDO;
import com.qsx.parking.dto.req.UserLoginReqDTO;
import com.qsx.parking.dto.req.UserRegisterReqDTO;
import com.qsx.parking.dto.resp.UserLoginRespDTO;

/**
 * 用户服务逻辑层
 * <p>
 * 作者：Qsx
 * 开发时间：2025-02-25
 */
public interface UserService extends IService<UserDO> {

    /**
     * 用户登录
     * @param requestParam 请求参数
     * @return token
     */
    UserLoginRespDTO userLogin(UserLoginReqDTO requestParam);

    /**
     * 校验用户名，检查用户名是否存在
     * @param username 用户名
     */
    void checkUsername(String username);

    /**
     * 用户注册
     * @param requestParam 请求参数
     */
    void userRegister(UserRegisterReqDTO requestParam);

    /**
     * 检查登录状态
     * @param token token
     */
    void checkLogin(String token);
}
