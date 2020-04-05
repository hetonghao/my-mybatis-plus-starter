package cn.hetonghao.mybatisplus.mybatisplus.typehandler;

import org.apache.ibatis.executor.result.ResultMapException;
import org.apache.ibatis.type.BaseTypeHandler;

import java.sql.CallableStatement;
import java.sql.ResultSet;

/**
 * 非空字段 类型处理
 *
 * @author HeTongHao
 * @since 2019-07-11 17:00
 */
public abstract class BaseNotNullResultTypeHandler<T> extends BaseTypeHandler<T> {
    public BaseNotNullResultTypeHandler() {
    }

    @Override
    public T getResult(ResultSet rs, String columnName) {
        try {
            return this.getNullableResult(rs, columnName);
        } catch (Exception var4) {
            throw new ResultMapException("Error attempting to get column '" + columnName + "' from result set.  Cause: " + var4, var4);
        }
    }

    @Override
    public T getResult(ResultSet rs, int columnIndex) {
        try {
            return this.getNullableResult(rs, columnIndex);
        } catch (Exception var4) {
            throw new ResultMapException("Error attempting to get column #" + columnIndex + " from result set.  Cause: " + var4, var4);
        }
    }

    @Override
    public T getResult(CallableStatement cs, int columnIndex) {
        try {
            return this.getNullableResult(cs, columnIndex);
        } catch (Exception var4) {
            throw new ResultMapException("Error attempting to get column #" + columnIndex + " from callable statement.  Cause: " + var4, var4);
        }
    }
}
