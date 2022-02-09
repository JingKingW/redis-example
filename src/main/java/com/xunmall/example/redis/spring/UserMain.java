package com.xunmall.example.redis.spring;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by wangyanjing on 2018/11/9.
 */
public class UserMain {

    @Test
    public void testDefined() {

        UserService userService = new UserService();

        userService.getUserById("001001");
        userService.getUserById("001001");

        userService.reload();

        System.out.println("after reload...");

        userService.getUserById("001001");
        userService.getUserById("001001");

    }

    @Test
    public void testSpringCache() {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:/spring-cache.xml");
        UserServiceBean userserviceBean = (UserServiceBean) context.getBean("userServiceBean");
        System.out.println("First query ...");
        userserviceBean.getUserById("000111");
        System.out.println("Second query ...");
        userserviceBean.getUserById("000111");
        System.out.println("Three query ...");
        userserviceBean.getUserById("000111");
    }


}
