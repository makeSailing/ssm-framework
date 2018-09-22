package com.makesailing.neo.controller;

import com.alibaba.fastjson.JSON;
import com.makesailing.neo.constant.Urls;
import com.makesailing.neo.domain.User;
import com.makesailing.neo.service.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * #
 *
 * @author jamie
 * @date 2018/9/21 15:50
 */
@RestController
public class UserController {

	@Autowired
	private UserService userService;


	@GetMapping(Urls.User.USER_DETAIL)
	public String getUserDetailById(@PathVariable(name = "id") Long id) {
		User user = userService.getUserDetailById(id);
		return JSON.toJSONString(user);
	}

	@PostMapping(Urls.User.SAVE_USER)
	public Long saveUser(@RequestBody User user) {
		Long userId = userService.saveUser(user);
		return userId;
	}

	@PutMapping(Urls.User.UPDATE_USER)
	public int updateUser(@RequestBody User user) {
		int result = userService.updateUser(user);
		return result;
	}

	@DeleteMapping(Urls.User.DELETE_USER)
	public int deleteUserById(@PathVariable("id") Long id) {
		return  userService.deleteUserById(id);
	}

	@GetMapping(Urls.User.USER_LIST)
	public List<User> getUserList() {
		return userService.getUserList();
	}
}


