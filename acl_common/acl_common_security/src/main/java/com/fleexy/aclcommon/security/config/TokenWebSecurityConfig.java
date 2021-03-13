package com.fleexy.aclcommon.security.config;

import com.fleexy.aclcommon.security.filter.TokenAuthenticationFilter;
import com.fleexy.aclcommon.security.filter.TokenLoginFilter;
import com.fleexy.aclcommon.security.handler.DefaultPasswordEncoder;
import com.fleexy.aclcommon.security.handler.TokenLogoutHandler;
import com.fleexy.aclcommon.security.handler.TokenManager;
import com.fleexy.aclcommon.security.handler.UnauthorizedEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class TokenWebSecurityConfig extends WebSecurityConfigurerAdapter {

    private DefaultPasswordEncoder defaultPasswordEncoder;
    private UserDetailsService userDetailsService;
    private TokenManager tokenManager;
    private RedisTemplate redisTemplate;

//    @Autowired
    public TokenWebSecurityConfig(DefaultPasswordEncoder defaultPasswordEncoder, UserDetailsService userDetailsService, TokenManager tokenManager, RedisTemplate redisTemplate) {
        this.defaultPasswordEncoder = defaultPasswordEncoder;
        this.userDetailsService = userDetailsService;
        this.tokenManager = tokenManager;
        this.redisTemplate = redisTemplate;
    }

    // 设置退出时的地址以及token，redis操作对象
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.exceptionHandling()
                .authenticationEntryPoint(new UnauthorizedEntryPoint())// 无权限访问配置
            .and().csrf().disable()
            .authorizeRequests().anyRequest().authenticated()
            .and().logout().logoutUrl("/admin/acl/index/logout") // 退出路径设置
                .addLogoutHandler(new TokenLogoutHandler(tokenManager, redisTemplate))// 退出处理器
            .and()
                .addFilter(new TokenLoginFilter(authenticationManager(), tokenManager, redisTemplate))// 添加认证过滤器，使用容器自带的authenticationManager
                .addFilter(new TokenAuthenticationFilter(authenticationManager(), tokenManager, redisTemplate))// 授权过滤器
            .httpBasic();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(defaultPasswordEncoder);
    }

    // 不需要进行认证可以直接访问的路径，也可以在configure(HttpSecurity http)中通过and()添加
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/api/**");
    }
}
