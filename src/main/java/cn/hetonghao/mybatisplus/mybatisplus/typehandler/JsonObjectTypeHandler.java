package cn.hetonghao.mybatisplus.mybatisplus.typehandler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.postgresql.util.PGobject;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * json转JSONObject 类型处理
 *
 * @author HeTongHao
 * @since 2019-07-11 17:00
 */
@MappedTypes({JSONObject.class})
public class JsonObjectTypeHandler extends BaseNotNullResultTypeHandler<JSONObject> {
    public JsonObjectTypeHandler() {
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, JSONObject parameter, JdbcType jdbcType) throws SQLException {
        PGobject jsonObject = new PGobject();
        jsonObject.setType("json");
        jsonObject.setValue(parameter.toJSONString());
        ps.setObject(i, jsonObject);
    }

    @Override
    public JSONObject getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String jsonSource = rs.getString(columnName);
        return jsonSource == null ? null : JSON.parseObject(jsonSource);
    }

    @Override
    public JSONObject getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String jsonSource = rs.getString(columnIndex);
        return jsonSource == null ? null : JSON.parseObject(jsonSource);
    }

    @Override
    public JSONObject getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String jsonSource = cs.getString(columnIndex);
        return jsonSource == null ? null : JSON.parseObject(jsonSource);
    }
}
