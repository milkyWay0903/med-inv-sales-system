package com.dayu.system.dao.impl;

import com.dayu.system.dao.BaseDAO;
import com.dayu.system.dao.RowMapper;
import com.dayu.system.entity.Supplier;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

/**
 * 供应商DAO（对应表：supplier）：继承BaseDAO实现专属业务方法
 */
public class SupplierDAO extends BaseDAO<Supplier> {

    // ---------------------- 增删改操作 ----------------------
    /**
     * 添加供应商（适配非空字段：supplier_name、phone）
     */
    public int addSupplier(Supplier supplier) throws SQLException {
        String sql = "INSERT INTO supplier(supplier_name, contact, phone, address, " +
                     "status, create_time, update_time) " +
                     "VALUES(?, ?, ?, ?, ?, ?, ?)";
        Object[] params = {
                supplier.getSupplierName(),
                supplier.getContact(),
                supplier.getPhone(),
                supplier.getAddress(),
                supplier.getStatus() == null ? "正常" : supplier.getStatus(),
                supplier.getCreateTime() == null ? new Timestamp(System.currentTimeMillis()) : supplier.getCreateTime(),
                supplier.getUpdateTime() == null ? new Timestamp(System.currentTimeMillis()) : supplier.getUpdateTime()
        };
        return super.executeUpdate(sql, params);
    }

    /**
     * 根据ID修改供应商状态（如停用/恢复）
     */
    public int updateSupplierStatus(Integer supplierId, String status) throws SQLException {
        String sql = "UPDATE supplier SET status = ?, update_time = CURRENT_TIMESTAMP WHERE supplier_id = ?";
        return super.executeUpdate(sql, status, supplierId);
    }

    /**
     * 根据ID删除供应商（建议逻辑删除，此处为物理删除示例）
     */
    public int deleteSupplier(Integer supplierId) throws SQLException {
        String sql = "DELETE FROM supplier WHERE supplier_id = ?";
        return super.executeUpdate(sql, supplierId);
    }

    // ---------------------- 查询操作 ----------------------
    /**
     * 根据ID查询供应商（单条结果）
     */
    public Supplier getSupplierById(Integer supplierId) throws SQLException {
        String sql = "SELECT * FROM supplier WHERE supplier_id = ?";
        RowMapper<Supplier> rowMapper = new RowMapper<Supplier>() {
            @Override
            public Supplier mapRow(ResultSet rs) throws SQLException {
                Supplier supplier = new Supplier();
                supplier.setSupplierId(rs.getInt("supplier_id"));
                supplier.setSupplierName(rs.getString("supplier_name"));
                supplier.setContact(rs.getString("contact"));
                supplier.setPhone(rs.getString("phone"));
                supplier.setAddress(rs.getString("address"));
                supplier.setStatus(rs.getString("status"));
                supplier.setCreateTime(rs.getTimestamp("create_time"));
                supplier.setUpdateTime(rs.getTimestamp("update_time"));
                return supplier;
            }
        };
        return super.queryOne(sql, rowMapper, supplierId);
    }

    /**
     * 根据名称模糊查询供应商（多条结果，适配idx_supplier_name索引）
     */
    public List<Supplier> getSuppliersByName(String nameKeyword) throws SQLException {
        String sql = "SELECT * FROM supplier WHERE supplier_name LIKE ? ORDER BY supplier_id DESC";
        RowMapper<Supplier> rowMapper = new RowMapper<Supplier>() {
            @Override
            public Supplier mapRow(ResultSet rs) throws SQLException {
                Supplier supplier = new Supplier();
                supplier.setSupplierId(rs.getInt("supplier_id"));
                supplier.setSupplierName(rs.getString("supplier_name"));
                supplier.setContact(rs.getString("contact"));
                supplier.setPhone(rs.getString("phone"));
                supplier.setAddress(rs.getString("address"));
                supplier.setStatus(rs.getString("status"));
                supplier.setCreateTime(rs.getTimestamp("create_time"));
                supplier.setUpdateTime(rs.getTimestamp("update_time"));
                return supplier;
            }
        };
        // 模糊查询拼接%（参数化防止SQL注入）
        return super.queryList(sql, rowMapper, "%" + nameKeyword + "%");
    }

    /**
     * 查询所有供应商（Swing界面刷新功能依赖）
     */
    public List<Supplier> getAllSuppliers() throws SQLException {
        String sql = "SELECT * FROM supplier ORDER BY supplier_id DESC";
        RowMapper<Supplier> rowMapper = new RowMapper<Supplier>() {
            @Override
            public Supplier mapRow(ResultSet rs) throws SQLException {
                Supplier supplier = new Supplier();
                supplier.setSupplierId(rs.getInt("supplier_id"));
                supplier.setSupplierName(rs.getString("supplier_name"));
                supplier.setContact(rs.getString("contact"));
                supplier.setPhone(rs.getString("phone"));
                supplier.setAddress(rs.getString("address"));
                supplier.setStatus(rs.getString("status"));
                supplier.setCreateTime(rs.getTimestamp("create_time"));
                supplier.setUpdateTime(rs.getTimestamp("update_time"));
                return supplier;
            }
        };
        return super.queryList(sql, rowMapper);
    }

    /**
     * 统计正常状态的供应商总数
     */
    public Long countNormalSuppliers() throws SQLException {
        String sql = "SELECT COUNT(*) FROM supplier WHERE status = '正常'";
        return super.queryCount(sql);
    }
}