package com.dayu.system.entity;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

public class SalesOrder {
    private Integer salesId;         // 销售单ID
    private String customerName;     // 客户名称
    private Date salesDate;          // 销售日期
    private BigDecimal totalAmount;  // 总金额
    private String status;           // 状态：待收款/已完成/已取消
    private Timestamp createTime;    // 创建时间

    public SalesOrder() {}

    public SalesOrder(Integer salesId, String customerName,
                      Date salesDate, BigDecimal totalAmount, String status, Timestamp createTime) {
        this.salesId = salesId;
        this.customerName = customerName;
        this.salesDate = salesDate;
        this.totalAmount = totalAmount;
        this.status = status;
        this.createTime = createTime;
    }

    // Getter & Setter
    public Integer getSalesId() { return salesId; }
    public void setSalesId(Integer salesId) { this.salesId = salesId; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public Date getSalesDate() { return salesDate; }
    public void setSalesDate(Date salesDate) { this.salesDate = salesDate; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Timestamp getCreateTime() { return createTime; }
    public void setCreateTime(Timestamp createTime) { this.createTime = createTime; }

    @Override
    public String toString() {
        return "SalesOrder{" +
                "salesId=" + salesId +
                ", customerName='" + customerName + '\'' +
                ", salesDate=" + salesDate +
                ", totalAmount=" + totalAmount +
                ", status='" + status + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}