package com.huaifang.yan.type;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class ListTypeHandler implements TypeHandler<List<String>> {
    @Override
    public void setParameter(PreparedStatement ps, int i, List<String> parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, String.join(",", parameter));
    }

    @Override
    public List<String> getResult(ResultSet rs, String columnName) throws SQLException {
        return Arrays.asList(rs.getString(columnName).split(","));
    }

    @Override
    public List<String> getResult(ResultSet rs, int columnIndex) throws SQLException {
        return Arrays.asList(rs.getString(columnIndex).split(","));
    }

    @Override
    public List<String> getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return Arrays.asList(cs.getObject(columnIndex, String.class).split(","));
    }
}
