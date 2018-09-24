## SSM的整合

**主要注意三点**

### 1. applicationContext.xml 书写

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xmlns:context="http://www.springframework.org/schema/context"
  xmlns:tx="http://www.springframework.org/schema/tx" xmlns:aop="http://www.springframework.org/schema/aop"
  xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       http://www.springframework.org/schema/context/spring-context.xsd
       http://www.springframework.org/schema/tx
       http://www.springframework.org/schema/tx/spring-tx.xsd http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop.xsd">

  <!-- 扫描service包下所有使用注解的类型 -->
  <context:component-scan base-package="com.makesailing.neo.service"/>

  <!-- 配置数据库相关参数properties的属性：${url} -->
  <context:property-placeholder location="classpath:jdbc.properties"/>

  <!-- 数据库连接池 -->
  <bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource" init-method="init" destroy-method="close">
    <property name="driverClassName" value="${jdbc.driver}" />
    <property name="url" value="${jdbc.url}" />
    <property name="username" value="${jdbc.username}" />
    <property name="password" value="${jdbc.password}" />
    <!-- 配置初始化大小、最小、最大 -->
    <property name="initialSize" value="10" />
    <property name="minIdle" value="10" />
    <property name="maxActive" value="300" />

    <!-- 配置获取连接等待超时的时间 -->
    <property name="maxWait" value="1800" />

    <!-- 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒 -->
    <property name="timeBetweenEvictionRunsMillis" value="60000" />

    <!-- 配置一个连接在池中最小生存的时间，单位是毫秒 -->
    <property name="minEvictableIdleTimeMillis" value="300000" />
    <!-- mysql SELECT 'x'  Oracle: SELECT 1 FROM DUAL -->
    <property name="validationQuery" value="SELECT 'x'" />
    <property name="testWhileIdle" value="true" />
    <property name="testOnBorrow" value="false" />
    <property name="testOnReturn" value="false" />

    <!-- 打开PSCache，并且指定每个连接上PSCache的大小 -->
    <!-- 如果用Oracle，则把poolPreparedStatements配置为true，mysql可以配置为false。分库分表较多的数据库，建议配置为false。 -->
    <property name="poolPreparedStatements" value="false" />
    <property name="maxPoolPreparedStatementPerConnectionSize" value="20" />
    <!-- 配置监控统计拦截的filters，去掉后监控界面sql无法统计 wall=防火墙 -->
    <property name="filters" value="stat" />
  </bean>

  <!-- 配置SqlSessionFactory对象 -->
  <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
    <!-- 注入数据库连接池 -->
    <property name="dataSource" ref="dataSource"/>
    <!-- 扫描model包 使用别名 -->
    <property name="typeAliasesPackage" value="com.makesailing.neo.domain"/>
    <!-- 扫描sql配置文件:mapper需要的xml文件 -->
    <property name="mapperLocations" value="classpath:mappers/*.xml"/>
  </bean>

  <!-- 配置扫描Dao接口包，动态实现Dao接口，注入到spring容器中 -->
  <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
    <!-- 注入sqlSessionFactory -->
    <property name="sqlSessionFactoryBeanName" value="sqlSessionFactory"/>
    <!-- 给出需要扫描Dao接口包 -->
    <property name="basePackage" value="com.makesailing.neo.mappers"/>
  </bean>

  <!-- 配置事务管理器 -->
  <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
    <!-- 注入数据库连接池 -->
    <property name="dataSource" ref="dataSource"/>
  </bean>

  <!-- 配置基于注解的声明式事务 -->
  <!--<tx:annotation-driven transaction-manager="transactionManager"/>-->

  <!--手动事务-->
  <!-- 通知 -->
  <tx:advice id="txAdvice" transaction-manager="transactionManager">
    <tx:attributes>
      <!-- 传播行为 -->
      <tx:method name="save*" propagation="REQUIRED" />
      <tx:method name="insert*" propagation="REQUIRED" />
      <tx:method name="add*" propagation="REQUIRED" />
      <tx:method name="create*" propagation="REQUIRED" />
      <tx:method name="delete*" propagation="REQUIRED" />
      <tx:method name="update*" propagation="REQUIRED" />
      <tx:method name="find*" propagation="SUPPORTS" read-only="true" />
      <tx:method name="select*" propagation="SUPPORTS" read-only="true" />
      <tx:method name="get*" propagation="SUPPORTS" read-only="true" />
    </tx:attributes>
  </tx:advice>
  <!-- 切面 -->
  <aop:config>
    <aop:advisor advice-ref="txAdvice"
      pointcut="execution(* com.makesailing.neo.service.*.*(..))" />
  </aop:config>

