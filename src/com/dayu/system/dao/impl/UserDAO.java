package com.dayu.system.dao.impl;

import com.dayu.system.dao.BaseDAO;
import com.dayu.system.dao.RowMapper;
import com.dayu.system.entity.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDAO extends BaseDAO<User> {
    // 新增用户
    public int addUser(User user) throws SQLException {
        String sql = "INSERT INTO user(username, password, real_name, role, status, create_time) VALUES(?, ?, ?, ?, ?, ?)";
        return executeUpdate(sql,
                user.getUsername(),
                user.getPassword(),
                user.getRealName(),
                user.getRole(),
                user.getStatus() == null ? "正常" : user.getStatus(),
                user.getCreateTime());
    }

    // 登录验证（用户名+密码）
    public User login(String username, String password) throws SQLException {
        String sql = "SELECT * FROM system_user WHERE username = ? AND password = ? AND status = '正常'";
        return queryOne(sql, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs) throws SQLException {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRealName(rs.getString("real_name"));
                user.setRole(rs.getString("role"));
                user.setStatus(rs.getString("status"));
                user.setCreateTime(rs.getTimestamp("create_time"));
                return user;
            }
        }, username, password);
    }

    // 根据用户名查询用户
    public User getUserByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM user WHERE username = ?";
        return queryOne(sql, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs) throws SQLException {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRealName(rs.getString("real_name"));
                user.setRole(rs.getString("role"));
                user.setStatus(rs.getString("status"));
                user.setCreateTime(rs.getTimestamp("create_time"));
                return user;
            }
        }, username);
    }
}