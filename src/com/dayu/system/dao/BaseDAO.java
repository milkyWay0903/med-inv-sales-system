package com.dayu.system.dao;

import com.dayu.system.entity.Medicine;
import com.dayu.system.util.JDBCUtils;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 通用BaseDAO：封装所有表的通用CRUD操作
 * @param <T> 实体类泛型（如Medicine、PurchaseOrder等）
 */
public abstract class BaseDAO<T> {
    // 通用增删改操作（返回受影响行数）
    public int executeUpdate(String sql, Object... params) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            // 1. 获取连接
            conn = JDBCUtils.getConnection();
            // 2. 创建PreparedStatement（预编译SQL，防止注入）
            pstmt = conn.prepareStatement(sql);
            // 3. 设置SQL参数（适配?占位符）
            setParams(pstmt, params);
            // 4. 执行SQL（增删改返回受影响行数）
            return pstmt.executeUpdate();
        } finally {
            // 5. 关闭资源（Connection由事务控制时不关闭）
            JDBCUtils.close(null, pstmt, conn);
        }
    }

    // 通用批量增删改（适合批量插入采购明细、销售明细等）
    public int[] executeBatch(String sql, List<Object[]> paramsList) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = JDBCUtils.getConnection();
            pstmt = conn.prepareStatement(sql);
            // 添加批量参数
            for (Object[] params : paramsList) {
                setParams(pstmt, params);
                pstmt.addBatch(); // 加入批处理
            }
            // 执行批量操作（返回每组受影响行数）
            return pstmt.executeBatch();
        } finally {
            JDBCUtils.close(null, pstmt, conn);
        }
    }

    // 通用查询：返回单条实体（如根据ID查药品、查采购单）
    public T queryOne(String sql, RowMapper<T> rowMapper, Object... params) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();
            pstmt = conn.prepareStatement(sql);
            setParams(pstmt, params);
            rs = pstmt.executeQuery(); // 执行查询，返回结果集
            // 映射结果集到实体类（有数据则映射，无则返回null）
            if (rs.next()) {
                return rowMapper.mapRow(rs);
            }
            return null;
        } finally {
            JDBCUtils.close(rs, pstmt, conn);
        }
    }

    // 通用查询：返回多条实体（如查询所有药品、查询某采购单的所有明细）
    public List<T> queryList(String sql, RowMapper<T> rowMapper, Object... params) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<T> list = new ArrayList<>(); // 存储结果集
        try {
            conn = JDBCUtils.getConnection();
            pstmt = conn.prepareStatement(sql);
            setParams(pstmt, params);
            rs = pstmt.executeQuery();
            // 遍历结果集，映射为实体类并加入列表
            while (rs.next()) {
                list.add(rowMapper.mapRow(rs));
            }
            return list;
        } finally {
            JDBCUtils.close(rs, pstmt, conn);
        }
    }

    // 通用查询：返回统计结果（如统计药品总数、某时间段销售总额）
    public Long queryCount(String sql, Object... params) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();
            pstmt = conn.prepareStatement(sql);
            setParams(pstmt, params);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getLong(1); // 返回第一列的统计值（count(*)）
            }
            return 0L; // 无数据时返回0
        } finally {
            JDBCUtils.close(rs, pstmt, conn);
        }
    }

    // ---------------------- 私有辅助方法 ----------------------
    /**
     * 给PreparedStatement设置参数（适配不同类型的参数）
     * @param pstmt PreparedStatement对象
     * @param params 参数数组（与SQL中的?顺序对应）
     */
    private void setParams(PreparedStatement pstmt, Object... params) throws SQLException {
        if (params == null || params.length == 0) {
            return; // 无参数时直接返回
        }
        // 遍历参数数组，按顺序设置到PreparedStatement
        for (int i = 0; i < params.length; i++) {
            Object param = params[i];
            int index = i + 1; // SQL参数索引从1开始
            // 根据参数类型设置（覆盖数据字典中的所有字段类型）
            if (param instanceof Integer) {
                pstmt.setInt(index, (Integer) param);
            } else if (param instanceof String) {
                pstmt.setString(index, (String) param);
            } else if (param instanceof BigDecimal) {
                pstmt.setBigDecimal(index, (BigDecimal) param);
            } else if (param instanceof Date) {
                pstmt.setDate(index, (Date) param);
            } else if (param instanceof Timestamp) {
                pstmt.setTimestamp(index, (Timestamp) param);
            } else if (param == null) {
                pstmt.setNull(index, Types.NULL); // 处理null值
            }
        }
    }

}