</beans>
```

### 2. spring-mvc.xml 书写

```java
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xmlns:context="http://www.springframework.org/schema/context"
  xmlns:mvc="http://www.springframework.org/schema/mvc"
  xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       http://www.springframework.org/schema/context/spring-context.xsd
       http://www.springframework.org/schema/mvc
       http://www.springframework.org/schema/mvc/spring-mvc-4.0.xsd">

  <!-- 扫描web相关的bean -->
  <context:component-scan base-package="com.makesailing.neo.controller"/>

<!--  <mvc:annotation-driven>
             <mvc:message-converters>
                 <bean class="org.springframework.http.converter.StringHttpMessageConverter"/>
                 <bean class="com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter">
                     <property name="supportedMediaTypes">
                         <list>
                             <value>text/html;charset=UTF-8</value>
                             <value>application/json;charset=UTF-8</value>  &lt;!&ndash; JSON转换器 &ndash;&gt;
                         </list>
                     </property>
                 </bean>
             </mvc:message-converters>
  </mvc:annotation-driven>-->


  <!-- 开启SpringMVC注解模式 -->
  <!-- 启用默认配置 -->
  <mvc:annotation-driven>
    <!--不使用默认消息转换器 -->
    <mvc:message-converters register-defaults="true">
      <!--spring消息转换器 -->
      <bean class="org.springframework.http.converter.ByteArrayHttpMessageConverter"/>
      <bean class="org.springframework.http.converter.BufferedImageHttpMessageConverter"/>

      <!--解决@Responcebody中文乱码问题 -->
      <bean class="org.springframework.http.converter.StringHttpMessageConverter">
        <constructor-arg value="UTF-8"/>
      </bean>
      <!--配合fastjson支持 -->
      <bean class="com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter">
        <property name="defaultCharset" value="UTF-8"/>
        <property name="supportedMediaTypes">
          <list>
            <!--顺序保持这样,避免IE下载出错 -->
            <value>text/html;charset=UTF-8</value>
            <value>application/json</value>
          </list>
        </property>
        <property name="fastJsonConfig" ref="fastJsonConfig"/>
      </bean>
    </mvc:message-converters>
  </mvc:annotation-driven>

  <!--fastJsonConfig -->
  <bean id="fastJsonConfig" class="com.alibaba.fastjson.support.config.FastJsonConfig">
    <!--默认编码格式 -->
    <property name="charset" value="UTF-8"/>

    <property name="serializerFeatures">
      <list>
        <value>WriteNullListAsEmpty</value>
        <value>WriteDateUseDateFormat</value>
        <value>PrettyFormat</value>
        <value>WriteMapNullValue</value>
        <value>WriteNullStringAsEmpty</value>
        <value>WriteNullListAsEmpty</value>
        <value>DisableCircularReferenceDetect</value>
      </list>
    </property>

  </bean>

  <!-- 静态资源默认servlet配置 -->
  <!-- org.springframework.web.servlet.resource.DefaultServletHttpRequestHandler，
    它会像一个检查员，对进入DispatcherServlet的URL进行筛查，如果发现是静态资源的请求，
    就将该请求转由Web应用服务器默认的Servlet处理，如果不是静态资源的请求，才由DispatcherServlet继续处理。 -->
   <mvc:default-servlet-handler/>

  <!-- 配置jsp 显示ViewResolver -->
  <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
    <property name="prefix" value="/WEB-INF/views/"/>
    <property name="suffix" value=".jsp"/>
  </bean>

</beans>
```

### 3. 让程序入口 web.xml 加载配置文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd"
  version="3.1">

  <display-name>ssm-framework</display-name>
  <description>ssm-framework</description>

  <!--加载spring容器配置-->
  <listener>
    <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
  </listener>

  <!--Spring容器加载所有的配置文件的路径-->
  <context-param>
    <param-name>contextConfigLocation</param-name>
    <param-value>classpath*:applicationContext.xml</param-value>
  </context-param>


  <!-- 配置DispatcherServlet -->
  <servlet>
    <servlet-name>springMvc</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <!-- 配置springMVC需要加载的配置文件-->
    <init-param>
      <param-name>contextConfigLocation</param-name>
      <param-value>classpath:spring-mvc.xml</param-value>
    </init-param>
    <!--用来标记是否在项目启动时就加在此Servlet,0或正数表示容器在应用启动时就加载这个Servlet,
        当是一个负数时或者没有指定时，则指示容器在该servlet被选择时才加载.正数值越小启动优先值越高  -->
    <load-on-startup>1</load-on-startup>
    <async-supported>true</async-supported>
  </servlet>
  <!--为DispatcherServlet建立映射-->
  <servlet-mapping>
    <servlet-name>springMvc</servlet-name>
    <!-- 拦截所有请求,千万注意是(/)而不是(/*) -->
    <url-pattern>/</url-pattern>
  </servlet-mapping>

  <!-- 编码过滤器 -->
  <filter>
    <filter-name>encodingFilter</filter-name>
    <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
    <init-param>
      <param-name>encoding</param-name>
      <param-value>UTF-8</param-value>
    </init-param>
    <init-param>
      <param-name>forceEncoding</param-name>
      <param-value>true</param-value>
    </init-param>
  </filter>
  <filter-mapping>
    <filter-name>encodingFilter</filter-name>
    <url-pattern>/*</url-pattern>
  </filter-mapping>


</web-app>
```

