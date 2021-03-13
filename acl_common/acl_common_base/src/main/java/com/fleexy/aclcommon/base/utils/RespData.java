package com.fleexy.aclcommon.base.utils;

import lombok.Data;
import java.util.HashMap;
import java.util.Map;

//统一返回结果的类
@Data
public class RespData {

    private Boolean success;

    private Integer code;

    private String message;

    private Map<String, Object> data = new HashMap<String, Object>();

    //把构造方法私有
    private RespData() {}

    //成功静态方法
    public static RespData ok() {
        RespData r = new RespData();
        r.setSuccess(true);
        r.setCode(20000);
        r.setMessage("成功");
        return r;
    }

    //失败静态方法
    public static RespData error() {
        RespData r = new RespData();
        r.setSuccess(false);
        r.setCode(20001);
        r.setMessage("失败");
        return r;
    }

    public RespData success(Boolean success){
        this.setSuccess(success);
        return this;
    }

    public RespData message(String message){
        this.setMessage(message);
        return this;
    }

    public RespData code(Integer code){
        this.setCode(code);
        return this;
    }

    public RespData data(String key, Object value){
        this.data.put(key, value);
        return this;
    }

    public RespData data(Map<String, Object> map){
        this.setData(map);
        return this;
    }
}
