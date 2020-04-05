package cn.hetonghao.mybatisplus.cache;

import org.apache.ibatis.cache.Cache;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * mybatis二级缓存
 *
 * @author lee
 * @since Aug 13, 2019 4:34:31 PM
 */
public final class SpringRedisCache implements Cache {

    private final ReadWriteLock readWriteLock = new DummyReadWriteLock();

    private String id;

    private JedisPool pool;

    private Serializer serializer = new FastJsonRedisSerializer();

    private Integer timeout;

    public SpringRedisCache(final String id) {
        if (id == null) {
            throw new IllegalArgumentException("Cache instances require an ID");
        }
        this.id = id;
    }

    //  Review this is UNUSED
    private Object execute(RedisCallback callback) {
        if (pool == null) {
            pool = SpringContextHolder.getBean("jedisPool");
        }
        Jedis jedis = pool.getResource();
        try {
            return callback.doWithRedis(jedis);
        } finally {
            jedis.close();
        }
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public int getSize() {
        return (Integer) execute(new RedisCallback() {
            @Override
            public Object doWithRedis(Jedis jedis) {
                Map<byte[], byte[]> result = jedis.hgetAll(id.getBytes());
                return result.size();
            }
        });
    }

    @Override
    public void putObject(final Object key, final Object value) {
        execute(new RedisCallback() {
            @Override
            public Object doWithRedis(Jedis jedis) {
                final byte[] idBytes = id.getBytes();
                jedis.hset(idBytes, key.toString().getBytes(), serializer.serialize(value));
                if (timeout != null && jedis.ttl(idBytes) == -1) {
                    jedis.expire(idBytes, timeout);
                }
                return null;
            }
        });
    }

    @Override
    public Object getObject(final Object key) {
        return execute(new RedisCallback() {
            @Override
            public Object doWithRedis(Jedis jedis) {
                return serializer.unserialize(jedis.hget(id.getBytes(), key.toString().getBytes()));
            }
        });
    }

    @Override
    public Object removeObject(final Object key) {
        return execute(new RedisCallback() {
            @Override
            public Object doWithRedis(Jedis jedis) {
                return jedis.hdel(id, key.toString());
            }
        });
    }

    @Override
    public void clear() {
        execute(new RedisCallback() {
            @Override
            public Object doWithRedis(Jedis jedis) {
                jedis.del(id);
                return null;
            }
        });

    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return readWriteLock;
    }

    @Override
    public String toString() {
        return "Redis {" + id + "}";
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

}
