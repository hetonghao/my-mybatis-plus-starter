package cn.hetonghao.mybatisplus.redis;

import cn.hetonghao.mybatisplus.utils.RedisUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Redis 配置
 *
 * @author HeTongHao
 * @since 2019-08-06 20:00
 */
@Configuration
@EnableConfigurationProperties({RedisProperties.class,})
public class RedisConfig {
    @Autowired
    private RedisProperties redisProperties;
    @Autowired
    private RedisKeyExpiredListener redisKeyExpiredListener;

    private static final ExecutorService executorService =
            new ThreadPoolExecutor(1, 1, 100L, TimeUnit.SECONDS
                    , new ArrayBlockingQueue<>(1), r -> new Thread("redisKeyExpiredListener"));

    @Bean
    public JedisPool jedisPool() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(20);
        poolConfig.setMinIdle(2);
        poolConfig.setMaxIdle(10);
        JedisPool pool;
        if (StringUtils.isBlank(redisProperties.getPassword())) {
            pool = new JedisPool(poolConfig, redisProperties.getHost(), redisProperties.getPort()
                    , redisProperties.getTimeout());
        } else {
            pool = new JedisPool(poolConfig, redisProperties.getHost(), redisProperties.getPort()
                    , redisProperties.getTimeout(), redisProperties.getPassword(), redisProperties.getDatabase());
        }
        //redis key超时监听
        executorService.execute(() -> pool.getResource().psubscribe(redisKeyExpiredListener, "__key*__:expired"));
        return pool;
    }

    @Bean
    public RedisUtils redisUtils(@Autowired JedisPool jedisPool) {
        return new RedisUtils(jedisPool);
    }
}
