package com.fleexy.aclcommon.security.handler;

import com.fleexy.aclcommon.base.utils.RespData;
import com.fleexy.aclcommon.base.utils.ResponseUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 退出处理器
 *
 * 操作步骤：
 * 1.从请求header获取token
 * 2.token不为空，移除token；
 *  2.1：移除header中的token
 *  2.2：从token中获取用户名，通过用户名删除redis中的用户权限信息
 */
public class TokenLogoutHandler implements LogoutHandler {

    private TokenManager tokenManager;
    private RedisTemplate redisTemplate;

    public TokenLogoutHandler(TokenManager tokenManager, RedisTemplate redisTemplate) {
        this.tokenManager = tokenManager;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // 从header中获取token
        String token = request.getHeader("token");
        if(token != null) {
            // 移除header中的token
            tokenManager.removeToken(token);
            // 通过token获取用户名，清空当前用户缓存中的权限数据
            String username = tokenManager.getUserFromToken(token);
            redisTemplate.delete(username);
        }
        ResponseUtil.out(response, RespData.ok());// response返回操作成功信息
    }
}
