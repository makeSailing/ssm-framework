package com.makesailing.neo.service;

import com.makesailing.neo.domain.User;

/**
 * #
 *
 * @author jamie
 * @date 2018/9/21 15:46
 */
public interface UserService {

	String SERVICE_ID = "userService";

	User getUserDetailById(Long id);
}
