package com.dayu.system.view;

import com.dayu.system.dao.impl.MedicineDAO;
import com.dayu.system.dao.impl.SalesOrderDAO;
import com.dayu.system.dao.impl.SalesDetailDAO;
import com.dayu.system.dao.impl.StockDAO;
import com.dayu.system.entity.Medicine;
import com.dayu.system.entity.SalesOrder;
import com.dayu.system.entity.SalesDetail;
import com.dayu.system.entity.Stock;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

/**
 * 销售与出库管理面板（最终版：表单+按钮同框+布局不挤压+按钮完全可见）
 */
public class SalesPanel extends JPanel {
    // DAO对象
    private final SalesOrderDAO salesOrderDAO = new SalesOrderDAO();
    private final SalesDetailDAO salesDetailDAO = new SalesDetailDAO();
    private final MedicineDAO medicineDAO = new MedicineDAO();
    private final StockDAO stockDAO = new StockDAO();

    // 组件声明
    private JTable orderTable, detailTable;
    private DefaultTableModel orderTableModel, detailTableModel;
    private JTextField tfSalesId, tfCustomerName, tfSalesDate, tfTotalAmount, tfStatus;
    private JTextField tfDetailId, tfQuantity, tfUnitPrice, tfAmount, tfRemark;
    private JComboBox<String> medicineComboBox, salesIdComboBox;
    private JButton btnAddOrder, btnUpdateOrder, btnDeleteOrder, btnRefreshOrder;
    private JButton btnAddDetail, btnUpdateDetail, btnDeleteDetail, btnRefreshDetail;

    public SalesPanel() {
        // 基础布局与边距
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setPreferredSize(new Dimension(1024, 768)); // 固定面板首选尺寸，避免缩放异常

        // 创建上下分割面板（订单+明细）
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(400); // 分割线位置，适配大部分屏幕
        splitPane.setResizeWeight(0.5);    // 上下面板拉伸权重均等
        splitPane.setOneTouchExpandable(true); // 增加展开/折叠按钮

        // 订单面板（上半部分）+ 明细面板（下半部分）
        splitPane.setTopComponent(createOrderPanel());
        splitPane.setBottomComponent(createDetailPanel());

        add(splitPane, BorderLayout.CENTER);

        // 加载初始数据
        loadOrderData();
        loadDetailData();  // 添加这一行以在界面初始化时加载销售明细数据
        loadMedicineComboBox();
    }

    /**
     * 销售订单面板：表单+按钮同框显示，表格独立
     */
    private JPanel createOrderPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.setPreferredSize(new Dimension(0, 400));

        // ========== 核心：表单+按钮合并到同一个边框面板 ==========
        JPanel orderContainer = new JPanel();
        orderContainer.setLayout(new BoxLayout(orderContainer, BoxLayout.Y_AXIS));
        TitledBorder orderBorder = BorderFactory.createTitledBorder("销售订单信息"); // 统一边框
        orderBorder.setTitleFont(new Font("微软雅黑", Font.PLAIN, 12));
        orderContainer.setBorder(orderBorder);
        orderContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        // 1. 订单表单区域（GridBagLayout精准对齐）
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // 第一行：销售单ID + 客户姓名
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel salesIdLabel = new JLabel("销售单ID：");
        salesIdLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(salesIdLabel, gbc);
        gbc.gridx = 1; gbc.weightx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        tfSalesId = new JTextField(20);
        tfSalesId.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(tfSalesId, gbc);
        gbc.gridx = 2; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        JLabel customerNameLabel = new JLabel("客户姓名：");
        customerNameLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(customerNameLabel, gbc);
        gbc.gridx = 3; gbc.weightx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        tfCustomerName = new JTextField(20);
        tfCustomerName.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(tfCustomerName, gbc);

        // 第二行：销售日期
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        JLabel salesDateLabel = new JLabel("销售日期：");
        salesDateLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(salesDateLabel, gbc);
        gbc.gridx = 1; gbc.weightx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        tfSalesDate = new JTextField(20);
        tfSalesDate.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(tfSalesDate, gbc);

