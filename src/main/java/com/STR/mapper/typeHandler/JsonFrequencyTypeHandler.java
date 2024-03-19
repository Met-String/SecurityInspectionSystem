package com.STR.mapper.typeHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


@MappedTypes(List.class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public class JsonFrequencyTypeHandler extends BaseTypeHandler {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException {
        try {
            ps.setString(i,objectMapper.writeValueAsString(parameter));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error when trying to convert List<Integer> to JSON String.", e);
        }
    }
    @Override
    public List<Integer> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        try {
            return rs.getString(columnName) == null ? null : objectMapper.readValue(rs.getString(columnName), new TypeReference<List<Integer>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Integer> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        try {
            return rs.getString(columnIndex) == null ? null : objectMapper.readValue(rs.getString(columnIndex), new TypeReference<List<Integer>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Integer> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        try {
            return cs.getString(columnIndex) == null ? null : objectMapper.readValue(cs.getString(columnIndex), new TypeReference<List<Integer>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
