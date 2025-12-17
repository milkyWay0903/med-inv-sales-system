package com.dayu.system.entity;

import java.math.BigDecimal;

public class PurchaseDetail {
    private Integer purchaseDetailId;        // 明细ID
    private Integer purchaseId;      // 采购单ID
    private Integer medicineId;      // 药品ID
    private Integer quantity;        // 采购数量
    private BigDecimal unitPrice;    // 单价
    private BigDecimal amount;       // 小计金额
    private String remark;           // 备注

    public PurchaseDetail() {}

    public PurchaseDetail(Integer purchaseDetailId, Integer purchaseId, Integer medicineId,
                          Integer quantity, BigDecimal unitPrice, BigDecimal amount, String remark) {
        this.purchaseDetailId = purchaseDetailId;
        this.purchaseId = purchaseId;
        this.medicineId = medicineId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.amount = amount;
        this.remark = remark;
    }

    // Getter & Setter
    public Integer getPurchaseDetailId() { return purchaseDetailId; }
    public void setPurchaseDetailId(Integer purchaseDetailId) { this.purchaseDetailId = purchaseDetailId; }
    public Integer getPurchaseId() { return purchaseId; }
    public void setPurchaseId(Integer purchaseId) { this.purchaseId = purchaseId; }
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
        return "PurchaseDetail{" +
                "detailId=" + purchaseDetailId +
                ", purchaseId=" + purchaseId +
                ", medicineId=" + medicineId +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", amount=" + amount +
                ", remark='" + remark + '\'' +
                '}';
    }
}