        // 第三行：总金额 + 状态
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        JLabel totalAmountLabel = new JLabel("总金额：");
        totalAmountLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(totalAmountLabel, gbc);
        gbc.gridx = 1; gbc.weightx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        tfTotalAmount = new JTextField(20);
        tfTotalAmount.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(tfTotalAmount, gbc);
        gbc.gridx = 2; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        JLabel statusLabel = new JLabel("状态：");
        statusLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(statusLabel, gbc);
        gbc.gridx = 3; gbc.weightx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        tfStatus = new JTextField("待收款", 20);
        tfStatus.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(tfStatus, gbc);

        // 2. 操作按钮区域（FlowLayout，固定高度不挤压）
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        btnPanel.setPreferredSize(new Dimension(0, 50)); // 固定按钮面板高度
        btnAddOrder = createButton("新增订单");
        btnUpdateOrder = createButton("修改订单");
        btnDeleteOrder = createButton("删除订单");
        btnRefreshOrder = createButton("刷新订单");
        btnPanel.add(btnAddOrder);
        btnPanel.add(btnUpdateOrder);
        btnPanel.add(btnDeleteOrder);
        btnPanel.add(btnRefreshOrder);

        // 表单+按钮合并到同边框面板
        orderContainer.add(formPanel);
        orderContainer.add(Box.createVerticalStrut(5)); // 表单与按钮间距
        orderContainer.add(btnPanel);

