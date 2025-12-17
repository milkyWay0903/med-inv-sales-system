package com.dayu.system.dao.impl;

import com.dayu.system.dao.BaseDAO;
import com.dayu.system.dao.RowMapper;
import com.dayu.system.entity.PurchaseOrder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PurchaseOrderDAO extends BaseDAO<PurchaseOrder> {
    // 新增采购单
    public int addPurchaseOrder(PurchaseOrder po) throws SQLException {
        String sql = "INSERT INTO purchase_order(supplier_id, purchase_date, total_amount, status, create_time) VALUES(?, ?, ?, ?, ?)";
        return executeUpdate(sql,
                po.getSupplierId(),
                po.getPurchaseDate(),
                po.getTotalAmount(),
                po.getStatus() == null ? "待入库" : po.getStatus(),
                po.getCreateTime());
    }

    // 根据ID查询采购单
    public PurchaseOrder getPurchaseOrderById(Integer id) throws SQLException {
        String sql = "SELECT * FROM purchase_order WHERE purchase_id = ?";
        return queryOne(sql, new RowMapper<PurchaseOrder>() {
            @Override
            public PurchaseOrder mapRow(ResultSet rs) throws SQLException {
                PurchaseOrder po = new PurchaseOrder();
                po.setPurchaseId(rs.getInt("purchase_id"));
                po.setSupplierId(rs.getInt("supplier_id"));
                po.setPurchaseDate(rs.getDate("purchase_date"));
                po.setTotalAmount(rs.getBigDecimal("total_amount"));
                po.setStatus(rs.getString("status"));
                po.setCreateTime(rs.getTimestamp("create_time"));
                return po;
            }
        }, id);
    }

    // 查询所有采购单
    public List<PurchaseOrder> getAllPurchaseOrders() throws SQLException {
        String sql = "SELECT * FROM purchase_order ORDER BY purchase_id DESC";
        return queryList(sql, new RowMapper<PurchaseOrder>() {
            @Override
            public PurchaseOrder mapRow(ResultSet rs) throws SQLException {
                PurchaseOrder po = new PurchaseOrder();
                po.setPurchaseId(rs.getInt("purchase_id"));
                po.setSupplierId(rs.getInt("supplier_id"));
                po.setPurchaseDate(rs.getDate("purchase_date"));
                po.setTotalAmount(rs.getBigDecimal("total_amount"));
                po.setStatus(rs.getString("status"));
                po.setCreateTime(rs.getTimestamp("create_time"));
                return po;
            }
        });
    }

    // 修改采购单状态
    public int updatePurchaseStatus(Integer id, String status) throws SQLException {
        String sql = "UPDATE purchase_order SET status = ? WHERE purchase_id = ?";
        return executeUpdate(sql, status, id);
    }
    
    // 修改采购单
    public int updatePurchaseOrder(PurchaseOrder po) throws SQLException {
        String sql = "UPDATE purchase_order SET supplier_id=?, purchase_date=?, total_amount=?, status=? WHERE purchase_id=?";
        return executeUpdate(sql,
                po.getSupplierId(),
                po.getPurchaseDate(),
                po.getTotalAmount(),
                po.getStatus(),
                po.getPurchaseId());
    }
    
    // 删除采购单
    public int deletePurchaseOrder(Integer id) throws SQLException {
        String sql = "DELETE FROM purchase_order WHERE purchase_id=?";
        return executeUpdate(sql, id);
    }
}