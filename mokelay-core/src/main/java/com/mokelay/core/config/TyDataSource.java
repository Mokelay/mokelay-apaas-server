package com.mokelay.core.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.stereotype.Component;

/**
 * TyDataSource
 * TY 默认主配置库  可以通过 ty.datasource.key=innerTYDatasource 来设置，或者直接在sessionfactory中引入
 * @author xiaobc
 * @date 18/5/2
 */
@Component
public class TyDataSource implements BeanDefinitionRegistryPostProcessor
{
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        GenericBeanDefinition definition = new GenericBeanDefinition();
        definition.setBeanClass(org.apache.tomcat.jdbc.pool.DataSource.class);    //设置类
        definition.setScope("singleton");       //设置scope
        definition.setLazyInit(false);          //设置是否懒加载
        definition.setAutowireCandidate(true);  //设置是否可以被其他对象自动注入
        MutablePropertyValues values = new MutablePropertyValues();

        values.addPropertyValue("driverClassName","com.mysql.jdbc.Driver");
        values.addPropertyValue("url","jdbc:mysql://59294ed0a3aba.sh.cdb.myqcloud.com:16992/db_ty?useUnicode=true&amp;characterEncoding=utf8");
        values.addPropertyValue("username","root");
        values.addPropertyValue("password","9qkslboGwLWllRsE");

        values.addPropertyValue("maxActive","100");
        values.addPropertyValue("initialSize","10");
        values.addPropertyValue("minIdle","10");
        values.addPropertyValue("jdbcInterceptors","ConnectionState;StatementFinalizer;StatementDecoratorInterceptor;ResetAbandonedTimer;SlowQueryReport(threshold=100);SlowQueryReportJmx(notifyPool=false)");
        values.addPropertyValue("testWhileIdle","true");
        values.addPropertyValue("testOnBorrow","true");
        values.addPropertyValue("validationQuery","select 1");
        values.addPropertyValue("testOnReturn","false");
        values.addPropertyValue("validationInterval","30000");
        values.addPropertyValue("timeBetweenEvictionRunsMillis","5000");
        values.addPropertyValue("maxWait","15000");
        values.addPropertyValue("removeAbandoned","true");
        values.addPropertyValue("removeAbandonedTimeout","60");
        values.addPropertyValue("logAbandoned","true");
        values.addPropertyValue("minEvictableIdleTimeMillis","30000");
        values.addPropertyValue("jmxEnabled","true");
        values.addPropertyValue("name","jdbcPool");

        definition.setPropertyValues(values);
        beanDefinitionRegistry.registerBeanDefinition("innerTYDatasource", definition);
        System.out.println("inner ty dataSource bean init finish。。。。。。。。。。");
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }
}
