package com.greatbee.core.config;

import com.greatbee.base.util.DataUtil;
import com.greatbee.db.util.RedisBuildUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.io.File;
import java.util.*;

/**
 * RedisBeanFactoryAwareBean
 *  动态创建redis文件夹下的 所有redis缓存配置
 * @author xiaobc
 * @date 17/9/26
 */
public class RedisBeanFactoryAwareBean implements BeanFactoryAware,
        ApplicationListener<ContextRefreshedEvent> {
    private DefaultListableBeanFactory beanFactory;

    private static final String CACHE_REDIS_POOL_MAXTOTAL = "cache.redis.pool.maxTotal";//最大连接数 默认60
    private static final String CACHE_REDIS_POOL_MAXIDLE = "cache.redis.pool.maxIdle";//最大空闲数，默认40
    private static final String CACHE_REDIS_POOL_MINIDLE = "cache.redis.pool.minIdle";//最小空闲数，默认20


    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println("setBeanFactory.........................");
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }

    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        System.out.println("ContextRefreshed...................");

        File[] files = RedisBuildUtil.getRedisConfigFiles();
        if(files==null || files.length<=0){
            return;
        }

        //注册jedis pool
        BeanDefinitionBuilder jedisPool = BeanDefinitionBuilder
                .genericBeanDefinition(JedisPoolConfig.class);
        jedisPool.addPropertyValue("maxTotal", "1000");
        jedisPool.addPropertyValue("maxIdle", "40");
        jedisPool.addPropertyValue("minIdle", "20");
        beanFactory.registerBeanDefinition("jedisPool",
                jedisPool.getRawBeanDefinition());

        //定义redisMap的参数
        Map mapBean = new HashMap<>();

        for(int i=0;i<files.length;i++){
            String alias = files[i].getName().split("\\.")[0];//redis缓存别名
            Properties pops = RedisBuildUtil.filterRedis(RedisBuildUtil.REDIS_CONFIG_PATH + files[i].getName());
            if(!pops.containsKey(RedisBuildUtil.REDIS_URL)){
                continue;
            }
            String redisUrl = pops.getProperty(RedisBuildUtil.REDIS_URL);
            int dataBase = DataUtil.getInt(pops.getProperty(RedisBuildUtil.REDIS_DATABASE),-1);//几号库
            String[] redisUrlArray = redisUrl.split(",");//eg:xxx.com:26380,xxxx2.com:26380,xxxx3.com:26380
            if(redisUrlArray==null||redisUrlArray.length<=0){
                continue;
            }

            //主
            BeanDefinitionBuilder cacheRedisSentinelConfiguration = BeanDefinitionBuilder
                    .genericBeanDefinition(RedisSentinelConfiguration.class);

            RedisNode.RedisNodeBuilder rn = new RedisNode.RedisNodeBuilder();
            rn.withName("mymaster");
            cacheRedisSentinelConfiguration.addPropertyValue("master",rn.build());

            //从
            Set<RedisNode> set = new HashSet<>();
            for(int j=0;j<redisUrlArray.length;j++){
                String[] redisUrlConfig = redisUrlArray[j].split(":");
                String url = redisUrlConfig[0];
                int port = DataUtil.getInt(redisUrlConfig[1],-1);
                if(port==-1){
                    continue;
                }
                RedisNode item = new RedisNode(url,port);
                set.add(item);
            }
            cacheRedisSentinelConfiguration.addPropertyValue("sentinels",set);
            //注册
            beanFactory.registerBeanDefinition(alias+RedisBuildUtil.RedisConfig,
                    cacheRedisSentinelConfiguration.getRawBeanDefinition());

            //cacheJedisConnectionFactory 配置
            BeanDefinitionBuilder cacheJedisConnectionFactory = BeanDefinitionBuilder
                    .genericBeanDefinition(JedisConnectionFactory.class);
            cacheJedisConnectionFactory.addConstructorArgReference(alias + RedisBuildUtil.RedisConfig);
            cacheJedisConnectionFactory.addPropertyValue("usePool", true);
            if(dataBase>=0){
                cacheJedisConnectionFactory.addPropertyValue("database",dataBase);
            }
            cacheJedisConnectionFactory.addPropertyReference("poolConfig", "jedisPool");//配置连接池属性
            //注册
            beanFactory.registerBeanDefinition(alias + RedisBuildUtil.RedisConnFactory,
                    cacheJedisConnectionFactory.getRawBeanDefinition());

            //cacheRedisTemplate 配置
            BeanDefinitionBuilder redisTemplate = BeanDefinitionBuilder
                    .genericBeanDefinition(RedisTemplate.class);
            redisTemplate.addPropertyReference("connectionFactory", alias + RedisBuildUtil.RedisConnFactory);
            redisTemplate.addPropertyValue("enableDefaultSerializer", true);
            redisTemplate.addPropertyValue("keySerializer",new StringRedisSerializer());
            redisTemplate.addPropertyValue("hashKeySerializer", new StringRedisSerializer());
            //注册
            beanFactory.registerBeanDefinition(alias + RedisBuildUtil.RedisTemplate,
                    redisTemplate.getRawBeanDefinition());

            //将所有的template bean 放到hashMap中
//            redisMap.addPropertyReference(alias, alias + RedisBuildUtil.RedisTemplate);
            mapBean.put(alias, beanFactory.getBean(alias + RedisBuildUtil.RedisTemplate));
        }

        BeanDefinitionBuilder redisMap = BeanDefinitionBuilder
                .genericBeanDefinition(HashMap.class);
        redisMap.addConstructorArgValue(mapBean);
        //注册 所有的template 放到一个redisMap中
        beanFactory.registerBeanDefinition("redisMap",
                redisMap.getRawBeanDefinition());

        System.out.println("redis bean init finish。。。。。。。。。。");

        Map map = (Map) beanFactory.getBean("redisMap");
        System.out.println("map seze="+map.size());

    }
}
