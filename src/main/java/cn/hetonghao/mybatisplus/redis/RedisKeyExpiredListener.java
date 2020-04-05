package cn.hetonghao.mybatisplus.redis;

import cn.hetonghao.mybatisplus.cache.redis.RedisKeyExpiredHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPubSub;

/**
 * RedisKey 过期监听
 *
 * @author HeTongHao
 * @since 2019-09-09 16:12
 */
@Slf4j
@Data
@Configuration
@EqualsAndHashCode(callSuper = true)
public class RedisKeyExpiredListener extends JedisPubSub {
    private RedisKeyExpiredHandler redisKeyExpiredHandler;

    @Override
    public void onPSubscribe(String pattern, int subscribedChannels) {
        log.info("onPSubscribe:{}", pattern);
    }

    @Override
    public void onPMessage(String pattern, String channel, String message) {
        log.debug("onPMessage-pattern:{},channel:{},message:{}", pattern, channel, message);
        if (redisKeyExpiredHandler != null) {
            redisKeyExpiredHandler.expiredHandler(message);
        }
    }
}
