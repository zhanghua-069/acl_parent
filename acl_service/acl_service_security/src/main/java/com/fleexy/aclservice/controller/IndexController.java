package com.fleexy.aclservice.controller;

import com.alibaba.fastjson.JSONObject;
import com.fleexy.aclservice.service.IndexService;
import com.fleexy.aclcommon.base.utils.RespData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/acl/index")
//@CrossOrigin
public class IndexController {

    @Autowired
    private IndexService indexService;

    /**
     * 根据token获取用户信息
     */
    @GetMapping("info")
    public RespData info(){
        //获取当前登录用户用户名（用Security替代Session，从Security上下文中获取用户名）
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Map<String, Object> userInfo = indexService.getUserInfo(username);
        return RespData.ok().data(userInfo);
    }

    /**
     * 获取用户可访问的菜单
     * @return
     */
    @GetMapping("menu")
    public RespData getMenu(){
        //获取当前登录用户用户名
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<JSONObject> permissionList = indexService.getMenu(username);
        return RespData.ok().data("permissionList", permissionList);
    }

    @PostMapping("logout")
    public RespData logout(){
        return RespData.ok();
    }

}
