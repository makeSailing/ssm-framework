package com.makesailing.neo.service.impl;

import com.alibaba.fastjson.JSON;
import com.makesailing.neo.domain.User;
import com.makesailing.neo.service.UserService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/** 
* UserServiceImpl Tester. 
* 
* @author jamie
* @since <pre>09/21/2018</pre> 
*/
// 加载spring配置文件
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-mybatis.xml"})
public class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Before
    public void setUp() throws Exception { 
    } 
    
    @After
    public void tearDown() throws Exception { 
    } 
    
    /** 
    * 
    * Method: getUserById(Long id) 
    * 
    */ 
    @Test
    public void testGetUserDetailById() throws Exception {
        User user = userService.getUserDetailById(1L);
        Assert.assertNotNull(user);
        System.out.println(JSON.toJSONString(user));
    } 
    
        
    } 
