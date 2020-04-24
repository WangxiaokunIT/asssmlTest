package com.xinshang.core.vo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

/**
 * @author zhangjiajia
 */
public class ResultVO extends HashMap<String, Object> {

    private static ObjectMapper mapper = new ObjectMapper();

    private ResultVO() { } //禁止被new调用

    public static ResultVO of() {
        return new ResultVO();
    }
    public static ResultVO of(int code) {
        ResultVO er = new ResultVO();
        er.put("code", code);
        return er;
    }
    public static ResultVO of(int code,String msg) {
        ResultVO er = new ResultVO();
        er.put("code", code);
        er.put("msg", msg);
        return er;
    }
    public static ResultVO of(int code,String msg,Object data) {
        ResultVO er = new ResultVO();
        er.put("code", code);
        er.put("msg", msg);
        er.put("data", data);
        return er;
    }

    @Autowired
    public ResultVO put(String key,Object value) {
        super.put(key, value);
        return this;
    }

    @Override
    public String toString() {
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
