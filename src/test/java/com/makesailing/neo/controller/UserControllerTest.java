package com.makesailing.neo.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.alibaba.fastjson.JSON;
import com.makesailing.neo.BaseControllerTest;
import com.makesailing.neo.constant.Urls;
import com.makesailing.neo.domain.User;
import java.util.Date;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.transaction.annotation.Transactional;

/**
 * UserController Tester.
 *
 * @author jamie
 * @since <pre>09/21/2018</pre>
 */
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class UserControllerTest extends BaseControllerTest {

  /**
   * Method: getUserDetailById(@PathVariable(name = "id") Long id)
   */
  @Test
  public void testGetUserDetailById() throws Exception {
    RequestBuilder request = null;
    request = get(Urls.User.USER_DETAIL, 1);
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
    String response = mockMvc.perform(get(Urls.User.USER_LIST)).andExpect(status().isOk())
        .andDo(print()).andReturn().getResponse().getContentAsString();
    Assert.assertNotNull(response);
    System.out.println(response);
  }


  /**
   * Method: saveUser(@RequestBody User user)
   */
  @Test
  public void testSaveUser() throws Exception {
    User user = new User();
    user.setEmail("12345678@qq.com");
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
    User user = new User();
    user.setId(2L);
    user.setUsername("tom");

    String respone = mockMvc.perform(
        put(Urls.User.UPDATE_USER).contentType(MediaType.APPLICATION_JSON_UTF8).content(JSON.toJSONString(user)))
        .andDo(print()).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
    Assert.assertNotNull(respone);
    System.out.println(respone);
  }

  /**
   * Method: deleteUserById(@PathVariable("id") Long id)
   */
  @Test
  public void testDeleteUserById() throws Exception {
    String response = mockMvc.perform(delete(Urls.User.DELETE_USER, 4L)).andExpect(status().isOk())
        .andDo(print()).andReturn().getResponse().getContentAsString();
    Assert.assertNotNull(response);
    System.out.println(response);
  }


}
