package com.dayu.system.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 药品信息表实体（对应表：medicine）
 */
public class Medicine {
    // 字段与数据字典完全对应
    private Integer medicineId;         // 药品ID（主键，自动递增）
    private String medicineName;        // 药品名称（非空，索引）
    private String specification;       // 规格
    private String unit;                // 单位
    private String manufacturer;        // 生产厂家
    private String category;            // 类别（索引）
    private BigDecimal price;           // 单价（默认0.00）
    private String status;              // 状态（默认"正常"：正常/停用）
    private Timestamp createTime;       // 创建时间（默认当前时间）
    private Timestamp updateTime;       // 更新时间（默认当前时间）

    // 无参构造（必须，用于结果集映射）
    public Medicine() {}

    // 全参构造（可选，用于快速创建对象）
    public Medicine(Integer medicineId, String medicineName, String specification, String unit, 
                    String manufacturer, String category, BigDecimal price, String status, 
                    Timestamp createTime, Timestamp updateTime) {
        this.medicineId = medicineId;
        this.medicineName = medicineName;
        this.specification = specification;
        this.unit = unit;
        this.manufacturer = manufacturer;
        this.category = category;
        this.price = price;
        this.status = status;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    // Getter和Setter（必须，用于结果集映射和对象操作）
    public Integer getMedicineId() { return medicineId; }
    public void setMedicineId(Integer medicineId) { this.medicineId = medicineId; }
    public String getMedicineName() { return medicineName; }
    public void setMedicineName(String medicineName) { this.medicineName = medicineName; }
    public String getSpecification() { return specification; }
    public void setSpecification(String specification) { this.specification = specification; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Timestamp getCreateTime() { return createTime; }
    public void setCreateTime(Timestamp createTime) { this.createTime = createTime; }
    public Timestamp getUpdateTime() { return updateTime; }
    public void setUpdateTime(Timestamp updateTime) { this.updateTime = updateTime; }

    @Override
    public String toString() {
        return "Medicine{" +
                "medicineId=" + medicineId +
                ", medicineName='" + medicineName + '\'' +
                ", specification='" + specification + '\'' +
                ", unit='" + unit + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", status='" + status + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}