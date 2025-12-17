package com.dayu.system.entity;

import java.sql.Timestamp;

public class Supplier {
    // 字段与数据库表一一对应（驼峰式匹配下划线命名）
    private Integer supplierId;        // 供应商ID（主键，自动递增）
    private String supplierName;       // 供应商名称（非空，索引）
    private String contact;      // 联系人
    private String phone;              // 联系电话（非空，索引）
    private String address;            // 地址
    private String status;             // 状态（默认"正常"：正常/停用，索引）
    private Timestamp createTime;      // 创建时间（默认当前时间）
    private Timestamp updateTime;      // 更新时间（默认当前时间）

    // 无参构造（用于结果集映射）
    public Supplier() {}

    // 全参构造（用于快速创建对象）
    public Supplier(Integer supplierId, String supplierName, String contact,
                    String phone, String address, String status,
                    Timestamp createTime, Timestamp updateTime) {
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.contact = contact;
        this.phone = phone;
        this.address = address;
        this.status = status;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    // Getter和Setter方法（必须，用于结果集映射和对象操作）
    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    // toString方法（可选，用于打印调试）
    @Override
    public String toString() {
        return "Supplier{" +
                "supplierId=" + supplierId +
                ", supplierName='" + supplierName + '\'' +
                ", contact='" + contact + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", status='" + status + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}