        // 3. 订单表格区域（独立于边框外，固定高度）
        String[] orderColumns = {"销售单ID", "客户姓名", "销售日期", "总金额", "状态", "创建时间"};
        orderTableModel = new DefaultTableModel(orderColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 表格不可编辑
            }
        };
        orderTable = new JTable(orderTableModel);
        orderTable.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        orderTable.setRowHeight(20);
        JScrollPane tableScroll = new JScrollPane(orderTable);
        tableScroll.setPreferredSize(new Dimension(0, 150)); // 表格固定高度

        // 组装订单面板：合并面板 + 表格
        panel.add(orderContainer);
        panel.add(Box.createVerticalStrut(8));
        panel.add(tableScroll);

        // 绑定订单事件
        bindOrderEvents();

        return panel;
    }

    /**
     * 销售明细面板：表单+按钮同框显示，表格独立（与订单面板样式完全一致）
     */
    private JPanel createDetailPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.setPreferredSize(new Dimension(0, 400));

        // ========== 核心：表单+按钮合并到同一个边框面板 ==========
        JPanel detailContainer = new JPanel();
        detailContainer.setLayout(new BoxLayout(detailContainer, BoxLayout.Y_AXIS));
        TitledBorder detailBorder = BorderFactory.createTitledBorder("销售明细信息"); // 统一边框
        detailBorder.setTitleFont(new Font("微软雅黑", Font.PLAIN, 12));
        detailContainer.setBorder(detailBorder);
        detailContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        // 1. 明细表单区域（GridBagLayout精准对齐）
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // 第一行：明细ID + 销售单ID + 药品
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel detailIdLabel = new JLabel("明细ID：");
        detailIdLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(detailIdLabel, gbc);
        gbc.gridx = 1; gbc.weightx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        tfDetailId = new JTextField(15);
        tfDetailId.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(tfDetailId, gbc);
        gbc.gridx = 2; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        JLabel salesOrderIdLabel = new JLabel("销售单ID：");
        salesOrderIdLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(salesOrderIdLabel, gbc);
        gbc.gridx = 3; gbc.weightx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        salesIdComboBox = new JComboBox<>();
        salesIdComboBox.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(salesIdComboBox, gbc);
        gbc.gridx = 4; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        JLabel medicineLabel = new JLabel("药品：");
        medicineLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(medicineLabel, gbc);
        gbc.gridx = 5; gbc.weightx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        medicineComboBox = new JComboBox<>();
        medicineComboBox.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(medicineComboBox, gbc);

        // 第二行：数量 + 单价 + 小计
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        JLabel quantityLabel = new JLabel("数量：");
        quantityLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(quantityLabel, gbc);
        gbc.gridx = 1; gbc.weightx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        tfQuantity = new JTextField(15);
        tfQuantity.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(tfQuantity, gbc);
        gbc.gridx = 2; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        JLabel unitPriceLabel = new JLabel("单价：");
        unitPriceLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(unitPriceLabel, gbc);
        gbc.gridx = 3; gbc.weightx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        tfUnitPrice = new JTextField(15);
        tfUnitPrice.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(tfUnitPrice, gbc);
        gbc.gridx = 4; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        JLabel amountLabel = new JLabel("小计：");
        amountLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(amountLabel, gbc);
        gbc.gridx = 5; gbc.weightx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        tfAmount = new JTextField(15);
        tfAmount.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(tfAmount, gbc);

        // 第三行：备注（跨列）
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        JLabel remarkLabel = new JLabel("备注：");
        remarkLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(remarkLabel, gbc);
        gbc.gridx = 1; gbc.gridwidth = 5; gbc.weightx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        tfRemark = new JTextField(15);
        tfRemark.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(tfRemark, gbc);

        // 2. 操作按钮区域（FlowLayout，固定高度不挤压）
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        btnPanel.setPreferredSize(new Dimension(0, 50)); // 固定按钮面板高度
        btnAddDetail = createButton("新增明细");
        btnUpdateDetail = createButton("修改明细");
        btnDeleteDetail = createButton("删除明细");
        btnRefreshDetail = createButton("刷新明细");
        btnPanel.add(btnAddDetail);
        btnPanel.add(btnUpdateDetail);
        btnPanel.add(btnDeleteDetail);
        btnPanel.add(btnRefreshDetail);

        // 表单+按钮合并到同边框面板
        detailContainer.add(formPanel);
        detailContainer.add(Box.createVerticalStrut(5)); // 表单与按钮间距
        detailContainer.add(btnPanel);

        // 3. 明细表格区域（独立于边框外，固定高度）
        String[] detailColumns = {"明细ID", "销售单ID", "药品ID", "数量", "单价", "小计", "备注"};
        detailTableModel = new DefaultTableModel(detailColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 表格不可编辑
            }
        };
        detailTable = new JTable(detailTableModel);
        detailTable.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        detailTable.setRowHeight(20);
        JScrollPane tableScroll = new JScrollPane(detailTable);
        tableScroll.setPreferredSize(new Dimension(0, 150)); // 表格固定高度

        // 组装明细面板：合并面板 + 表格
        panel.add(detailContainer);
        panel.add(Box.createVerticalStrut(8));
        panel.add(tableScroll);

        // 绑定明细事件
        bindDetailEvents();
        return panel;
    }

    /**
     * 通用按钮创建：统一样式+固定尺寸，避免按钮变形/消失
     */
    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(80, 30)); // 固定按钮大小
        btn.setMinimumSize(new Dimension(80, 30));   // 最小尺寸，防止压缩
        btn.setFont(new Font("微软雅黑", Font.PLAIN, 10)); // 统一字体
        return btn;
    }

    // ======================== 以下为业务逻辑（兼容原有功能） ========================
    /**
     * 绑定订单事件
     */
    private void bindOrderEvents() {
        // 刷新按钮
        btnRefreshOrder.addActionListener(e -> loadOrderData());
        // 新增按钮
        btnAddOrder.addActionListener(e -> addSalesOrder());
        // 修改按钮
        btnUpdateOrder.addActionListener(e -> updateSalesOrder());
        // 删除按钮
        btnDeleteOrder.addActionListener(e -> deleteSalesOrder());

        // 表格选中事件
        orderTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && orderTable.getSelectedRow() != -1) {
                int selectedRow = orderTable.getSelectedRow();
                tfSalesId.setText(orderTableModel.getValueAt(selectedRow, 0).toString());
                tfCustomerName.setText(orderTableModel.getValueAt(selectedRow, 1).toString());
                // 注意：现在客户电话列已被移除，所以原来索引为2的是销售日期
                tfSalesDate.setText(orderTableModel.getValueAt(selectedRow, 2).toString());
                tfTotalAmount.setText(orderTableModel.getValueAt(selectedRow, 3).toString());
                tfStatus.setText(orderTableModel.getValueAt(selectedRow, 4).toString());
                
                // 自动加载该销售单的明细数据
                loadDetailDataBySalesId(orderTableModel.getValueAt(selectedRow, 0).toString());
            }
        });
    }

    /**
     * 绑定明细事件
     */
    private void bindDetailEvents() {
        // 刷新按钮
        btnRefreshDetail.addActionListener(e -> loadDetailData());
        // 新增按钮
        btnAddDetail.addActionListener(e -> addSalesDetail());
        // 修改按钮
        btnUpdateDetail.addActionListener(e -> updateSalesDetail());
        // 删除按钮
        btnDeleteDetail.addActionListener(e -> deleteSalesDetail());

        // 表格选中事件
        detailTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && detailTable.getSelectedRow() != -1) {
                int row = detailTable.getSelectedRow();
                tfDetailId.setText(detailTableModel.getValueAt(row, 0).toString());

                // 销售单ID下拉框选中
                String salesId = detailTableModel.getValueAt(row, 1).toString();
                for (int i = 0; i < salesIdComboBox.getItemCount(); i++) {
                    String item = salesIdComboBox.getItemAt(i);
                    if (item.startsWith(salesId + "-")) {
                        salesIdComboBox.setSelectedIndex(i);
                        break;
                    }
                }

                // 药品下拉框选中
                String medicineId = detailTableModel.getValueAt(row, 2).toString();
                for (int i = 0; i < medicineComboBox.getItemCount(); i++) {
                    String item = medicineComboBox.getItemAt(i);
                    if (item.startsWith(medicineId + "-")) {
                        medicineComboBox.setSelectedIndex(i);
                        break;
                    }
                }

                tfQuantity.setText(detailTableModel.getValueAt(row, 3).toString());
                tfUnitPrice.setText(detailTableModel.getValueAt(row, 4).toString());
                tfAmount.setText(detailTableModel.getValueAt(row, 5).toString());
                tfRemark.setText(detailTableModel.getValueAt(row, 6).toString());
            }
        });

        // 数量/单价变化自动计算小计（焦点失去时触发）
        tfQuantity.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent e) {
                calculateAmount();
            }
        });
        tfUnitPrice.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent e) {
                calculateAmount();
            }
        });
    }

    /**
     * 计算小计金额
     */
    private void calculateAmount() {
        try {
            String qty = tfQuantity.getText().trim();
            String price = tfUnitPrice.getText().trim();
            if (!qty.isEmpty() && !price.isEmpty()) {
                int quantity = Integer.parseInt(qty);
                BigDecimal unitPrice = new BigDecimal(price);
                BigDecimal amount = unitPrice.multiply(new BigDecimal(quantity));
                tfAmount.setText(amount.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "数量/单价格式错误！", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 加载销售订单数据
     */
    private void loadOrderData() {
        try {
            orderTableModel.setRowCount(0);
            List<SalesOrder> orders = salesOrderDAO.getAllSalesOrders();
            
            // 清空并重新填充销售单ID下拉框
            salesIdComboBox.removeAllItems();
            
            for (SalesOrder order : orders) {
                Object[] rowData = {
                    order.getSalesId(),
                    order.getCustomerName(),
                    order.getSalesDate(),
                    order.getTotalAmount(),
                    order.getStatus(),
                    order.getCreateTime()
                };
                orderTableModel.addRow(rowData);
                
                // 添加到销售单ID下拉框
                salesIdComboBox.addItem(order.getSalesId() + "-" + order.getCustomerName());
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "加载销售订单数据失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 加载销售明细数据
     */
    private void loadDetailData() {
        try {
            detailTableModel.setRowCount(0);
            List<SalesDetail> details = salesDetailDAO.getAllSalesDetails();
            for (SalesDetail detail : details) {
                detailTableModel.addRow(new Object[]{
                        detail.getDetailId(),
                        detail.getSalesId(),
                        detail.getMedicineId(),
                        detail.getQuantity(),
                        detail.getUnitPrice().setScale(2, BigDecimal.ROUND_HALF_UP),
                        detail.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP),
                        detail.getRemark() == null ? "" : detail.getRemark()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "加载明细失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * 根据销售单ID加载明细数据
     */
    private void loadDetailDataBySalesId(String salesId) {
        try {
            detailTableModel.setRowCount(0);
            List<SalesDetail> details = salesDetailDAO.getDetailsBySalesId(Integer.valueOf(salesId));
            
            for (SalesDetail detail : details) {
                detailTableModel.addRow(new Object[]{
                        detail.getDetailId(),
                        detail.getSalesId(),
                        detail.getMedicineId(),
                        detail.getQuantity(),
                        detail.getUnitPrice().setScale(2, BigDecimal.ROUND_HALF_UP),
                        detail.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP),
                        detail.getRemark() == null ? "" : detail.getRemark()
                });
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "加载明细失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 加载药品下拉框
     */
    private void loadMedicineComboBox() {
        try {
            medicineComboBox.removeAllItems();
            List<Medicine> medicines = medicineDAO.getAllMedicines(); // 兼容原有DAO方法，若原方法是getMedicinesByCategory则替换
            for (Medicine m : medicines) {
                medicineComboBox.addItem(m.getMedicineId() + "-" + m.getMedicineName());
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "加载药品失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 新增销售订单
     */
    private void addSalesOrder() {
        try {
            // 获取表单数据
            String customerName = tfCustomerName.getText().trim();
            String salesDateStr = tfSalesDate.getText().trim();
            String totalAmountStr = tfTotalAmount.getText().trim();
            String status = tfStatus.getText().trim();
            
            if (customerName.isEmpty() || salesDateStr.isEmpty() || totalAmountStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请填写完整信息！", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // 封装实体
            SalesOrder order = new SalesOrder();
            order.setCustomerName(customerName);
            order.setSalesDate(Date.valueOf(salesDateStr));
            order.setTotalAmount(new BigDecimal(totalAmountStr));
            order.setStatus(status.isEmpty() ? "待收款" : status);
            order.setCreateTime(new Timestamp(System.currentTimeMillis()));
            
            // 调用DAO新增
            int rows = salesOrderDAO.addSalesOrder(order);
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "新增销售订单成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                loadOrderData();
                clearOrderForm();
            } else {
                JOptionPane.showMessageDialog(this, "新增销售订单失败！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "数据格式不正确！", "错误", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "新增销售订单失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 修改销售订单
     */
    private void updateSalesOrder() {
        try {
            String idStr = tfSalesId.getText().trim();
            if (idStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请选择要修改的销售订单！", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int salesId = Integer.parseInt(idStr);
            
            // 获取表单数据
            String customerName = tfCustomerName.getText().trim();
            String salesDateStr = tfSalesDate.getText().trim();
            String totalAmountStr = tfTotalAmount.getText().trim();
            String status = tfStatus.getText().trim();
            
            if (customerName.isEmpty() || salesDateStr.isEmpty() || totalAmountStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请填写完整信息！", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // 封装实体
            SalesOrder order = new SalesOrder();
            order.setSalesId(salesId);
            order.setCustomerName(customerName);
            order.setSalesDate(Date.valueOf(salesDateStr));
            order.setTotalAmount(new BigDecimal(totalAmountStr));
            order.setStatus(status.isEmpty() ? "待收款" : status);
            
            // 调用DAO修改
            int rows = salesOrderDAO.updateSalesOrder(order);
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "修改销售订单成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                loadOrderData();
                clearOrderForm();
            } else {
                JOptionPane.showMessageDialog(this, "修改销售订单失败！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "数据格式不正确！", "错误", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "修改销售订单失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 删除销售订单
     */
    private void deleteSalesOrder() {
        try {
            String idStr = tfSalesId.getText().trim();
            if (idStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请选择要删除的订单！", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "删除后明细也会删除，确定吗？", "确认", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) return;

            int id = Integer.parseInt(idStr);
            // 先删明细
            salesOrderDAO.executeUpdate("DELETE FROM sales_detail WHERE sales_id=?", id);
            // 再删订单
            if (salesOrderDAO.deleteSalesOrder(id) > 0) {
                JOptionPane.showMessageDialog(this, "删除订单成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                loadOrderData();
                clearOrderForm();
                clearDetailForm();
            } else {
                JOptionPane.showMessageDialog(this, "删除订单失败！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "删除订单异常：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 新增销售明细
     */
    private void addSalesDetail() {
        try {
            String salesIdStr = (String) salesIdComboBox.getSelectedItem();
            String medicineStr = (String) medicineComboBox.getSelectedItem();
            String qty = tfQuantity.getText().trim();
            String price = tfUnitPrice.getText().trim();
            String remark = tfRemark.getText().trim();

            if (salesIdStr == null || medicineStr == null || qty.isEmpty() || price.isEmpty()) {
                JOptionPane.showMessageDialog(this, "销售单/药品/数量/单价不能为空！", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int salesId = Integer.parseInt(salesIdStr.split("-")[0]);
            int medicineId = Integer.parseInt(medicineStr.split("-")[0]);
            int quantity = Integer.parseInt(qty);
            BigDecimal unitPrice = new BigDecimal(price).setScale(2, BigDecimal.ROUND_HALF_UP);
            
            // 自动计算小计金额
            BigDecimal amount = unitPrice.multiply(new BigDecimal(quantity)).setScale(2, BigDecimal.ROUND_HALF_UP);
            tfAmount.setText(amount.toString());

            // 检查库存是否足够
            List<Stock> stocks = stockDAO.getStocksByMedicineId(medicineId);
            int currentStock = 0;
            if (!stocks.isEmpty()) {
                currentStock = stocks.get(0).getCurrentQuantity();
            }
            
            if (currentStock < quantity) {
                JOptionPane.showMessageDialog(this, "库存不足！当前库存：" + currentStock + "，需求数量：" + quantity, "库存不足", JOptionPane.WARNING_MESSAGE);
                return;
            }

            SalesDetail detail = new SalesDetail();
            detail.setSalesId(salesId);
            detail.setMedicineId(medicineId);
            detail.setQuantity(quantity);
            detail.setUnitPrice(unitPrice);
            detail.setRemark(remark);

            if (salesDetailDAO.addSalesDetail(detail) > 0) {
                JOptionPane.showMessageDialog(this, "新增明细成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                loadDetailData();
                // 更新销售订单总金额
                updateSalesOrderTotalAmount(salesId);
                
                // 更新库存（销售出库，减少库存）
                int stockResult = stockDAO.updateStockQuantity(medicineId, -quantity);
                if (stockResult == -1) {
                    JOptionPane.showMessageDialog(this, "警告：库存更新异常！", "警告", JOptionPane.WARNING_MESSAGE);
                }
                
                clearDetailForm();
            } else {
                JOptionPane.showMessageDialog(this, "新增明细失败！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "新增明细异常：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 修改销售明细
     */
    private void updateSalesDetail() {
        try {
            String idStr = tfDetailId.getText().trim();
            if (idStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请选择要修改的明细！", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String salesIdStr = (String) salesIdComboBox.getSelectedItem();
            String medicineStr = (String) medicineComboBox.getSelectedItem();
            String qty = tfQuantity.getText().trim();
            String price = tfUnitPrice.getText().trim();
            String remark = tfRemark.getText().trim();

            if (salesIdStr == null || medicineStr == null || qty.isEmpty() || price.isEmpty()) {
                JOptionPane.showMessageDialog(this, "销售单/药品/数量/单价不能为空！", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int id = Integer.parseInt(idStr);
            int salesId = Integer.parseInt(salesIdStr.split("-")[0]);
            int medicineId = Integer.parseInt(medicineStr.split("-")[0]);
            int quantity = Integer.parseInt(qty);
            BigDecimal unitPrice = new BigDecimal(price).setScale(2, BigDecimal.ROUND_HALF_UP);
            
            // 自动计算小计金额
            BigDecimal amount = unitPrice.multiply(new BigDecimal(quantity)).setScale(2, BigDecimal.ROUND_HALF_UP);
            tfAmount.setText(amount.toString());

            // 检查库存是否足够
            List<Stock> stocks = stockDAO.getStocksByMedicineId(medicineId);
            int currentStock = 0;
            if (!stocks.isEmpty()) {
                currentStock = stocks.get(0).getCurrentQuantity();
            }
            
            if (currentStock < quantity) {
                JOptionPane.showMessageDialog(this, "库存不足！当前库存：" + currentStock + "，需求数量：" + quantity, "库存不足", JOptionPane.WARNING_MESSAGE);
                return;
            }

            SalesDetail detail = new SalesDetail();
            detail.setDetailId(id);
            detail.setSalesId(salesId);
            detail.setMedicineId(medicineId);
            detail.setQuantity(quantity);
            detail.setUnitPrice(unitPrice);
            detail.setRemark(remark);

            int result = salesDetailDAO.updateSalesDetail(detail);
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "修改明细成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                loadDetailData();
                // 更新销售订单总金额
                updateSalesOrderTotalAmount(salesId);
                
                // 更新库存（销售出库，减少库存）
                int stockResult = stockDAO.updateStockQuantity(medicineId, -quantity);
                if (stockResult == -1) {
                    JOptionPane.showMessageDialog(this, "警告：库存更新异常！", "警告", JOptionPane.WARNING_MESSAGE);
                }
                
                clearDetailForm();
            } else {
                JOptionPane.showMessageDialog(this, "修改明细失败！可能是没有符合条件的记录。", "错误", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "修改明细异常：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    /**
     * 删除销售明细
     */
    private void deleteSalesDetail() {
        try {
            String idStr = tfDetailId.getText().trim();
            if (idStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请选择要删除的明细！", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "确定删除该明细吗？", "确认", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) return;

            int id = Integer.parseInt(idStr);
            
            // 获取销售单ID用于后续更新总金额
            int salesId = 0;
            for (int i = 0; i < detailTableModel.getRowCount(); i++) {
                if (detailTableModel.getValueAt(i, 0).toString().equals(idStr)) {
                    salesId = Integer.parseInt(detailTableModel.getValueAt(i, 1).toString());
                    break;
                }
            }

            if (salesDetailDAO.deleteSalesDetail(id) > 0) {
                JOptionPane.showMessageDialog(this, "删除明细成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                loadDetailData();
                // 更新销售订单总金额
                if (salesId > 0) {
                    updateSalesOrderTotalAmount(salesId);
                }
                clearDetailForm();
            } else {
                JOptionPane.showMessageDialog(this, "删除明细失败！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "删除明细异常：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 更新销售订单总金额
     */
    private void updateSalesOrderTotalAmount(int salesId) {
        try {
            // 查询该销售单的所有明细
            List<SalesDetail> details = salesDetailDAO.getDetailsBySalesId(salesId);
            
            // 计算总金额
            BigDecimal totalAmount = BigDecimal.ZERO;
            for (SalesDetail detail : details) {
                totalAmount = totalAmount.add(detail.getAmount());
            }
            
            // 更新销售订单总金额
            String sql = "UPDATE sales_order SET total_amount=? WHERE sales_id=?";
            salesOrderDAO.executeUpdate(sql, totalAmount, salesId);
            
            // 刷新订单数据
            loadOrderData();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "更新销售订单总金额失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 清空订单表单
     */
    private void clearOrderForm() {
        tfSalesId.setText("");
        tfCustomerName.setText("");
        tfSalesDate.setText("");
        tfTotalAmount.setText("");
        tfStatus.setText("待收款");
    }

    /**
     * 清空明细表单
     */
    private void clearDetailForm() {
        tfDetailId.setText("");
        salesIdComboBox.setSelectedIndex(-1);
        medicineComboBox.setSelectedIndex(-1);
        tfQuantity.setText("");
        tfUnitPrice.setText("");
        tfAmount.setText("");
        tfRemark.setText("");
    }
}