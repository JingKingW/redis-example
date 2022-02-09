package com.xunmall.example.redis.spring;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by wangyanjing on 2018/11/9.
 */
@Data
public class User implements Serializable {
    private String userId;
    private String userName;
    private Integer age;
}
