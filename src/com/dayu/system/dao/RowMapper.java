package com.dayu.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 结果集映射接口：将ResultSet的一行数据映射为实体类对象
 * @param <T> 实体类泛型
 */
public interface RowMapper<T> {
    T mapRow(ResultSet rs) throws SQLException;
}