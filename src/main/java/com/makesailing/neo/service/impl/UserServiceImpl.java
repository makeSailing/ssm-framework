package com.makesailing.neo.service.impl;

import com.alibaba.fastjson.JSON;
import com.makesailing.neo.domain.User;
import com.makesailing.neo.mappers.UserMapper;
import com.makesailing.neo.service.UserService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserMapper userMapper;

	@Override
	public Long saveUser(User user) {
		logger.info("saveUser user parameter [{}] ", JSON.toJSONString(user));
		userMapper.insertSelective(user);
		return user.getId();
	}

	@Override
	public int updateUser(User user) {
		logger.info("updateUser user parameter [{}] " , JSON.toJSONString(user));
		return userMapper.updateByPrimaryKeySelective(user);
	}

	@Override
	public int deleteUserById(Long id) {
		return userMapper.deleteByPrimaryKey(id);
	}

	@Override
	public User getUserDetailById(Long id) {
		logger.info("getUserDetailById id parameter [{}] ", id);
		User user = userMapper.selectByPrimaryKey(id);
		return user;
	}

	@Override
	public List<User> getUserList() {
		List<User> list = userMapper.getUserList();
		return list;
	}
}


