package com.makesailing.neo.service.impl;

import com.makesailing.neo.domain.User;
import com.makesailing.neo.mappers.UserMapper;
import com.makesailing.neo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * #
 *
 * @author jamie
 * @date 2018/9/21 15:47
 */
@Service(UserService.SERVICE_ID)
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;

	@Override
	public User getUserDetailById(Long id) {
		User user = userMapper.selectByPrimaryKey(id);
		return user;
	}
}


