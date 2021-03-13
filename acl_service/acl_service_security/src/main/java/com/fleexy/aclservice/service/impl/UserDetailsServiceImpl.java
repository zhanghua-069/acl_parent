package com.fleexy.aclservice.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.fleexy.aclservice.entity.User;
import com.fleexy.aclservice.service.PermissionService;
import com.fleexy.aclservice.service.UserService;
import com.fleexy.aclcommon.security.entity.SecurityUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Autowired
    private PermissionService permissionService;

    // 查询用户名信息与用户权限，返回UserDetails对象
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 使用用户名查询数据库获取用户信息
        User user = userService.selectByUsername(username);
        if(user == null) {
            throw new UsernameNotFoundException("用户不存在！");
        }
        // 用户对象转换为Security用户实体类
        com.fleexy.aclcommon.security.entity.User curUser = new com.fleexy.aclcommon.security.entity.User();
        BeanUtil.copyProperties(user, curUser);

        // 通过用户ID查询用户权限列表
        List<String> permissionValueList = permissionService.selectPermissionValueByUserId(user.getId());
        SecurityUser securityUser = new SecurityUser(curUser);
        securityUser.setPermissionValueList(permissionValueList);
        return securityUser;
    }
}