### 4. 项目目录

![项目目录](https://user-gold-cdn.xitu.io/2018/9/24/1660b6b08cc7a2c8?w=361&h=437&f=png&s=21291)

### 5.所依赖的jar

```xml
<?xml version="1.0" encoding="UTF-8"?>

<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>

  <groupId>com.makesaililg.neo</groupId>
  <artifactId>ssm_framework</artifactId>
  <version>1.0-SNAPSHOT</version>
  <packaging>war</packaging>

  <name>ssm_framework Maven Webapp</name>
  <!-- FIXME change it to the project's website -->
  <url>http://www.example.com</url>

  <properties>
    <!-- 设置项目编码编码 -->
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
    <!-- spring版本号 -->
    <spring.version>4.3.5.RELEASE</spring.version>
    <!-- mybatis版本号 -->
    <mybatis.version>3.4.1</mybatis.version>
  </properties>

  <dependencies>
    <!-- java ee -->
    <dependency>
      <groupId>javax</groupId>
      <artifactId>javaee-api</artifactId>
      <version>7.0</version>
    </dependency>

    <dependency>
      <groupId>javax.servlet</groupId>
      <artifactId>javax.servlet-api</artifactId>
      <version>3.1.0</version>
    </dependency>

    <!-- 单元测试 -->
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>4.12</version>
    </dependency>

    <!-- 实现slf4j接口并整合 -->
    <dependency>
      <groupId>ch.qos.logback</groupId>
      <artifactId>logback-classic</artifactId>
      <version>1.2.2</version>
    </dependency>

    <!-- JSON -->
    <dependency>
      <groupId>com.alibaba</groupId>
      <artifactId>fastjson</artifactId>
      <version>1.2.40</version>
    </dependency>


    <!-- 数据库 -->
    <dependency>
      <groupId>mysql</groupId>
      <artifactId>mysql-connector-java</artifactId>
      <version>5.1.41</version>
      <scope>runtime</scope>
    </dependency>

    <!-- 数据库连接池 -->
    <dependency>
      <groupId>com.alibaba</groupId>
      <artifactId>druid</artifactId>
      <version>1.0.29</version>
    </dependency>

    <!-- MyBatis -->
    <dependency>
      <groupId>org.mybatis</groupId>
      <artifactId>mybatis</artifactId>
      <version>${mybatis.version}</version>
    </dependency>

    <!-- mybatis/spring整合包 -->
    <dependency>
      <groupId>org.mybatis</groupId>
      <artifactId>mybatis-spring</artifactId>
      <version>1.3.1</version>
    </dependency>

    <!-- Spring -->
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-core</artifactId>
      <version>${spring.version}</version>
    </dependency>
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-beans</artifactId>
      <version>${spring.version}</version>
    </dependency>
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-context</artifactId>
      <version>${spring.version}</version>
    </dependency>
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-jdbc</artifactId>
      <version>${spring.version}</version>
    </dependency>
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-tx</artifactId>
      <version>${spring.version}</version>
    </dependency>
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-web</artifactId>
      <version>${spring.version}</version>
    </dependency>
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-webmvc</artifactId>
      <version>${spring.version}</version>
    </dependency>
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-test</artifactId>
      <version>${spring.version}</version>
    </dependency>

    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-aop</artifactId>
      <version>${spring.version}</version>
    </dependency>

    <dependency>
      <groupId>org.aspectj</groupId>
      <artifactId>aspectjweaver</artifactId>
      <version>1.6.8</version>
    </dependency>


  </dependencies>

  <build>
    <finalName>ssm_framework</finalName>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <configuration>
          <!-- 设置JDK版本 -->
          <source>1.8</source>
          <target>1.8</target>
        </configuration>
      </plugin>
      <!--mybatis 代码生产器-->
      <plugin>
        <groupId>org.mybatis.generator</groupId>
        <artifactId>mybatis-generator-maven-plugin</artifactId>
        <version>1.3.2</version>
        <configuration>
          <configurationFile>${basedir}/src/main/resources/generator/generatorConfig.xml</configurationFile>
          <overwrite>true</overwrite>
          <verbose>true</verbose>
        </configuration>
      </plugin>
    </plugins>
  </build>
</project>

```



