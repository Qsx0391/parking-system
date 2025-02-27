package com.qsx.parking.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qsx.parking.common.enums.UserTypeEnum;
import com.qsx.parking.dao.mapper.UserMapper;
import com.qsx.parking.dao.entity.UserDO;
import com.qsx.parking.dto.req.UserLoginReqDTO;
import com.qsx.parking.dto.req.UserRegisterReqDTO;
import com.qsx.parking.dto.resp.UserLoginRespDTO;
import com.qsx.parking.framework.errorcode.ErrorCodeEnum;
import com.qsx.parking.framework.exception.ClientException;
import com.qsx.parking.service.UserService;
import com.qsx.parking.utils.JwtHelper;
import com.qsx.parking.utils.MD5Util;
import com.qsx.parking.utils.VerifyingUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {

    private final JwtHelper jwtHelper;
    private final UserMapper userMapper;

    @Override
    public UserLoginRespDTO userLogin(UserLoginReqDTO requestParam) {
        if (!VerifyingUtil.checkUsername(requestParam.getUsername()) || !VerifyingUtil.checkPassword(requestParam.getPassword())) {
            throw new ClientException(ErrorCodeEnum.USER_OR_PASSWORD_NOT_EXIST_ERROR);
        }

        LambdaQueryWrapper<UserDO> wrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername, requestParam.getUsername())
                .eq(UserDO::getPassword, MD5Util.encrypt(requestParam.getPassword()));
        UserDO userDO = userMapper.selectOne(wrapper);

        if (userDO == null) {
            throw new ClientException(ErrorCodeEnum.USER_OR_PASSWORD_NOT_EXIST_ERROR);
        }

        return UserLoginRespDTO.builder()
                .token(jwtHelper.createToken(userDO.getId(), userDO.getType()))
                .type(userDO.getType())
                .build();
    }

    @Override
    public void checkUsername(String username) {
        if (!VerifyingUtil.checkUsername(username)) {
            throw new ClientException(ErrorCodeEnum.USER_NAME_VERIFY_ERROR);
        }
        LambdaQueryWrapper<UserDO> wrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername, username);
        if (userMapper.selectCount(wrapper) != 0) {
            throw new ClientException(ErrorCodeEnum.USER_NAME_EXIST_ERROR);
        }
    }

    @Override
    @Transactional
    public void userRegister(UserRegisterReqDTO requestParam) {
        if (!VerifyingUtil.checkPassword(requestParam.getPassword())) {
            throw new ClientException(ErrorCodeEnum.PASSWORD_VERIFY_ERROR);
        }

        checkUsername(requestParam.getUsername());

        UserDO userDO = UserDO.builder()
                .username(requestParam.getUsername())
                .password(MD5Util.encrypt(requestParam.getPassword()))
                .type(UserTypeEnum.PARKING_USER.getType())
                .build();
        userMapper.insert(userDO);
    }

    @Override
    public void checkLogin(String token) {
        if (StrUtil.isEmpty(token)) {
            throw new ClientException(ErrorCodeEnum.IDEMPOTENT_TOKEN_NULL_ERROR);
        }
        if (jwtHelper.isExpiration(token)) {
            throw new ClientException(ErrorCodeEnum.IDEMPOTENT_TOKEN_DELETE_ERROR);
        }
    }
}
