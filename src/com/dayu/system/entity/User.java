package com.dayu.system.entity;

import java.sql.Timestamp;

public class User {
    private Integer userId;          // 用户ID
    private String username;         // 用户名
    private String password;         // 密码（建议加密）
    private String realName;         // 真实姓名
    private String role;             // 角色：管理员/采购员/销售员
    private String status;           // 状态：正常/禁用
    private Timestamp createTime;    // 创建时间

    public User() {}

    public User(Integer userId, String username, String password,
                String realName, String role, String status, Timestamp createTime) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.realName = realName;
        this.role = role;
        this.status = status;
        this.createTime = createTime;
    }

    // Getter & Setter
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRealName() { return realName; }
    public void setRealName(String realName) { this.realName = realName; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Timestamp getCreateTime() { return createTime; }
    public void setCreateTime(Timestamp createTime) { this.createTime = createTime; }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", realName='" + realName + '\'' +
                ", role='" + role + '\'' +
                ", status='" + status + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
