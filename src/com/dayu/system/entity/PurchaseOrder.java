package com.dayu.system.entity;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

public class PurchaseOrder {
    private Integer purchaseId;      // 采购单ID
    private Integer supplierId;      // 供应商ID
    private Date purchaseDate;       // 采购日期
    private BigDecimal totalAmount;  // 总金额
    private String status;           // 状态：待入库/已完成/已取消
    private Timestamp createTime;    // 创建时间

    // 无参构造
    public PurchaseOrder() {}

    // 全参构造
    public PurchaseOrder(Integer purchaseId, Integer supplierId, Date purchaseDate,
                         BigDecimal totalAmount, String status, Timestamp createTime) {
        this.purchaseId = purchaseId;
        this.supplierId = supplierId;
        this.purchaseDate = purchaseDate;
        this.totalAmount = totalAmount;
        this.status = status;
        this.createTime = createTime;
    }

    // Getter & Setter
    public Integer getPurchaseId() { return purchaseId; }
    public void setPurchaseId(Integer purchaseId) { this.purchaseId = purchaseId; }
    public Integer getSupplierId() { return supplierId; }
    public void setSupplierId(Integer supplierId) { this.supplierId = supplierId; }
    public Date getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(Date purchaseDate) { this.purchaseDate = purchaseDate; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Timestamp getCreateTime() { return createTime; }
    public void setCreateTime(Timestamp createTime) { this.createTime = createTime; }

    @Override
    public String toString() {
        return "PurchaseOrder{" +
                "purchaseId=" + purchaseId +
                ", supplierId=" + supplierId +
                ", purchaseDate=" + purchaseDate +
                ", totalAmount=" + totalAmount +
                ", status='" + status + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}