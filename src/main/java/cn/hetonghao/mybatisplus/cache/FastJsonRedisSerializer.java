package cn.hetonghao.mybatisplus.cache;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.util.IOUtils;

/**
 * FastJsonRedisSerializer
 *
 * @author lee
 * @since Aug 13, 2019 4:35:54 PM
 */
public class FastJsonRedisSerializer implements Serializer {

    private final static ParserConfig defaultRedisConfig = new ParserConfig();

    static {
        defaultRedisConfig.setAutoTypeSupport(true);
    }

    @Override
    public byte[] serialize(Object object) {
        if (object == null) {
            return new byte[0];
        }
        return JSON.toJSONBytes(object, SerializerFeature.WriteClassName);
    }

    @Override
    public Object unserialize(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        return JSON.parseObject(new String(bytes, IOUtils.UTF8), Object.class, defaultRedisConfig);
    }
}