package com.makesailing.neo.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.alibaba.fastjson.JSON;
import com.makesailing.neo.BaseControllerTest;
import com.makesailing.neo.constant.Urls;
import com.makesailing.neo.domain.User;
import java.util.Date;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * UserController Tester.
 *
 * @author jamie
 * @since <pre>09/21/2018</pre>
 */
public class UserControllerTest extends BaseControllerTest {

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
    request = MockMvcRequestBuilders.get(Urls.User.USER_DETAIL, 1);
    String responseString = mockMvc.perform(request)
        .andExpect(status().isOk()) // 返回的状态是200
        .andDo(print()) // 打印出请求和相应的内容
        .andReturn().getResponse().getContentAsString(); // 将数据转换为字符串
    Assert.assertNotNull(responseString);
    System.out.println(responseString);

  }

  /**
   * Method: getUserList()
   */
  @Test
  public void testGetUserList() throws Exception {
    //TODO: Test goes here...
  }


  /**
   * Method: saveUser(@RequestBody User user)
   */
  @Test
  public void testSaveUser() throws Exception {
    User user = new User();
    user.setEmail("1234567@qq.com");
    user.setUsername("jack");
    user.setPassword("123456");
    user.setRole("root");
    user.setStatus(1);
    user.setRegtime(new Date());
    //user.setRegip("127.0.0.1");

    String content = JSON.toJSONString(user);
    String response = mockMvc.perform(
        post(Urls.User.SAVE_USER).contentType(MediaType.APPLICATION_JSON_UTF8).content(content))
        .andDo(print())
        .andExpect(status().isOk())
        .andReturn().getResponse().getContentAsString();
    Assert.assertNotNull(response);
    Assert.assertNotEquals(response,"");
    System.out.println(response);
  }

  /**
   * Method: updateUser(@RequestBody User user)
   */
  @Test
  public void testUpdateUser() throws Exception {
    //TODO: Test goes here...
  }

  /**
   * Method: deleteUserById(@PathVariable("id") Long id)
   */
  @Test
  public void testDeleteUserById() throws Exception {
    //TODO: Test goes here...
  }


}
