package com.mokelay;

import junit.framework.TestCase;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by CarlChen on 2017/5/24.
 */
public class MokelayBaseTest extends TestCase implements TestConfig {
    protected ApplicationContext context;

    /**
     * @return
     */
    public String getServerConfigName() {
        return "test_server.xml";
    }

    /**
     * Set Up
     */
    public void setUp() {
        context = new ClassPathXmlApplicationContext(getServerConfigName());
    }

    /**
     * Test Context
     */
    public void testContext() {
        System.out.println(context);
    }

//    public void testJDBC() {
////        BasicDataSource ds = new BasicDataSource();
//        DataSource ds = new DataSource();
//
//        ds.setDriverClassName("com.mysql.jdbc.Driver");
//        ds.setUrl("jdbc:mysql://localhost:3306/ty?useUnicode=true&characterEncoding=utf8&autoReconnect=true&user=root&password=qazWSXedc&p");
//        ds.setUsername("root");
//        ds.setPassword("qazWSXedc");
//
//        JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
//        jdbcTemplate.execute("select 1");
////        jdbcTemplate.execute("update ty_ds set name='数据源';");
//    }
}
