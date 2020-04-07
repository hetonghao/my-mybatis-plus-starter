package cn.hetonghao.mybatisplus.start;

import cn.hetonghao.mybatisplus.MyMetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import cn.hetonghao.mybatisplus.interceptors.PageHandlerInterceptor;

/**
 * MybatisPlus 配置文件
 *
 * @author HeTongHao
 * @since 2019-07-11 17:00
 */
@Configuration
@EnableTransactionManagement
@MapperScan("**.mapper")
public class MybatisPlusConfig {

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    /**
     * 分页插件优化拦截器
     */
    @Bean
    public PageHandlerInterceptor pageHandlerInterceptor() {
        return new PageHandlerInterceptor();
    }

    /**
     * 乐观锁插件
     * 将module整型字段注解@Version，更新的时候会自动累加，并且更新的时候带了版本，则条件也会带上版本
     */
    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }

    /**
     * 元数据填充处理
     * 如果需要自定义继承该组件重写两个方法即可
     */
    @Bean
    public MyMetaObjectHandler metaObjectHandler() {
        return new MyMetaObjectHandler();
    }
}
