package com.makesailing.neo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.WebApplicationContext;

@WebAppConfiguration
@ContextConfiguration({ "classpath:spring-mvc.xml", "classpath:spring-mybatis.xml"})
public class AbstractContextControllerTests {

	@Autowired
	protected WebApplicationContext webApplicationContext;
}