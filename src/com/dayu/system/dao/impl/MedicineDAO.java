package com.dayu.system.dao.impl;
import com.dayu.system.dao.BaseDAO;
import com.dayu.system.dao.RowMapper;
import com.dayu.system.entity.Medicine;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * 药品DAO（对应表：medicine）：继承BaseDAO，实现药品相关操作
 */
public class MedicineDAO extends BaseDAO<Medicine> {
    // ---------------------- 1. 增删改操作 ----------------------
    /**
     * 添加药品（适配数据字典中的非空字段：medicine_name）
     */
    public int addMedicine(Medicine medicine) throws SQLException {
        String sql = "INSERT INTO medicine(medicine_name, specification, unit, manufacturer, " +
                     "category, price, status, create_time, update_time) " +
                     "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        // 对应SQL中的?参数（顺序与字段顺序一致）
        Object[] params = {
                medicine.getMedicineName(),
                medicine.getSpecification(),
                medicine.getUnit(),
                medicine.getManufacturer(),
                medicine.getCategory(),
                medicine.getPrice() == null ? new BigDecimal("0.00") : medicine.getPrice(), // 默认0.00
                medicine.getStatus() == null ? "正常" : medicine.getStatus(), // 默认"正常"
                medicine.getCreateTime(),
                medicine.getUpdateTime()
        };
        return super.executeUpdate(sql, params);
    }

    /**
     * 根据ID修改药品状态（如停用药品）
     */
    public int updateMedicineStatus(Integer medicineId, String status) throws SQLException {
        String sql = "UPDATE medicine SET status = ?, update_time = CURRENT_TIMESTAMP WHERE medicine_id = ?";
        return super.executeUpdate(sql, status, medicineId);
    }

    /**
     * 根据ID删除药品（实际业务中建议逻辑删除，此处为物理删除示例）
     */
    public int deleteMedicine(Integer medicineId) throws SQLException {
        String sql = "DELETE FROM medicine WHERE medicine_id = ?";
        return super.executeUpdate(sql, medicineId);
    }

    // ---------------------- 2. 查询操作 ----------------------
    /**
     * 查询所有药品
     */
    public List<Medicine> getAllMedicines() throws SQLException {
        String sql = "SELECT * FROM medicine ORDER BY medicine_id DESC";
        RowMapper<Medicine> rowMapper = new RowMapper<Medicine>() {
            @Override
            public Medicine mapRow(ResultSet rs) throws SQLException {
                Medicine medicine = new Medicine();
                medicine.setMedicineId(rs.getInt("medicine_id"));
                medicine.setMedicineName(rs.getString("medicine_name"));
                medicine.setSpecification(rs.getString("specification"));
                medicine.setUnit(rs.getString("unit"));
                medicine.setManufacturer(rs.getString("manufacturer"));
                medicine.setCategory(rs.getString("category"));
                medicine.setPrice(rs.getBigDecimal("price"));
                medicine.setStatus(rs.getString("status"));
                medicine.setCreateTime(rs.getTimestamp("create_time"));
                medicine.setUpdateTime(rs.getTimestamp("update_time"));
                return medicine;
            }
        };
        return super.queryList(sql, rowMapper);
    }
    
    /**
     * 根据ID查询药品（单条结果）
     */
    public Medicine getMedicineById(Integer medicineId) throws SQLException {
        String sql = "SELECT * FROM medicine WHERE medicine_id = ?";
        // 实现RowMapper：将ResultSet映射为Medicine对象
        RowMapper<Medicine> rowMapper = new RowMapper<Medicine>() {
            @Override
            public Medicine mapRow(ResultSet rs) throws SQLException {
                Medicine medicine = new Medicine();
                medicine.setMedicineId(rs.getInt("medicine_id"));
                medicine.setMedicineName(rs.getString("medicine_name"));
                medicine.setSpecification(rs.getString("specification"));
                medicine.setUnit(rs.getString("unit"));
                medicine.setManufacturer(rs.getString("manufacturer"));
                medicine.setCategory(rs.getString("category"));
                medicine.setPrice(rs.getBigDecimal("price"));
                medicine.setStatus(rs.getString("status"));
                medicine.setCreateTime(rs.getTimestamp("create_time"));
                medicine.setUpdateTime(rs.getTimestamp("update_time"));
                return medicine;
            }
        };
        return super.queryOne(sql, rowMapper, medicineId);
    }

    /**
     * 根据名称查询药品（多条结果，适配数据字典中的idx_medicine_name索引）
     */
    public List<Medicine> getMedicinesByName(String nameKeyword) throws SQLException {
        String sql = "SELECT * FROM medicine WHERE medicine_name LIKE ? ORDER BY medicine_id DESC";
        RowMapper<Medicine> rowMapper = new RowMapper<Medicine>() {
            @Override
            public Medicine mapRow(ResultSet rs) throws SQLException {
                Medicine medicine = new Medicine();
                medicine.setMedicineId(rs.getInt("medicine_id"));
                medicine.setMedicineName(rs.getString("medicine_name"));
                medicine.setSpecification(rs.getString("specification"));
                medicine.setUnit(rs.getString("unit"));
                medicine.setManufacturer(rs.getString("manufacturer"));
                medicine.setCategory(rs.getString("category"));
                medicine.setPrice(rs.getBigDecimal("price"));
                medicine.setStatus(rs.getString("status"));
                medicine.setCreateTime(rs.getTimestamp("create_time"));
                medicine.setUpdateTime(rs.getTimestamp("update_time"));
                return medicine;
            }
        };
        // 模糊查询拼接%，参数化防止SQL注入
        return super.queryList(sql, rowMapper, "%" + nameKeyword + "%");
    }

    /**
     * 根据类别查询药品（多条结果，适配数据字典中的idx_category索引）
     */
    public List<Medicine> getMedicinesByCategory(String category) throws SQLException {
        String sql = "SELECT * FROM medicine WHERE category = ? ORDER BY medicine_id DESC";
        RowMapper<Medicine> rowMapper = new RowMapper<Medicine>() {
            @Override
            public Medicine mapRow(ResultSet rs) throws SQLException {
                Medicine medicine = new Medicine();
                medicine.setMedicineId(rs.getInt("medicine_id"));
                medicine.setMedicineName(rs.getString("medicine_name"));
                medicine.setSpecification(rs.getString("specification"));
                medicine.setUnit(rs.getString("unit"));
                medicine.setManufacturer(rs.getString("manufacturer"));
                medicine.setCategory(rs.getString("category"));
                medicine.setPrice(rs.getBigDecimal("price"));
                medicine.setStatus(rs.getString("status"));
                medicine.setCreateTime(rs.getTimestamp("create_time"));
                medicine.setUpdateTime(rs.getTimestamp("update_time"));
                return medicine;
            }
        };
        return super.queryList(sql, rowMapper, category);
    }

    /**
     * 统计正常状态的药品总数（适配数据字典中的status字段）
     */
    public Long countNormalMedicines() throws SQLException {
        String sql = "SELECT COUNT(*) FROM medicine WHERE status = '正常'";
        return super.queryCount(sql);
    }

}