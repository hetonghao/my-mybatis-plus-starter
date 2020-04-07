package cn.hetonghao.mybatisplus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectionException;

import java.time.LocalDateTime;

/**
 * mybatis-plus字段填充
 * entity对象字段修饰以下注解可触发填充
 * //@TableField(fill = FieldFill.INSERT_UPDATE)  触发 insertFill()、updateFill()
 * //@TableField(fill = FieldFill.INSERT) 触发 insertFill()
 *
 * @author HeTongHao
 * @since 2019-07-11 17:00
 */
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        LocalDateTime localDateTime = LocalDateTime.now();
        try {
            this.setFieldValByName("createTime", localDateTime, metaObject);
            this.setFieldValByName("updateTime", localDateTime, metaObject);
        } catch (ReflectionException e) {
            log.debug("用Date做时间不做处理");
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        try {
            this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
        } catch (ReflectionException e) {
            log.debug("用Date做时间不做处理");
        }
    }
}
