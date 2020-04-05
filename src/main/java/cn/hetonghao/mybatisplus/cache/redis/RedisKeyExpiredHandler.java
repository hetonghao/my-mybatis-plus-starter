package cn.hetonghao.mybatisplus.cache.redis;

/**
 * RedisKey 过期处理
 *
 * @author HeTongHao
 * @since 2019-09-09 18:20
 */
public interface RedisKeyExpiredHandler {
    /**
     * 过期处理
     *
     * @param key 过期的key
     */
    void expiredHandler(String key);
}