package com.makesailing.neo.service;

import com.makesailing.neo.domain.User;

/**
 * #
 *
 * @author <a href="mailto:jamie.li@wolaidai.com">jamie.li</a>
 * @date 2018/9/21 15:46
 */
public interface UserService {

	String SERVICE_ID = "userService";

	User getUserDetailById(Long id);
}
