package cn.hetonghao.mybatisplus.mybatisplus.interceptors;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import cn.hetonghao.mybatisplus.utils.RedisUtils;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 分页处理拦截
 * 实现:
 * 1、返回List时，将返回的List动态set到page中的records中
 * 2、查询total缓存处理
 *
 * @author HeTongHao
 * @since 2019-07-12 14:51
 */
@Intercepts({@Signature(
        type = Executor.class,
        method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}
)})
@Slf4j
@AllArgsConstructor
public class PageHandlerInterceptor implements Interceptor {
    private RedisUtils redisUtils;

    private IPage getPage(Object params) {
        if (params instanceof IPage) {
            return (IPage) params;
        } else if (params instanceof Map) {
            Map<String, Object> pageParamMap = (Map) params;
            for (Object param : pageParamMap.values()) {
                if (param instanceof IPage) {
                    return (IPage) param;
                }
            }
        }
        return null;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        Object params = args[1];
        IPage page = getPage(params);
        Object result = invocation.proceed();
        if (page != null) {
            page.setRecords((List) result);
            //缓存总数处理
            caCheTotalHandle(args, params, page);
        }
        return result;
    }

    /**
     * 获得查询条件列表
     *
     * @param params
     * @return
     */
    private List<Object> queryConditions(Object params) {
        Set<Object> paramsSet = Sets.newHashSet();
        if (params instanceof Map) {
            Map<String, Object> pageParamMap = (Map) params;
            paramsSet.addAll(pageParamMap.values().stream()
                    .filter(param -> !(param instanceof IPage)).collect(Collectors.toList()));
        }
        return Lists.newArrayList(paramsSet);
    }

    /**
     * 缓存总数处理
     *
     * @param args
     * @param params
     * @param page
     */
    private void caCheTotalHandle(Object[] args, Object params, IPage page) {
        //缓存total，解决mybatis引入缓存后total为0的问题
        String pageTotalKey;
        try {
            pageTotalKey = "page-total-" + ((MappedStatement) args[0]).getId() + JSON.toJSONString(queryConditions(params));
        } catch (ClassCastException e) {
            log.error("分页total缓存处理异常，请优化代码");
            return;
        }
        if (!page.getRecords().isEmpty() && page.getTotal() == 0 && redisUtils.exists(pageTotalKey)) {
            page.setTotal(Integer.parseInt(redisUtils.get(pageTotalKey)));
        } else {
            redisUtils.set(pageTotalKey, page.getTotal());
        }
    }

    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }
}
