package com.makesailing.neo.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.makesailing.neo.AbstractContextControllerTests;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/** 
* UserController Tester. 
* 
* @author jamie
* @since <pre>09/21/2018</pre> 
*/
@RunWith(SpringJUnit4ClassRunner.class)  //此处调用Spring单元测试类
//@WebAppConfiguration    //调用javaWEB的组件，比如自动注入ServletContext Bean等等
//@ContextConfiguration(locations = {"classpath:spring-mvc.xml","classpath:spring-mybatis.xml"})//加载Spring配置文件
public class UserControllerTest extends AbstractContextControllerTests {

    /**
     * 模拟mvc测试对象
     */
    private MockMvc mockMvc;


    /**
     * 所有测试方法执行之前执行该方法
     */
    @Before
    public void before() {
        //获取mockmvc对象实例
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    } 
    
    @After
    public void tearDown() throws Exception { 
    }

    /**
     * Method: getUserDetailById(@PathVariable(name = "id") Long id)
     */
    @Test
    public void testGetUserDetailById() throws Exception {
        RequestBuilder request = null;
        MvcResult result = null;
        request = MockMvcRequestBuilders.get("/user/{id}/detail",1);
        result = mockMvc.perform(request).andExpect(status().isOk())
            .andReturn();
        Assert.assertNotNull(result);
    } 
    
        
    } 
