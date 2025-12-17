package com.dayu.system.util;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * JDBC工具类：管理连接、关闭资源、事务控制
 */
public class JDBCUtils {
    // 静态变量存储配置信息
    private static String driver;
    private static String url;
    private static String username;
    private static String password;

    // ThreadLocal：存储当前线程的Connection，用于事务管理（保证同一线程事务内使用同一连接）
    private static ThreadLocal<Connection> threadLocal = new ThreadLocal<>();

    // 静态代码块：加载配置文件（类加载时执行一次）
    static {
        try {
            // 读取配置文件
            InputStream is = JDBCUtils.class.getClassLoader().getResourceAsStream("db.properties");
            Properties props = new Properties();
            props.load(is);

            // 赋值配置参数
            driver = props.getProperty("jdbc.driver");
            url = props.getProperty("jdbc.url");
            username = props.getProperty("jdbc.username");
            password = props.getProperty("jdbc.password");

            // 加载数据库驱动（MySQL 8.0+可省略，但为了兼容性保留）
            Class.forName(driver);
        } catch (Exception e) {
            throw new RuntimeException("JDBC工具类初始化失败", e);
        }
    }

    /**
     * 获取数据库连接（优先从ThreadLocal获取，无则新建）
     */
    public static Connection getConnection() throws SQLException {
        Connection conn = threadLocal.get();
        if (conn == null || conn.isClosed()) {
            conn = DriverManager.getConnection(url, username, password);
            threadLocal.set(conn); // 存入ThreadLocal，供事务使用
        }
        return conn;
    }

    /**
     * 关闭资源（ResultSet、Statement、Connection）
     * 注意：事务模式下不关闭Connection，由事务手动控制
     */
    public static void close(ResultSet rs, Statement stmt, Connection conn) {
        try {
            if (rs != null) rs.close(); // 关闭结果集
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (stmt != null) stmt.close(); // 关闭Statement
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // 非事务模式下关闭连接（ThreadLocal中无连接则为非事务）
        if (threadLocal.get() == null) {
            try {
                if (conn != null && !conn.isClosed()) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // ---------------------- 事务控制方法 ----------------------

    /**
     * 开启事务（关闭自动提交）
     */
    public static void beginTransaction() throws SQLException {
        Connection conn = getConnection();
        conn.setAutoCommit(false); // 关闭自动提交，开启事务
    }

    /**
     * 提交事务
     */
    public static void commitTransaction() throws SQLException {
        Connection conn = threadLocal.get();
        if (conn != null) {
            conn.commit(); // 提交事务
        }
    }

    /**
     * 回滚事务
     */
    public static void rollbackTransaction() throws SQLException {
        Connection conn = threadLocal.get();
        if (conn != null) {
            conn.rollback(); // 回滚事务
        }
    }

    /**
     * 事务结束后清除ThreadLocal（释放连接）
     */
    public static void releaseTransaction() throws SQLException {
        Connection conn = threadLocal.get();
        if (conn != null) {
            conn.setAutoCommit(true); // 恢复自动提交
            conn.close(); // 关闭连接
            threadLocal.remove(); // 清除ThreadLocal
        }
    }
}