package com.makesailing.neo.controller;

import com.alibaba.fastjson.JSON;
import com.makesailing.neo.domain.User;
import com.makesailing.neo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * #
 *
 * @author jamie
 * @date 2018/9/21 15:50
 */
@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/{id}/detail")
	public String getUserDetailById(@PathVariable(name = "id") Long id) {
		User user = userService.getUserDetailById(id);
		return JSON.toJSONString(user);
	}

}


