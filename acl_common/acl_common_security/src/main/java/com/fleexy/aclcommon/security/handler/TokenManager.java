package com.fleexy.aclcommon.security.handler;

import cn.hutool.core.date.DateUtil;
import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

/**
 * token操作工具类
 * 作用：
 * 1.生成token
 * 2.从token中获取用户信息
 * 3.删除token
 */
@Component
public class TokenManager {

    // token过期时间，单位ms
    private long tokenExpiration = 24 * 60 * 60 * 1000;
    // hash签名密码
    private String tokenSignKey = "wtnb233";

    /**
     * 使用jwt根据用户名生成token
     *
     * @param username
     * @return
     */
    public String generToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(DateUtil.date(System.currentTimeMillis() + tokenExpiration))
                .signWith(SignatureAlgorithm.HS512, tokenSignKey)
                .compressWith(CompressionCodecs.GZIP)
                .compact();
    }

    /**
     * 从token中获取用户信息
     *
     * @param token
     * @return
     */
    public String getUserFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(tokenSignKey)
                .parseClaimsJws(token)
                .getBody().getSubject();
    }

    /**
     * 删除token：jwttoken无需删除，客户端扔掉即可
     * @param token
     */
    public void removeToken(String token) {

    }
}
