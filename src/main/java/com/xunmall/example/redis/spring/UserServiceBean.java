package com.xunmall.example.redis.spring;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * Created by wangyanjing on 2018/11/9.
 */
@Service
public class UserServiceBean {

    @Cacheable(cacheNames = "users", condition = "#userId.length() > 5")
    public User getUserById(String userId) {
        System.out.println("real query user ..." + userId);
        return getFromDB(userId);
    }

    private User getFromDB(String userId) {
        System.out.println("real querying db ..." + userId);
        User user = new User();
        user.setUserId(userId);
        user.setUserName("Jake");
        user.setAge(26);
        return user;
    }


}
