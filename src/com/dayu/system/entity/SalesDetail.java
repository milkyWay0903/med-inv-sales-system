package com.dayu.system.entity;

import java.math.BigDecimal;

public class SalesDetail {
    private Integer detailId;        // 明细ID
    private Integer salesId;         // 销售单ID
    private Integer medicineId;      // 药品ID
    private Integer quantity;        // 销售数量
    private BigDecimal unitPrice;    // 单价
    private BigDecimal amount;       // 小计金额
    private String remark;           // 备注

    public SalesDetail() {}

    public SalesDetail(Integer detailId, Integer salesId, Integer medicineId,
                       Integer quantity, BigDecimal unitPrice, BigDecimal amount, String remark) {
        this.detailId = detailId;
        this.salesId = salesId;
        this.medicineId = medicineId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.amount = amount;
        this.remark = remark;
    }

    // Getter & Setter
    public Integer getDetailId() { return detailId; }
    public void setDetailId(Integer detailId) { this.detailId = detailId; }
    public Integer getSalesId() { return salesId; }
    public void setSalesId(Integer salesId) { this.salesId = salesId; }
    public Integer getMedicineId() { return medicineId; }
    public void setMedicineId(Integer medicineId) { this.medicineId = medicineId; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

    @Override
    public String toString() {
        return "SalesDetail{" +
                "detailId=" + detailId +
                ", salesId=" + salesId +
                ", medicineId=" + medicineId +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", amount=" + amount +
                ", remark='" + remark + '\'' +
                '}';
    }
}