package cn.hetonghao.mybatisplus.typehandler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.postgresql.util.PGobject;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * json转JSONArray 类型处理
 *
 * @author HeTongHao
 * @since 2019-07-11 17:00
 */
@MappedTypes({JSONArray.class})
public class JsonArrayTypeHandler extends BaseNotNullResultTypeHandler<JSONArray> {
    public JsonArrayTypeHandler() {
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, JSONArray parameter, JdbcType jdbcType) throws SQLException {
        PGobject jsonObject = new PGobject();
        jsonObject.setType("json");
        jsonObject.setValue(parameter.toJSONString());
        ps.setObject(i, jsonObject);
    }

    @Override
    public JSONArray getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String jsonSource = rs.getString(columnName);
        return jsonSource == null ? null : JSON.parseArray(jsonSource);
    }

    @Override
    public JSONArray getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String jsonSource = rs.getString(columnIndex);
        return jsonSource == null ? null : JSON.parseArray(jsonSource);
    }

    @Override
    public JSONArray getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String jsonSource = cs.getString(columnIndex);
        return jsonSource == null ? null : JSON.parseArray(jsonSource);
    }
}
