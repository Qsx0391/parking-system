package com.qsx.parking.utils;

import com.alibaba.druid.util.StringUtils;
import com.qsx.parking.common.context.UserInfoDTO;
import io.jsonwebtoken.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * JWT工具类 | 用于生成和解析 token
 * <p>
 * 作者：Qsx
 * 开发时间：2025-02-25
 */
@Data
@Component
@ConfigurationProperties(prefix = "jwt.token")
public class JwtHelper {

    /**
     * token 过期时间，单位分钟
     */
    @Value("${jwt.token.tokenExpiration:}")
    private Long tokenExpiration;

    /**
     * token 签名秘钥
     */
    @Value("${jwt.token.tokenSignKey:}")
    private String tokenSignKey;

    /**
     * 生成token
     * @param userId 用户 ID
     * @return token
     */
    public String createToken(long userId, int userType) {
        //单位分钟
        return Jwts.builder()
                .setSubject("PARKING-USER")
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration * 1000 * 60))
                .claim("userId", userId)
                .claim("userType", userType)
                .signWith(SignatureAlgorithm.HS512, tokenSignKey)
                .compressWith(CompressionCodecs.GZIP)
                .compact();
    }

    /**
     * 从 token 中获取用户信息
     * @param token token
     * @return 用户信息
     */
    public UserInfoDTO getUserInfo(String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        UserInfoDTO userInfoDTO = UserInfoDTO.builder()
                .userId(((Number) claims.get("userId")).longValue())
                .userType((Integer) claims.get("userType"))
                .build();
        return userInfoDTO.getUserId() != null && userInfoDTO.getUserType() != null ? userInfoDTO : null;
    }

    /**
     * 检查 token 是否过期
     * @param token token
     * @return true表示已过期，false表示未过期
     */
    public boolean isExpiration(String token) {
        try {
            //没有过期，有效，返回false
            return Jwts.parser()
                    .setSigningKey(tokenSignKey)
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration().before(new Date());
        } catch (Exception e) {
            //过期出现异常，返回true
            return true;
        }
    }
}
