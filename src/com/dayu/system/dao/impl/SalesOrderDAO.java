package com.dayu.system.dao.impl;

import com.dayu.system.dao.BaseDAO;
import com.dayu.system.dao.RowMapper;
import com.dayu.system.entity.SalesOrder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SalesOrderDAO extends BaseDAO<SalesOrder> {
    // 新增销售单
    public int addSalesOrder(SalesOrder so) throws SQLException {
        String sql = "INSERT INTO sales_order(customer_name, sales_date, total_amount, status, create_time) VALUES(?, ?, ?, ?, ?)";
        return executeUpdate(sql,
                so.getCustomerName(),
                so.getSalesDate(),
                so.getTotalAmount(),
                so.getStatus() == null ? "待收款" : so.getStatus(),
                so.getCreateTime());
    }

    // 根据ID查询销售单
    public SalesOrder getSalesOrderById(Integer id) throws SQLException {
        String sql = "SELECT * FROM sales_order WHERE sales_id = ?";
        return queryOne(sql, new RowMapper<SalesOrder>() {
            @Override
            public SalesOrder mapRow(ResultSet rs) throws SQLException {
                SalesOrder so = new SalesOrder();
                so.setSalesId(rs.getInt("sales_id"));
                so.setCustomerName(rs.getString("customer_name"));
                so.setSalesDate(rs.getDate("sales_date"));
                so.setTotalAmount(rs.getBigDecimal("total_amount"));
                so.setStatus(rs.getString("status"));
                so.setCreateTime(rs.getTimestamp("create_time"));
                return so;
            }
        }, id);
    }

    // 查询所有销售单
    public List<SalesOrder> getAllSalesOrders() throws SQLException {
        String sql = "SELECT * FROM sales_order ORDER BY sales_id DESC";
        return queryList(sql, new RowMapper<SalesOrder>() {
            @Override
            public SalesOrder mapRow(ResultSet rs) throws SQLException {
                SalesOrder so = new SalesOrder();
                so.setSalesId(rs.getInt("sales_id"));
                so.setCustomerName(rs.getString("customer_name"));
                so.setSalesDate(rs.getDate("sales_date"));
                so.setTotalAmount(rs.getBigDecimal("total_amount"));
                so.setStatus(rs.getString("status"));
                so.setCreateTime(rs.getTimestamp("create_time"));
                return so;
            }
        });
    }
    
    // 修改销售单
    public int updateSalesOrder(SalesOrder so) throws SQLException {
        String sql = "UPDATE sales_order SET customer_name=?, sales_date=?, total_amount=?, status=? WHERE sales_id=?";
        return executeUpdate(sql,
                so.getCustomerName(),
                so.getSalesDate(),
                so.getTotalAmount(),
                so.getStatus(),
                so.getSalesId());
    }
    
    // 删除销售单
    public int deleteSalesOrder(Integer id) throws SQLException {
        String sql = "DELETE FROM sales_order WHERE sales_id=?";
        return executeUpdate(sql, id);
    }
}