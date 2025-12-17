package com.dayu.system.view;

import com.dayu.system.dao.impl.UserDAO;
import com.dayu.system.entity.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

/**
 * 登陆界面
 */
public class LoginFrame extends JFrame {
    private JTextField unameTfd;
    private JPasswordField upassTfd;
    
    // 构造方法
    LoginFrame() {
        initFrame();
        initComp();
    }

    /**
     * 初始化窗体
     */
    public void initFrame() {
        // 设置窗体大小
        setSize(500, 300);
        // 设置标题
        setTitle("登录");
        // 设置显示位置居中
        setLocationRelativeTo(null);
        // 设置窗体关闭时退出系统
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        // 去掉布局管理器  设置为绝对布局
        setLayout(null);
    }

    /**
     * 初始化组件
     */
    public void initComp() {
        // 字体
        Font font1 = new Font("微软雅黑", Font.BOLD, 20);
        Font font2 = new Font("微软雅黑", Font.PLAIN, 16);

        // 欢迎标题
        JLabel titleLbl = new JLabel("欢迎使用大聿市第一中心医院药品存销管理系统");
        titleLbl.setFont(font1);
        titleLbl.setHorizontalAlignment(JLabel.CENTER); // 水平居中
        titleLbl.setBackground(new Color(0xFF87CEEB)); // 设置背景色
        titleLbl.setOpaque(true); // 设置非透明
        titleLbl.setForeground(Color.WHITE);
        titleLbl.setBounds(0, 20, getWidth(), 60);
        add(titleLbl);

        // 创建用户名输入文本框
        unameTfd = new JTextField();
        unameTfd.setBounds(150, 100, 200, 30);
        add(unameTfd);
        // 创建用户名标签
        JLabel unameLbl = new JLabel("账号:");
        unameLbl.setFont(font2);
        unameLbl.setBounds(100, 100, 50, 30);
        add(unameLbl);

        // 创建密码输入框
        upassTfd = new JPasswordField();
        upassTfd.setBounds(150, 150, 200, 30);
        add(upassTfd);
        // 创建密码标签
        JLabel upassLbl = new JLabel("密码:");
        upassLbl.setFont(font2);
        upassLbl.setBounds(100, 150, 50, 30);
        add(upassLbl);

        // 创建登录按钮
        JButton loginBtn = new JButton("登录");
        loginBtn.setFont(font2);
        loginBtn.setBounds(200, 200, 100, 30);
        add(loginBtn);
        
        // 添加登录按钮事件监听器
        loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
    }
    
    // 登录处理方法
    private void login() {
        String username = unameTfd.getText().trim();
        String password = new String(upassTfd.getPassword()).trim();
        
        // 验证输入
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入用户名和密码！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // 数据库验证
        UserDAO userDAO = new UserDAO();
        try {
            User user = userDAO.login(username, password);
            if (user != null) {
                // 登录成功
                JOptionPane.showMessageDialog(this, "登录成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
                // 关闭登录窗口
                this.dispose();
                // 打开主窗口
                SwingUtilities.invokeLater(() -> {
                    new MainFrame().setVisible(true);
                });
            } else {
                // 登录失败
                JOptionPane.showMessageDialog(this, "用户名或密码错误！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "数据库连接错误：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new LoginFrame().setVisible(true);
    }
}