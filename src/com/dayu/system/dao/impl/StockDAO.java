package com.dayu.system.dao.impl;

import com.dayu.system.dao.BaseDAO;
import com.dayu.system.dao.RowMapper;
import com.dayu.system.entity.Stock;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class StockDAO extends BaseDAO<Stock> {
    // 新增库存记录（允许同一药品有多个库存记录）
    public int addStock(Stock stock) throws SQLException {
        String sql = "INSERT INTO stock(medicine_id, current_quantity, min_quantity, max_quantity, location, update_time, status) VALUES(?, ?, ?, ?, ?, ?, ?)";
        return executeUpdate(sql,
                stock.getMedicineId(),
                stock.getCurrentQuantity(),
                stock.getMinQuantity(),
                stock.getMaxQuantity(),
                stock.getLocation(),
                stock.getUpdateTime() == null ? new Timestamp(System.currentTimeMillis()) : stock.getUpdateTime(),
                stock.getStatus());
    }

    // 更新库存记录（根据库存ID更新）
    public int updateStock(Stock stock) throws SQLException {
        String sql = "UPDATE stock SET medicine_id = ?, current_quantity = ?, min_quantity = ?, max_quantity = ?, location = ?, update_time = ?, status = ? WHERE stock_id = ?";
        return executeUpdate(sql,
                stock.getMedicineId(),
                stock.getCurrentQuantity(),
                stock.getMinQuantity(),
                stock.getMaxQuantity(),
                stock.getLocation(),
                new Timestamp(System.currentTimeMillis()),
                stock.getStatus(),
                stock.getStockId());
    }

    // 根据药品ID查询库存（可能返回多条记录）
    public List<Stock> getStocksByMedicineId(Integer medicineId) throws SQLException {
        String sql = "SELECT * FROM stock WHERE medicine_id = ?";
        return queryList(sql, new RowMapper<Stock>() {
            @Override
            public Stock mapRow(ResultSet rs) throws SQLException {
                Stock stock = new Stock();
                stock.setStockId(rs.getInt("stock_id"));
                stock.setMedicineId(rs.getInt("medicine_id"));
                stock.setCurrentQuantity(rs.getInt("current_quantity"));
                stock.setMinQuantity(rs.getInt("min_quantity"));
                stock.setMaxQuantity(rs.getInt("max_quantity"));
                stock.setLocation(rs.getString("location"));
                stock.setUpdateTime(rs.getTimestamp("update_time"));
                stock.setStatus(rs.getString("status"));
                return stock;
            }
        }, medicineId);
    }
    
    // 根据药品ID查询库存（只返回一条记录，用于向后兼容）
    public Stock getStockByMedicineId(Integer medicineId) throws SQLException {
        String sql = "SELECT * FROM stock WHERE medicine_id = ? LIMIT 1";
        return queryOne(sql, new RowMapper<Stock>() {
            @Override
            public Stock mapRow(ResultSet rs) throws SQLException {
                Stock stock = new Stock();
                stock.setStockId(rs.getInt("stock_id"));
                stock.setMedicineId(rs.getInt("medicine_id"));
                stock.setCurrentQuantity(rs.getInt("current_quantity"));
                stock.setMinQuantity(rs.getInt("min_quantity"));
                stock.setMaxQuantity(rs.getInt("max_quantity"));
                stock.setLocation(rs.getString("location"));
                stock.setUpdateTime(rs.getTimestamp("update_time"));
                stock.setStatus(rs.getString("status"));
                return stock;
            }
        }, medicineId);
    }
    
    // 获取所有库存
    public List<Stock> getAllStocks() throws SQLException {
        String sql = "SELECT * FROM stock";
        return queryList(sql, new RowMapper<Stock>() {
            @Override
            public Stock mapRow(ResultSet rs) throws SQLException {
                Stock stock = new Stock();
                stock.setStockId(rs.getInt("stock_id"));
                stock.setMedicineId(rs.getInt("medicine_id"));
                stock.setCurrentQuantity(rs.getInt("current_quantity"));
                stock.setMinQuantity(rs.getInt("min_quantity"));
                stock.setMaxQuantity(rs.getInt("max_quantity"));
                stock.setLocation(rs.getString("location"));
                stock.setUpdateTime(rs.getTimestamp("update_time"));
                stock.setStatus(rs.getString("status"));
                return stock;
            }
        });
    }

    // 根据库存ID获取库存记录
    public Stock getStockById(Integer stockId) throws SQLException {
        String sql = "SELECT * FROM stock WHERE stock_id = ?";
        return queryOne(sql, new RowMapper<Stock>() {
            @Override
            public Stock mapRow(ResultSet rs) throws SQLException {
                Stock stock = new Stock();
                stock.setStockId(rs.getInt("stock_id"));
                stock.setMedicineId(rs.getInt("medicine_id"));
                stock.setCurrentQuantity(rs.getInt("current_quantity"));
                stock.setMinQuantity(rs.getInt("min_quantity"));
                stock.setMaxQuantity(rs.getInt("max_quantity"));
                stock.setLocation(rs.getString("location"));
                stock.setUpdateTime(rs.getTimestamp("update_time"));
                stock.setStatus(rs.getString("status"));
                return stock;
            }
        }, stockId);
    }

    // 查询库存预警列表（当前数量<最小数量）
    public List<Stock> getWarningStock() throws SQLException {
        String sql = "SELECT * FROM stock WHERE current_quantity < min_quantity";
        return queryList(sql, new RowMapper<Stock>() {
            @Override
            public Stock mapRow(ResultSet rs) throws SQLException {
                Stock stock = new Stock();
                stock.setStockId(rs.getInt("stock_id"));
                stock.setMedicineId(rs.getInt("medicine_id"));
                stock.setCurrentQuantity(rs.getInt("current_quantity"));
                stock.setMinQuantity(rs.getInt("min_quantity"));
                stock.setMaxQuantity(rs.getInt("max_quantity"));
                stock.setLocation(rs.getString("location"));
                stock.setUpdateTime(rs.getTimestamp("update_time"));
                stock.setStatus(rs.getString("status"));
                return stock;
            }
        });
    }
    
    // 更新库存数量
    public int updateStockQuantity(Integer medicineId, Integer quantityChange) throws SQLException {
        // 先查询是否有该药品的库存记录
        List<Stock> stocks = getStocksByMedicineId(medicineId);
        
        if (stocks.isEmpty()) {
            // 如果没有库存记录，则新增一条库存记录
            Stock newStock = new Stock();
            newStock.setMedicineId(medicineId);
            newStock.setCurrentQuantity(Math.max(0, quantityChange)); // 确保库存不为负数
            newStock.setMinQuantity(10);  // 默认最小库存
            newStock.setMaxQuantity(1000); // 默认最大库存
            newStock.setLocation("默认库位");
            newStock.setStatus("正常");
            newStock.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            return addStock(newStock);
        } else {
            // 如果有库存记录，则更新第一条记录的数量
            Stock stock = stocks.get(0);
            int newQuantity = stock.getCurrentQuantity() + quantityChange;
            
            // 确保库存不为负数
            if (newQuantity < 0) {
                return -1; // 返回-1表示库存不足
            }
            
            stock.setCurrentQuantity(newQuantity);
            stock.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            return updateStock(stock);
        }
    }
}