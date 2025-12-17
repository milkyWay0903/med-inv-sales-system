package com.dayu.system.view;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainFrame extends JFrame {
    private JPanel mainPanel;
    private JPanel navigationPanel;
    private JPanel contentPanel;
    private CardLayout cardLayout;

    // 导航按钮
    private JButton medSuppButton;
    private JButton purchaseButton;
    private JButton inventoryButton;
    private JButton salesButton;
    
    // 当前选中的按钮
    private JButton selectedButton;
    
    // 时间显示标签
    private JLabel dateLabel;
    private JLabel timeLabel;
    private Timer timer;

    public MainFrame() {
        initializeComponents();
        setupLayout();
        setupEventListeners();
        startTimeUpdater(); // 启动时间更新器
    }

    private void initializeComponents() {
        setTitle("大聿市第一中心医院药品存销管理系统");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);

        // 初始化面板
        mainPanel = new JPanel(new BorderLayout());
        navigationPanel = new JPanel();
        contentPanel = new JPanel();
        cardLayout = new CardLayout();
        contentPanel.setLayout(cardLayout);

        // 初始化导航按钮
        medSuppButton = new JButton("药品与供应商管理");
        purchaseButton = new JButton("采购与入库管理");
        inventoryButton = new JButton("库存与盘点管理");
        salesButton = new JButton("销售与出库管理");
        
        // 初始化日期和时间标签
        dateLabel = new JLabel();
        dateLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        dateLabel.setForeground(Color.WHITE);
        dateLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        timeLabel = new JLabel();
        timeLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
    }

    private void setupLayout() {
        // 设置导航面板
        navigationPanel.setLayout(new GridLayout(9, 1, 0, 10));
        navigationPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        navigationPanel.setPreferredSize(new Dimension(180, 0));
        navigationPanel.setBackground(new Color(0xFF87CEEB)); // 蓝色主题

        // 设置按钮样式
        setupButtonStyle(medSuppButton);
        setupButtonStyle(purchaseButton);
        setupButtonStyle(inventoryButton);
        setupButtonStyle(salesButton);

        // 添加日期时间标签和按钮
        navigationPanel.add(dateLabel);
        navigationPanel.add(timeLabel);
        navigationPanel.add(new JLabel()); // 占位
        navigationPanel.add(medSuppButton);
        navigationPanel.add(purchaseButton);
        navigationPanel.add(inventoryButton);
        navigationPanel.add(salesButton);
        navigationPanel.add(new JLabel()); // 占位
        navigationPanel.add(new JLabel()); // 占位

        // 初始化右侧为空面板
        JPanel emptyPanel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("欢迎使用大聿市第一中心医院药品存销管理系统", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("微软雅黑", Font.PLAIN, 30));
        welcomeLabel.setForeground(Color.GRAY);
        emptyPanel.add(welcomeLabel, BorderLayout.CENTER);
        contentPanel.add(emptyPanel, "EMPTY_PANEL");

        // 组装主面板
        mainPanel.add(navigationPanel, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);

        // 默认显示空面板
        cardLayout.show(contentPanel, "EMPTY_PANEL");
    }

    private void setupButtonStyle(JButton button) {
        button.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(81, 159, 213)); // 默认背景色
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    // 设置按钮为选中状态
    private void selectButton(JButton button) {
        // 取消之前选中按钮的高亮
        if (selectedButton != null) {
            selectedButton.setBackground(new Color(81, 159, 213)); // 恢复默认背景色
        }
        // 设置当前按钮为选中状态
        button.setBackground(Color.BLUE); // 蓝色高亮
        selectedButton = button;
    }
    
    // 启动时间更新器
    private void startTimeUpdater() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updateTime();
            }
        }, 0, 1000); // 每秒更新一次
    }
    
    // 更新时间显示
    private void updateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String currentDate = dateFormat.format(new Date());
        String currentTime = timeFormat.format(new Date());
        SwingUtilities.invokeLater(() -> {
            dateLabel.setText(currentDate);
            timeLabel.setText(currentTime);
        });
    }

    private void setupEventListeners() {
        medSuppButton.addActionListener(e -> {
            selectButton(medSuppButton);
            showMedSuppPanel();
        });
        purchaseButton.addActionListener(e -> {
            selectButton(purchaseButton);
            showPurchasePanel();
        });
        inventoryButton.addActionListener(e -> {
            selectButton(inventoryButton);
            showInventoryPanel();
        });
        salesButton.addActionListener(e -> {
            selectButton(salesButton);
            showSalesPanel();
        });
    }

    private void showMedSuppPanel() {
        // 检查面板是否已存在，不存在则创建
        boolean panelExists = false;
        for (Component comp : contentPanel.getComponents()) {
            if ("MED_SUPP_PANEL".equals(comp.getName())) {
                panelExists = true;
                break;
            }
        }

        if (!panelExists) {
            // 创建并添加药品与供应商管理面板
            MedAndSuppPanel medAndSuppPanel = new MedAndSuppPanel();
            medAndSuppPanel.setName("MED_SUPP_PANEL");
            contentPanel.add(medAndSuppPanel, "MED_SUPP_PANEL");
        }
        cardLayout.show(contentPanel, "MED_SUPP_PANEL");
    }

    private void showPurchasePanel() {
        // 检查面板是否已存在，不存在则创建
        boolean panelExists = false;
        for (Component comp : contentPanel.getComponents()) {
            if ("PURCHASE_PANEL".equals(comp.getName())) {
                panelExists = true;
                break;
            }
        }

        if (!panelExists) {
            // 创建并添加采购与入库管理面板
            PurchasePanel purchasePanel = new PurchasePanel();
            purchasePanel.setName("PURCHASE_PANEL");
            contentPanel.add(purchasePanel, "PURCHASE_PANEL");
        }
        cardLayout.show(contentPanel, "PURCHASE_PANEL");
    }

    private void showInventoryPanel() {
        // 检查面板是否已存在，不存在则创建
        boolean panelExists = false;
        for (Component comp : contentPanel.getComponents()) {
            if ("INVENTORY_PANEL".equals(comp.getName())) {
                panelExists = true;
                break;
            }
        }

        if (!panelExists) {
            // 创建并添加库存与盘点管理面板
            InventoryPanel inventoryPanel = new InventoryPanel();
            inventoryPanel.setName("INVENTORY_PANEL");
            contentPanel.add(inventoryPanel, "INVENTORY_PANEL");
        }
        cardLayout.show(contentPanel, "INVENTORY_PANEL");
    }

    private void showSalesPanel() {
        // 检查面板是否已存在，不存在则创建
        boolean panelExists = false;
        for (Component comp : contentPanel.getComponents()) {
            if ("SALES_PANEL".equals(comp.getName())) {
                panelExists = true;
                break;
            }
        }

        if (!panelExists) {
            // 创建并添加销售与出库管理面板
            SalesPanel salesPanel = new SalesPanel();
            salesPanel.setName("SALES_PANEL");
            contentPanel.add(salesPanel, "SALES_PANEL");
        }
        cardLayout.show(contentPanel, "SALES_PANEL");
    }
    
    // 重写dispose方法，确保timer被正确关闭
    @Override
    public void dispose() {
        if (timer != null) {
            timer.cancel();
        }
        super.dispose();
    }
}