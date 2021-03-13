package com.fleexy.aclcommon.security.filter;

import cn.hutool.core.util.StrUtil;
import com.fleexy.aclcommon.base.utils.RespData;
import com.fleexy.aclcommon.base.utils.ResponseUtil;
import com.fleexy.aclcommon.security.handler.TokenManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 授权过滤器
 */
public class TokenAuthenticationFilter extends BasicAuthenticationFilter {

    private TokenManager tokenManager;
    private RedisTemplate redisTemplate;

    public TokenAuthenticationFilter(AuthenticationManager authenticationManager, TokenManager tokenManager, RedisTemplate redisTemplate) {
        super(authenticationManager);
        this.tokenManager = tokenManager;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 认证用户授权
     * 1.获取用户权限列表
     * 2.将用户权限列表放到security请求上下文中
     *
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 获取用户权限
        UsernamePasswordAuthenticationToken authentication = this.getAuthentication(request);
        if(authentication != null) {
            // 用户权限不为空，放到security上下文中
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else {
            ResponseUtil.out(response, RespData.error());
        }
        // 请求放行，继续执行后面的过滤器
        chain.doFilter(request, response);
    }

    /**
     * 获取用户的权限列表
     * 执行内容：
     * 1.从请求header中获取token
     * 2.从token中获取用户名
     * 3.根据用户名从redis中获取用户权限列表
     * 4.将用户信息包含权限封装成UsernamePasswordAuthenticationToken对象进行返回
     *
     * @param request
     * @return
     */
    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        // 从header中获取token
        String token = request.getHeader("token");
        if (StrUtil.isNotBlank(token)) {
            // 从token中获取用户名
            String username = tokenManager.getUserFromToken(token);
            // 根据用户名从redis中获取用户权限列表
            List<String> permissionValueList = (List<String>) redisTemplate.opsForValue().get(username);
            Collection<SimpleGrantedAuthority> authorities = permissionValueList.stream()
                    .map(permissionValue -> new SimpleGrantedAuthority(permissionValue))
                    .collect(Collectors.toList());
            if (StrUtil.isNotBlank(username)) {
                // 返回用户权限信息
                return new UsernamePasswordAuthenticationToken(username, token, authorities);
            }
        }
        return null;// token为空，直接返回null
    }
}
