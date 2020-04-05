package cn.hetonghao.mybatisplus.utils;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.TypeReference;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.lang.reflect.Type;
import java.util.Set;

/**
 * Redis操作帮助类
 *
 * @author HeTongHao
 * @since 2019-07-22 17:00
 */
public class RedisUtils {
    /**
     * 连接池
     */
    private JedisPool pool;

    /**
     * 每个服务的独立标识，以免多个服务之间key重复
     * 会加在key的前面
     */
    private String SYS_CACHE;

    public RedisUtils(String prefix, JedisPool pool) {
        SYS_CACHE = prefix;
        this.pool = pool;
    }

    public RedisUtils(JedisPool pool) {
        SYS_CACHE = "";
        this.pool = pool;
    }

    public RedisUtils(String prefix, String host, int port) {
        SYS_CACHE = prefix;
        this.pool = new JedisPool(host, port);
    }

    public Jedis getJedis() {
        return pool.getResource();
    }

    public void returnResource(Jedis jedis) {
        jedis.close();
    }

    /**
     * 得到最终key名称
     *
     * @param key
     * @return
     */
    public String makeKey(String key) {
        if (StringUtils.isNotBlank(SYS_CACHE)) {
            return new StringBuilder().append(SYS_CACHE).append(":").append(key).toString();
        }
        return key;
    }

    /**
     * get
     *
     * @param key
     * @return
     */
    public String get(String key) {
        Jedis jedis = pool.getResource();
        try {
            return jedis.get(makeKey(key));
        } finally {
            jedis.close();
        }
    }

    /**
     * get
     *
     * @param key
     * @param type
     * @return
     */
    public <T> T get(String key, TypeReference<T> type) {
        String str = get(key);
        if (StringUtils.isBlank(str)) {
            return null;
        }
        return JSON.parseObject(str, (Type) type);
    }

    /**
     * set object
     *
     * @param key
     * @param value
     */
    public void set(String key, Object value) {
        set(key, JSON.toJSONString(value));
    }

    /**
     * set string
     *
     * @param key
     * @param value
     */
    public void set(String key, String value) {
        Jedis jedis = pool.getResource();
        try {
            jedis.set(makeKey(key), value);
        } finally {
            jedis.close();
        }
    }

    /**
     * set object 并设置超时
     *
     * @param key
     * @param value
     * @param expire
     */
    public void set(String key, Object value, int expire) {
        set(key, JSON.toJSONString(value), expire);
    }

    /**
     * set string 并设置超时
     *
     * @param key
     * @param value
     * @param expire
     */
    public void set(String key, String value, int expire) {
        Jedis jedis = pool.getResource();
        try {
            jedis.setex(makeKey(key), expire, value);
        } finally {
            jedis.close();
        }
    }

    /**
     * 判断 key 是否存在
     *
     * @param key
     * @return
     */
    public boolean exists(String key) {
        Jedis jedis = pool.getResource();
        try {
            return jedis.exists(makeKey(key));
        } finally {
            jedis.close();
        }
    }


    /**
     * 设置超时时间
     *
     * @param key
     * @param expire
     */
    public void expire(String key, int expire) {
        Jedis jedis = pool.getResource();
        try {
            if (expire != 0) {
                jedis.expire(makeKey(key), expire);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            jedis.close();
        }
    }

    /**
     * 删除
     *
     * @param key
     * @return
     */
    public long del(String key) {
        Jedis jedis = pool.getResource();
        try {
            return jedis.del(makeKey(key));
        } finally {
            jedis.close();
        }
    }

    /**
     * keys
     *
     * @param pattern 表达式
     * @return key列表
     */
    public Set<String> keys(String pattern) {
        Jedis jedis = pool.getResource();
        try {
            return jedis.keys(makeKey(pattern));
        } finally {
            jedis.close();
        }
    }

    /**
     * ttl
     *
     * @param key key
     * @return 缓存生命秒数
     */
    public Long ttl(String key) {
        Jedis jedis = pool.getResource();
        try {
            return jedis.ttl(makeKey(key));
        } finally {
            jedis.close();
        }
    }
}
