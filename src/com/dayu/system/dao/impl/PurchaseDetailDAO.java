package com.dayu.system.dao.impl;

import com.dayu.system.dao.BaseDAO;
import com.dayu.system.dao.RowMapper;
import com.dayu.system.entity.PurchaseDetail;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PurchaseDetailDAO extends BaseDAO<PurchaseDetail> {
    // 新增采购明细
    public int addPurchaseDetail(PurchaseDetail pd) throws SQLException {
        String sql = "INSERT INTO purchase_detail(purchase_id, medicine_id, quantity, unit_price, remark) VALUES(?, ?, ?, ?, ?)";
        return executeUpdate(sql,
                pd.getPurchaseId(),
                pd.getMedicineId(),
                pd.getQuantity(),
                pd.getUnitPrice(),
                pd.getRemark());
    }

    // 根据采购单ID查询明细
    public List<PurchaseDetail> getDetailsByPurchaseId(Integer purchaseId) throws SQLException {
        String sql = "SELECT * FROM purchase_detail WHERE purchase_id = ?";
        return queryList(sql, new RowMapper<PurchaseDetail>() {
            @Override
            public PurchaseDetail mapRow(ResultSet rs) throws SQLException {
                PurchaseDetail pd = new PurchaseDetail();
                pd.setPurchaseDetailId(rs.getInt("purchase_detail_id"));
                pd.setPurchaseId(rs.getInt("purchase_id"));
                pd.setMedicineId(rs.getInt("medicine_id"));
                pd.setQuantity(rs.getInt("quantity"));
                pd.setUnitPrice(rs.getBigDecimal("unit_price"));
                pd.setAmount(rs.getBigDecimal("amount"));
                pd.setRemark(rs.getString("remark"));
                return pd;
            }
        }, purchaseId);
    }
    
    // 根据ID查询采购明细
    public PurchaseDetail getPurchaseDetailById(Integer id) throws SQLException {
        String sql = "SELECT * FROM purchase_detail WHERE purchase_detail_id = ?";
        return queryOne(sql, new RowMapper<PurchaseDetail>() {
            @Override
            public PurchaseDetail mapRow(ResultSet rs) throws SQLException {
                PurchaseDetail pd = new PurchaseDetail();
                pd.setPurchaseDetailId(rs.getInt("purchase_detail_id"));
                pd.setPurchaseId(rs.getInt("purchase_id"));
                pd.setMedicineId(rs.getInt("medicine_id"));
                pd.setQuantity(rs.getInt("quantity"));
                pd.setUnitPrice(rs.getBigDecimal("unit_price"));
                pd.setAmount(rs.getBigDecimal("amount"));
                pd.setRemark(rs.getString("remark"));
                return pd;
            }
        }, id);
    }
    
    // 查询所有采购明细
    public List<PurchaseDetail> getAllPurchaseDetails() throws SQLException {
        String sql = "SELECT * FROM purchase_detail ORDER BY purchase_detail_id DESC";
        return queryList(sql, new RowMapper<PurchaseDetail>() {
            @Override
            public PurchaseDetail mapRow(ResultSet rs) throws SQLException {
                PurchaseDetail pd = new PurchaseDetail();
                pd.setPurchaseDetailId(rs.getInt("purchase_detail_id"));
                pd.setPurchaseId(rs.getInt("purchase_id"));
                pd.setMedicineId(rs.getInt("medicine_id"));
                pd.setQuantity(rs.getInt("quantity"));
                pd.setUnitPrice(rs.getBigDecimal("unit_price"));
                pd.setAmount(rs.getBigDecimal("amount"));
                pd.setRemark(rs.getString("remark"));
                return pd;
            }
        });
    }
    
    // 修改采购明细
    public int updatePurchaseDetail(PurchaseDetail pd) throws SQLException {
        String sql = "UPDATE purchase_detail SET purchase_id=?, medicine_id=?, quantity=?, unit_price=?, remark=? WHERE purchase_detail_id=?";
        return executeUpdate(sql,
                pd.getPurchaseId(),
                pd.getMedicineId(),
                pd.getQuantity(),
                pd.getUnitPrice(),
                pd.getRemark(),
                pd.getPurchaseDetailId());
    }
    
    // 删除采购明细
    public int deletePurchaseDetail(Integer id) throws SQLException {
        String sql = "DELETE FROM purchase_detail WHERE purchase_detail_id=?";
        return executeUpdate(sql, id);
    }
}