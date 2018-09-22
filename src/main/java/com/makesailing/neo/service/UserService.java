package com.makesailing.neo.service;

import com.makesailing.neo.domain.User;
import java.util.List;

/**
 * #
 *
 * @author jamie
 * @date 2018/9/21 15:46
 */
public interface UserService {

	String SERVICE_ID = "userService";

	/**
	 * 保存用户
	 * @param user
	 * @return
	 */
	Long saveUser(User user);

	/**
	 * 更新用户
	 * @param user
	 * @return
	 */
	int updateUser(User user);

	/**
	 * 根据用户ID删除用户
	 * @param id
	 * @return
	 */
	int deleteUserById(Long id);

	/**
	 * 根据用户ID获取用户信息
	 * @param id
	 * @return
	 */
	User getUserDetailById(Long id);

	/**
	 * 获取用户列表
	 * @return
	 */
	List<User> getUserList();

}
