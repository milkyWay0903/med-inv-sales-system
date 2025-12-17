package com.dayu.system.entity;

import java.sql.Timestamp;

public class Stock {
    private Integer stockId;         // 库存ID
    private Integer medicineId;      // 药品ID
    private Integer currentQuantity; // 当前数量
    private Integer minQuantity;     // 最小数量
    private Integer maxQuantity;     // 最大数量
    private String location;         // 库位
    private Timestamp updateTime;    // 更新时间
    private String status;           // 状态

    public Stock() {}

    public Stock(Integer stockId, Integer medicineId, Integer currentQuantity,
                 Integer minQuantity, Integer maxQuantity, String location, 
                 Timestamp updateTime, String status) {
        this.stockId = stockId;
        this.medicineId = medicineId;
        this.currentQuantity = currentQuantity;
        this.minQuantity = minQuantity;
        this.maxQuantity = maxQuantity;
        this.location = location;
        this.updateTime = updateTime;
        this.status = status;
    }

    // Getter & Setter
    public Integer getStockId() { return stockId; }
    public void setStockId(Integer stockId) { this.stockId = stockId; }
    public Integer getMedicineId() { return medicineId; }
    public void setMedicineId(Integer medicineId) { this.medicineId = medicineId; }
    public Integer getCurrentQuantity() { return currentQuantity; }
    public void setCurrentQuantity(Integer currentQuantity) { this.currentQuantity = currentQuantity; }
    public Integer getMinQuantity() { return minQuantity; }
    public void setMinQuantity(Integer minQuantity) { this.minQuantity = minQuantity; }
    public Integer getMaxQuantity() { return maxQuantity; }
    public void setMaxQuantity(Integer maxQuantity) { this.maxQuantity = maxQuantity; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public Timestamp getUpdateTime() { return updateTime; }
    public void setUpdateTime(Timestamp updateTime) { this.updateTime = updateTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "Stock{" +
                "stockId=" + stockId +
                ", medicineId=" + medicineId +
                ", currentQuantity=" + currentQuantity +
                ", minQuantity=" + minQuantity +
                ", maxQuantity=" + maxQuantity +
                ", location='" + location + '\'' +
                ", updateTime=" + updateTime +
                ", status='" + status + '\'' +
                '}';
    }
}