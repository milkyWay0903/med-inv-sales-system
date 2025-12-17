package com.dayu.system.dao.impl;

import com.dayu.system.dao.BaseDAO;
import com.dayu.system.dao.RowMapper;
import com.dayu.system.entity.SalesDetail;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SalesDetailDAO extends BaseDAO<SalesDetail> {
    // 新增销售明细
    public int addSalesDetail(SalesDetail sd) throws SQLException {
        String sql = "INSERT INTO sales_detail(sales_id, medicine_id, quantity, unit_price, remark) VALUES(?, ?, ?, ?, ?)";
        return executeUpdate(sql,
                sd.getSalesId(),
                sd.getMedicineId(),
                sd.getQuantity(),
                sd.getUnitPrice(),
                sd.getRemark());
    }

    // 根据销售单ID查询明细
    public List<SalesDetail> getDetailsBySalesId(Integer salesId) throws SQLException {
        String sql = "SELECT * FROM sales_detail WHERE sales_id = ?";
        return queryList(sql, new RowMapper<SalesDetail>() {
            @Override
            public SalesDetail mapRow(ResultSet rs) throws SQLException {
                SalesDetail sd = new SalesDetail();
                sd.setDetailId(rs.getInt("sales_detail_id"));
                sd.setSalesId(rs.getInt("sales_id"));
                sd.setMedicineId(rs.getInt("medicine_id"));
                sd.setQuantity(rs.getInt("quantity"));
                sd.setUnitPrice(rs.getBigDecimal("unit_price"));
                sd.setAmount(rs.getBigDecimal("amount"));
                sd.setRemark(rs.getString("remark"));
                return sd;
            }
        }, salesId);
    }
    
    // 根据ID查询销售明细
    public SalesDetail getSalesDetailById(Integer id) throws SQLException {
        String sql = "SELECT * FROM sales_detail WHERE sales_detail_id = ?";
        return queryOne(sql, new RowMapper<SalesDetail>() {
            @Override
            public SalesDetail mapRow(ResultSet rs) throws SQLException {
                SalesDetail sd = new SalesDetail();
                sd.setDetailId(rs.getInt("sales_detail_id"));
                sd.setSalesId(rs.getInt("sales_id"));
                sd.setMedicineId(rs.getInt("medicine_id"));
                sd.setQuantity(rs.getInt("quantity"));
                sd.setUnitPrice(rs.getBigDecimal("unit_price"));
                sd.setAmount(rs.getBigDecimal("amount"));
                sd.setRemark(rs.getString("remark"));
                return sd;
            }
        }, id);
    }
    
    // 查询所有销售明细
    public List<SalesDetail> getAllSalesDetails() throws SQLException {
        String sql = "SELECT * FROM sales_detail ORDER BY sales_detail_id DESC";
        return queryList(sql, new RowMapper<SalesDetail>() {
            @Override
            public SalesDetail mapRow(ResultSet rs) throws SQLException {
                SalesDetail sd = new SalesDetail();
                sd.setDetailId(rs.getInt("sales_detail_id"));
                sd.setSalesId(rs.getInt("sales_id"));
                sd.setMedicineId(rs.getInt("medicine_id"));
                sd.setQuantity(rs.getInt("quantity"));
                sd.setUnitPrice(rs.getBigDecimal("unit_price"));
                sd.setAmount(rs.getBigDecimal("amount"));
                sd.setRemark(rs.getString("remark"));
                return sd;
            }
        });
    }
    
    // 修改销售明细
    public int updateSalesDetail(SalesDetail sd) throws SQLException {
        String sql = "UPDATE sales_detail SET sales_id=?, medicine_id=?, quantity=?, unit_price=?, remark=? WHERE sales_detail_id=?";
        return executeUpdate(sql,
                sd.getSalesId(),
                sd.getMedicineId(),
                sd.getQuantity(),
                sd.getUnitPrice(),
                sd.getRemark(),
                sd.getDetailId());
    }
    
    // 删除销售明细
    public int deleteSalesDetail(Integer id) throws SQLException {
        String sql = "DELETE FROM sales_detail WHERE sales_detail_id=?";
        return executeUpdate(sql, id);
    }
}