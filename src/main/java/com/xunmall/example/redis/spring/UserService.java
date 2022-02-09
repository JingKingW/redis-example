package com.xunmall.example.redis.spring;

/**
 * Created by wangyanjing on 2018/11/9.
 */
public class UserService {

    private CacheManager<User> cacheManager;

    public UserService() {
        cacheManager = new CacheManager<User>();
    }

    public User getUserById(String userId) {
        User user = cacheManager.getValue(userId);
        if (user != null) {
            System.out.println("get from cache..." + userId);
            return user;
        }
        user = getFromDB(userId);
        if (user != null) {
            cacheManager.addOrUpdateCache(userId, user);

        }
        return user;
    }

    public void reload() {
        cacheManager.evictCache();
    }

    private User getFromDB(String userId) {
        System.out.println("real querying db ..." + userId);
        User user = new User();
        user.setUserId(userId);
        user.setUserName("Tom");
        user.setAge(26);
        return user;
    }


}
