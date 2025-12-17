package com.dayu.system.view;

import com.dayu.system.dao.impl.MedicineDAO;
import com.dayu.system.dao.impl.PurchaseOrderDAO;
import com.dayu.system.dao.impl.PurchaseDetailDAO;
import com.dayu.system.dao.impl.SupplierDAO;
import com.dayu.system.dao.impl.StockDAO;
import com.dayu.system.entity.Medicine;
import com.dayu.system.entity.PurchaseOrder;
import com.dayu.system.entity.PurchaseDetail;
import com.dayu.system.entity.Supplier;
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
 * 采购与入库管理面板（最终版：表单+按钮同框+布局不挤压+按钮完全可见）
 */
public class PurchasePanel extends JPanel {
    // DAO对象
    private final PurchaseOrderDAO purchaseOrderDAO = new PurchaseOrderDAO();
    private final PurchaseDetailDAO purchaseDetailDAO = new PurchaseDetailDAO();
    private final SupplierDAO supplierDAO = new SupplierDAO();
    private final MedicineDAO medicineDAO = new MedicineDAO();
    private final StockDAO stockDAO = new StockDAO();

    // 组件声明
    private JTable orderTable, detailTable;
    private DefaultTableModel orderTableModel, detailTableModel;
    private JTextField tfPurchaseId, tfPurchaseDate, tfTotalAmount, tfStatus;
    private JTextField tfDetailId, tfQuantity, tfUnitPrice, tfAmount, tfRemark;
    private JComboBox<String> medicineComboBox, purchaseIdComboBox, supplierComboBox;
    private JButton btnAddOrder, btnUpdateOrder, btnDeleteOrder, btnRefreshOrder;
    private JButton btnAddDetail, btnUpdateDetail, btnDeleteDetail, btnRefreshDetail;

    public PurchasePanel() {
        // 设置布局和边距
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 创建顶部订单管理面板（优化布局）
        JPanel orderPanel = createOrderPanel();
        // 创建底部明细管理面板（优化布局）
        JPanel detailPanel = createDetailPanel();

        // 创建分割面板
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, orderPanel, detailPanel);
        splitPane.setDividerLocation(300);
        splitPane.setResizeWeight(0.5);
        splitPane.setOneTouchExpandable(true);
        add(splitPane, BorderLayout.CENTER);

        // 加载初始数据
        loadOrderData();
        loadDetailData();  // 添加这一行以在界面初始化时加载明细数据
        loadSupplierComboBox();
        loadMedicineComboBox();
    }

    /**
     * 创建采购订单管理面板（按钮与表单在同一边框内）
     */
    private JPanel createOrderPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // 订单表单与按钮组合面板（同一边框内）
        JPanel orderContainer = new JPanel();
        TitledBorder orderBorder = BorderFactory.createTitledBorder("采购订单信息");
        orderBorder.setTitleFont(new Font("微软雅黑", Font.PLAIN, 12));
        orderContainer.setBorder(orderBorder);
        orderContainer.setLayout(new BorderLayout()); // 使用BorderLayout布局

        // 左侧表单区域
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // 第一行
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel purchaseIdLabel = new JLabel("采购单ID：");
        purchaseIdLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(purchaseIdLabel, gbc);

        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        tfPurchaseId = new JTextField(20);
        tfPurchaseId.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(tfPurchaseId, gbc);

        gbc.gridx = 2; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        JLabel supplierLabel = new JLabel("供应商：");
        supplierLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(supplierLabel, gbc);

        gbc.gridx = 3; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        supplierComboBox = new JComboBox<>();
        supplierComboBox.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(supplierComboBox, gbc);

        // 第二行
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        JLabel purchaseDateLabel = new JLabel("采购日期：");
        purchaseDateLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(purchaseDateLabel, gbc);

        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        tfPurchaseDate = new JTextField(20);
        tfPurchaseDate.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(tfPurchaseDate, gbc);

        gbc.gridx = 2; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        JLabel totalAmountLabel = new JLabel("总金额：");
        totalAmountLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(totalAmountLabel, gbc);

        gbc.gridx = 3; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        tfTotalAmount = new JTextField(20);
        tfTotalAmount.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(tfTotalAmount, gbc);

        // 第三行
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        JLabel statusLabel = new JLabel("状态：");
        statusLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(statusLabel, gbc);

        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        tfStatus = new JTextField("待入库", 20);
        tfStatus.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(tfStatus, gbc);

        // 右侧按钮区域
        JPanel orderBtnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        orderBtnPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // 设置按钮样式
        Dimension buttonSize = new Dimension(80, 30);
        btnAddOrder = createButton("新增订单", buttonSize);
        btnUpdateOrder = createButton("修改订单", buttonSize);
        btnDeleteOrder = createButton("删除订单", buttonSize);
        btnRefreshOrder = createButton("刷新订单", buttonSize);

        // 按钮垂直排列
        orderBtnPanel.setLayout(new BoxLayout(orderBtnPanel, BoxLayout.Y_AXIS));
        orderBtnPanel.add(btnAddOrder);
        orderBtnPanel.add(Box.createVerticalStrut(8));
        orderBtnPanel.add(btnUpdateOrder);
        orderBtnPanel.add(Box.createVerticalStrut(8));
        orderBtnPanel.add(btnDeleteOrder);
        orderBtnPanel.add(Box.createVerticalStrut(8));
        orderBtnPanel.add(btnRefreshOrder);

        // 将表单和按钮添加到容器面板
        orderContainer.add(formPanel, BorderLayout.CENTER);
        orderContainer.add(orderBtnPanel, BorderLayout.EAST);
        orderContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        // 订单表格面板
        String[] orderColumnNames = {"采购单ID", "供应商ID", "采购日期", "总金额", "状态", "创建时间"};
        orderTableModel = new DefaultTableModel(orderColumnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        orderTable = new JTable(orderTableModel);
        orderTable.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        orderTable.setRowHeight(20);
        JScrollPane orderScrollPane = new JScrollPane(orderTable);
        orderScrollPane.setPreferredSize(new Dimension(Integer.MAX_VALUE, 100));
        orderScrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        // 组装订单面板
        panel.add(orderContainer);
        panel.add(Box.createVerticalStrut(8));
        panel.add(orderScrollPane);

        // 绑定事件
        bindOrderEvents();

        return panel;
    }

    /**
     * 创建采购明细管理面板（按钮与表单在同一边框内）
     */
    private JPanel createDetailPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // 明细表单与按钮组合面板（同一边框内）
        JPanel detailContainer = new JPanel();
        TitledBorder detailBorder = BorderFactory.createTitledBorder("采购明细信息");
        detailBorder.setTitleFont(new Font("微软雅黑", Font.PLAIN, 12));
        detailContainer.setBorder(detailBorder);
        detailContainer.setLayout(new BorderLayout()); // 使用BorderLayout布局

        // 左侧表单区域
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // 第一行
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel detailIdLabel = new JLabel("明细ID：");
        detailIdLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(detailIdLabel, gbc);

        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        tfDetailId = new JTextField(15);
        tfDetailId.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(tfDetailId, gbc);

        gbc.gridx = 2; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        JLabel purchaseIdLabel = new JLabel("采购单ID：");
        purchaseIdLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(purchaseIdLabel, gbc);

        gbc.gridx = 3; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        purchaseIdComboBox = new JComboBox<>();
        purchaseIdComboBox.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(purchaseIdComboBox, gbc);

        gbc.gridx = 4; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        JLabel medicineLabel = new JLabel("药品：");
        medicineLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(medicineLabel, gbc);

        gbc.gridx = 5; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        medicineComboBox = new JComboBox<>();
        medicineComboBox.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(medicineComboBox, gbc);

        // 第二行
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        JLabel quantityLabel = new JLabel("数量：");
        quantityLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(quantityLabel, gbc);

        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        tfQuantity = new JTextField(15);
        tfQuantity.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(tfQuantity, gbc);

        gbc.gridx = 2; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        JLabel unitPriceLabel = new JLabel("单价：");
        unitPriceLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(unitPriceLabel, gbc);

        gbc.gridx = 3; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        tfUnitPrice = new JTextField(15);
        tfUnitPrice.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(tfUnitPrice, gbc);

        gbc.gridx = 4; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        JLabel amountLabel = new JLabel("小计：");
        amountLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(amountLabel, gbc);

        gbc.gridx = 5; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        tfAmount = new JTextField(15);
        tfAmount.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(tfAmount, gbc);

        // 第三行
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        JLabel remarkLabel = new JLabel("备注：");
        remarkLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(remarkLabel, gbc);

        gbc.gridx = 1; gbc.gridwidth = 5; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        tfRemark = new JTextField(15);
        tfRemark.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(tfRemark, gbc);

        // 右侧按钮区域
        JPanel detailBtnPanel = new JPanel();
        detailBtnPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        detailBtnPanel.setLayout(new BoxLayout(detailBtnPanel, BoxLayout.Y_AXIS));

        // 设置按钮样式
        Dimension buttonSize = new Dimension(80, 30);
        btnAddDetail = createButton("新增明细", buttonSize);
        btnUpdateDetail = createButton("修改明细", buttonSize);
        btnDeleteDetail = createButton("删除明细", buttonSize);
        btnRefreshDetail = createButton("刷新明细", buttonSize);

        // 添加按钮并设置间距
        detailBtnPanel.add(btnAddDetail);
        detailBtnPanel.add(Box.createVerticalStrut(8));
        detailBtnPanel.add(btnUpdateDetail);
        detailBtnPanel.add(Box.createVerticalStrut(8));
        detailBtnPanel.add(btnDeleteDetail);
        detailBtnPanel.add(Box.createVerticalStrut(8));
        detailBtnPanel.add(btnRefreshDetail);

        // 将表单和按钮添加到容器面板
        detailContainer.add(formPanel, BorderLayout.CENTER);
        detailContainer.add(detailBtnPanel, BorderLayout.EAST);
        detailContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        // 明细表格面板
        String[] detailColumnNames = {"明细ID", "采购单ID", "药品ID", "数量", "单价", "小计", "备注"};
        detailTableModel = new DefaultTableModel(detailColumnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        detailTable = new JTable(detailTableModel);
        detailTable.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        detailTable.setRowHeight(20);
        JScrollPane detailScrollPane = new JScrollPane(detailTable);
        detailScrollPane.setPreferredSize(new Dimension(Integer.MAX_VALUE, 100));
        detailScrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        // 组装明细面板
        panel.add(detailContainer);
        panel.add(Box.createVerticalStrut(8));
        panel.add(detailScrollPane);

        // 绑定事件
        bindDetailEvents();

        return panel;
    }

    /**
     * 通用按钮创建方法
     */
    private JButton createButton(String text, Dimension size) {
        JButton button = new JButton(text);
        button.setPreferredSize(size);
        button.setMinimumSize(size);
        button.setFont(new Font("微软雅黑", Font.PLAIN, 10));
        return button;
    }

    // 以下为原有事件绑定和业务逻辑代码（保持不变）
    private void bindOrderEvents() {
        // 刷新按钮
        btnRefreshOrder.addActionListener(e -> loadOrderData());

        // 新增按钮
        btnAddOrder.addActionListener(e -> addPurchaseOrder());

        // 修改按钮
        btnUpdateOrder.addActionListener(e -> updatePurchaseOrder());

        // 删除按钮
        btnDeleteOrder.addActionListener(e -> deletePurchaseOrder());

        // 表格选中事件
        orderTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && orderTable.getSelectedRow() != -1) {
                int selectedRow = orderTable.getSelectedRow();
                tfPurchaseId.setText(orderTableModel.getValueAt(selectedRow, 0).toString());
                tfPurchaseDate.setText(orderTableModel.getValueAt(selectedRow, 2).toString());
                tfTotalAmount.setText(orderTableModel.getValueAt(selectedRow, 3).toString());
                tfStatus.setText(orderTableModel.getValueAt(selectedRow, 4).toString());

                // 补充供应商下拉框选中逻辑
                String supplierId = orderTableModel.getValueAt(selectedRow, 1).toString();
                for (int i = 0; i < supplierComboBox.getItemCount(); i++) {
                    String item = supplierComboBox.getItemAt(i);
                    if (item.startsWith(supplierId + "-")) {
                        supplierComboBox.setSelectedIndex(i);
                        break;
                    }
                }
                
                // 自动加载该采购单的明细数据
                loadDetailDataByPurchaseId(orderTableModel.getValueAt(selectedRow, 0).toString());
            }
        });
    }

    private void bindDetailEvents() {
        // 刷新按钮
        btnRefreshDetail.addActionListener(e -> loadDetailData());

        // 新增按钮
        btnAddDetail.addActionListener(e -> addPurchaseDetail());

        // 修改按钮
        btnUpdateDetail.addActionListener(e -> updatePurchaseDetail());

        // 删除按钮
        btnDeleteDetail.addActionListener(e -> deletePurchaseDetail());

        // 表格选中事件
        detailTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && detailTable.getSelectedRow() != -1) {
                int selectedRow = detailTable.getSelectedRow();
                tfDetailId.setText(detailTableModel.getValueAt(selectedRow, 0).toString());

                // 采购单ID下拉框选中
                String purchaseId = detailTableModel.getValueAt(selectedRow, 1).toString();
                for (int i = 0; i < purchaseIdComboBox.getItemCount(); i++) {
                    String item = purchaseIdComboBox.getItemAt(i);
                    if (item.startsWith(purchaseId + "-")) {
                        purchaseIdComboBox.setSelectedIndex(i);
                        break;
                    }
                }

                // 药品下拉框选中
                String medicineId = detailTableModel.getValueAt(selectedRow, 2).toString();
                for (int i = 0; i < medicineComboBox.getItemCount(); i++) {
                    String item = medicineComboBox.getItemAt(i);
                    if (item.startsWith(medicineId + "-")) {
                        medicineComboBox.setSelectedIndex(i);
                        break;
                    }
                }

                tfQuantity.setText(detailTableModel.getValueAt(selectedRow, 3).toString());
                tfUnitPrice.setText(detailTableModel.getValueAt(selectedRow, 4).toString());
                tfAmount.setText(detailTableModel.getValueAt(selectedRow, 5).toString());
                tfRemark.setText(detailTableModel.getValueAt(selectedRow, 6).toString());
            }
        });

        // 数量或单价变化时自动计算小计
        tfQuantity.addActionListener(e -> calculateAmount());
        tfUnitPrice.addActionListener(e -> calculateAmount());
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

    private void calculateAmount() {
        try {
            String quantityStr = tfQuantity.getText().trim();
            String unitPriceStr = tfUnitPrice.getText().trim();

            if (!quantityStr.isEmpty() && !unitPriceStr.isEmpty()) {
                int quantity = Integer.parseInt(quantityStr);
                BigDecimal unitPrice = new BigDecimal(unitPriceStr);
                BigDecimal amount = unitPrice.multiply(new BigDecimal(quantity));
                tfAmount.setText(amount.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "数量或单价格式不正确！请输入数字", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadOrderData() {
        try {
            orderTableModel.setRowCount(0);
            List<PurchaseOrder> orders = purchaseOrderDAO.getAllPurchaseOrders();

            // 清空并重新填充采购单ID下拉框
            purchaseIdComboBox.removeAllItems();

            for (PurchaseOrder order : orders) {
                Object[] rowData = {
                    order.getPurchaseId(),
                    order.getSupplierId(),
                    order.getPurchaseDate(),
                    order.getTotalAmount().setScale(2, BigDecimal.ROUND_HALF_UP),
                    order.getStatus(),
                    order.getCreateTime()
                };
                orderTableModel.addRow(rowData);

                // 添加到采购单ID下拉框
                purchaseIdComboBox.addItem(order.getPurchaseId() + "-" + order.getPurchaseDate());
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "加载采购订单数据失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadDetailData() {
        try {
            detailTableModel.setRowCount(0);
            List<PurchaseDetail> details = purchaseDetailDAO.getAllPurchaseDetails();

            for (PurchaseDetail detail : details) {
                Object[] rowData = {
                    detail.getPurchaseDetailId(),
                    detail.getPurchaseId(),
                    detail.getMedicineId(),
                    detail.getQuantity(),
                    detail.getUnitPrice().setScale(2, BigDecimal.ROUND_HALF_UP),
                    detail.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP),
                    detail.getRemark() != null ? detail.getRemark() : ""
                };
                detailTableModel.addRow(rowData);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "加载采购明细数据失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 根据采购单ID加载明细数据
     */
    private void loadDetailDataByPurchaseId(String purchaseId) {
        try {
            detailTableModel.setRowCount(0);
            List<PurchaseDetail> details = purchaseDetailDAO.getDetailsByPurchaseId(Integer.valueOf(purchaseId));

            for (PurchaseDetail detail : details) {
                Object[] rowData = {
                    detail.getPurchaseDetailId(),
                    detail.getPurchaseId(),
                    detail.getMedicineId(),
                    detail.getQuantity(),
                    detail.getUnitPrice().setScale(2, BigDecimal.ROUND_HALF_UP),
                    detail.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP),
                    detail.getRemark() != null ? detail.getRemark() : ""
                };
                detailTableModel.addRow(rowData);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "加载采购明细数据失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadSupplierComboBox() {
        try {
            supplierComboBox.removeAllItems();
            List<Supplier> suppliers = supplierDAO.getAllSuppliers();
            for (Supplier supplier : suppliers) {
                supplierComboBox.addItem(supplier.getSupplierId() + "-" + supplier.getSupplierName());
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "加载供应商数据失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadMedicineComboBox() {
        try {
            medicineComboBox.removeAllItems();
            List<Medicine> medicines = medicineDAO.getAllMedicines();
            for (Medicine medicine : medicines) {
                medicineComboBox.addItem(medicine.getMedicineId() + "-" + medicine.getMedicineName());
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "加载药品数据失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addPurchaseOrder() {
        try {
            // 获取表单数据
            String supplierStr = (String) supplierComboBox.getSelectedItem();
            if (supplierStr == null || supplierStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请选择供应商！", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int supplierId = Integer.parseInt(supplierStr.split("-")[0]);
            String purchaseDateStr = tfPurchaseDate.getText().trim();
            String totalAmountStr = tfTotalAmount.getText().trim();
            String status = tfStatus.getText().trim();

            if (purchaseDateStr.isEmpty() || totalAmountStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "采购日期和总金额不能为空！", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 验证日期格式
            Date purchaseDate;
            try {
                purchaseDate = Date.valueOf(purchaseDateStr);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, "日期格式不正确！请使用yyyy-MM-dd格式", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 封装实体
            PurchaseOrder order = new PurchaseOrder();
            order.setSupplierId(supplierId);
            order.setPurchaseDate(purchaseDate);
            order.setTotalAmount(new BigDecimal(totalAmountStr).setScale(2, BigDecimal.ROUND_HALF_UP));
            order.setStatus(status.isEmpty() ? "待入库" : status);
            order.setCreateTime(new Timestamp(System.currentTimeMillis()));

            // 调用DAO新增
            int rows = purchaseOrderDAO.addPurchaseOrder(order);
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "新增采购订单成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                loadOrderData();
                clearOrderForm();
            } else {
                JOptionPane.showMessageDialog(this, "新增采购订单失败！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "总金额格式不正确！请输入数字", "错误", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "新增采购订单失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updatePurchaseOrder() {
        try {
            String idStr = tfPurchaseId.getText().trim();
            if (idStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请选择要修改的采购订单！", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int purchaseId = Integer.parseInt(idStr);

            // 获取表单数据
            String supplierStr = (String) supplierComboBox.getSelectedItem();
            if (supplierStr == null || supplierStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请选择供应商！", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int supplierId = Integer.parseInt(supplierStr.split("-")[0]);
            String purchaseDateStr = tfPurchaseDate.getText().trim();
            String totalAmountStr = tfTotalAmount.getText().trim();
            String status = tfStatus.getText().trim();

            if (purchaseDateStr.isEmpty() || totalAmountStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "采购日期和总金额不能为空！", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 验证日期格式
            Date purchaseDate;
            try {
                purchaseDate = Date.valueOf(purchaseDateStr);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, "日期格式不正确！请使用yyyy-MM-dd格式", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 封装实体
            PurchaseOrder order = new PurchaseOrder();
            order.setPurchaseId(purchaseId);
            order.setSupplierId(supplierId);
            order.setPurchaseDate(purchaseDate);
            order.setTotalAmount(new BigDecimal(totalAmountStr).setScale(2, BigDecimal.ROUND_HALF_UP));
            order.setStatus(status.isEmpty() ? "待入库" : status);

            // 调用DAO修改
            int rows = purchaseOrderDAO.updatePurchaseOrder(order);
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "修改采购订单成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                loadOrderData();
                clearOrderForm();
            } else {
                JOptionPane.showMessageDialog(this, "修改采购订单失败！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "采购单ID或总金额格式不正确！", "错误", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "修改采购订单失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deletePurchaseOrder() {
        try {
            String idStr = tfPurchaseId.getText().trim();
            if (idStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请选择要删除的采购订单！", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "确定要删除该采购订单吗？删除后相关明细也会被删除！", "确认删除", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            int purchaseId = Integer.parseInt(idStr);

            // 先删除明细
            String deleteDetailSql = "DELETE FROM purchase_detail WHERE purchase_id=?";
            purchaseOrderDAO.executeUpdate(deleteDetailSql, purchaseId);

            // 再删除订单
            int rows = purchaseOrderDAO.deletePurchaseOrder(purchaseId);

            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "删除采购订单成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                loadOrderData();
                clearOrderForm();
                clearDetailForm();
            } else {
                JOptionPane.showMessageDialog(this, "删除采购订单失败！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "采购单ID格式不正确！请输入数字", "错误", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "删除采购订单失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addPurchaseDetail() {
        try {
            // 获取表单数据
            String purchaseIdStr = (String) purchaseIdComboBox.getSelectedItem();
            String medicineStr = (String) medicineComboBox.getSelectedItem();
            String quantityStr = tfQuantity.getText().trim();
            String unitPriceStr = tfUnitPrice.getText().trim();
            String remark = tfRemark.getText().trim();

            if (purchaseIdStr == null || purchaseIdStr.isEmpty() ||
                medicineStr == null || medicineStr.isEmpty() ||
                quantityStr.isEmpty() || unitPriceStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "采购单、药品、数量、单价不能为空！", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int purchaseId = Integer.parseInt(purchaseIdStr.split("-")[0]);
            int medicineId = Integer.parseInt(medicineStr.split("-")[0]);
            int quantity = Integer.parseInt(quantityStr);
            BigDecimal unitPrice = new BigDecimal(unitPriceStr).setScale(2, BigDecimal.ROUND_HALF_UP);
            
            // 自动计算小计金额
            BigDecimal amount = unitPrice.multiply(new BigDecimal(quantity)).setScale(2, BigDecimal.ROUND_HALF_UP);
            tfAmount.setText(amount.toString());

            // 封装实体
            PurchaseDetail detail = new PurchaseDetail();
            detail.setPurchaseId(purchaseId);
            detail.setMedicineId(medicineId);
            detail.setQuantity(quantity);
            detail.setUnitPrice(unitPrice);
            detail.setRemark(remark);

            // 调用DAO新增
            int rows = purchaseDetailDAO.addPurchaseDetail(detail);
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "新增采购明细成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                loadDetailData();
                // 更新采购订单总金额
                updatePurchaseOrderTotalAmount(purchaseId);
                
                // 更新库存（采购入库，增加库存）
                int stockResult = stockDAO.updateStockQuantity(medicineId, quantity);
                if (stockResult == -1) {
                    JOptionPane.showMessageDialog(this, "警告：库存更新异常！", "警告", JOptionPane.WARNING_MESSAGE);
                }
                
                clearDetailForm();
            } else {
                JOptionPane.showMessageDialog(this, "新增采购明细失败！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "数量、单价格式不正确！请输入数字", "错误", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "新增采购明细失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updatePurchaseDetail() {
        try {
            String detailIdStr = tfDetailId.getText().trim();
            if (detailIdStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请选择要修改的采购明细！", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 获取表单数据
            String purchaseIdStr = (String) purchaseIdComboBox.getSelectedItem();
            String medicineStr = (String) medicineComboBox.getSelectedItem();
            String quantityStr = tfQuantity.getText().trim();
            String unitPriceStr = tfUnitPrice.getText().trim();
            String remark = tfRemark.getText().trim();

            if (purchaseIdStr == null || purchaseIdStr.isEmpty() ||
                medicineStr == null || medicineStr.isEmpty() ||
                quantityStr.isEmpty() || unitPriceStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "采购单、药品、数量、单价不能为空！", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int detailId = Integer.parseInt(detailIdStr);
            int purchaseId = Integer.parseInt(purchaseIdStr.split("-")[0]);
            int medicineId = Integer.parseInt(medicineStr.split("-")[0]);
            int quantity = Integer.parseInt(quantityStr);
            BigDecimal unitPrice = new BigDecimal(unitPriceStr).setScale(2, BigDecimal.ROUND_HALF_UP);
            
            // 自动计算小计金额
            BigDecimal amount = unitPrice.multiply(new BigDecimal(quantity)).setScale(2, BigDecimal.ROUND_HALF_UP);
            tfAmount.setText(amount.toString());

            // 封装实体
            PurchaseDetail detail = new PurchaseDetail();
            detail.setPurchaseDetailId(detailId);
            detail.setPurchaseId(purchaseId);
            detail.setMedicineId(medicineId);
            detail.setQuantity(quantity);
            detail.setUnitPrice(unitPrice);
            detail.setRemark(remark);

            // 调用DAO修改
            int rows = purchaseDetailDAO.updatePurchaseDetail(detail);
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "修改采购明细成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                loadDetailData();
                // 更新采购订单总金额
                updatePurchaseOrderTotalAmount(purchaseId);
                
                // 更新库存（采购入库，增加库存）
                int stockResult = stockDAO.updateStockQuantity(medicineId, quantity);
                if (stockResult == -1) {
                    JOptionPane.showMessageDialog(this, "警告：库存更新异常！", "警告", JOptionPane.WARNING_MESSAGE);
                }
                
                clearDetailForm();
            } else {
                JOptionPane.showMessageDialog(this, "修改采购明细失败！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "明细ID、数量、单价格式不正确！", "错误", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "修改采购明细失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deletePurchaseDetail() {
        try {
            String detailIdStr = tfDetailId.getText().trim();
            if (detailIdStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请选择要删除的采购明细！", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "确定要删除该采购明细吗？", "确认删除", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            int detailId = Integer.parseInt(detailIdStr);
            
            // 获取采购单ID用于后续更新总金额
            int purchaseId = 0;
            for (int i = 0; i < detailTableModel.getRowCount(); i++) {
                if (detailTableModel.getValueAt(i, 0).toString().equals(detailIdStr)) {
                    purchaseId = Integer.parseInt(detailTableModel.getValueAt(i, 1).toString());
                    break;
                }
            }

            int rows = purchaseDetailDAO.deletePurchaseDetail(detailId);

            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "删除采购明细成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                loadDetailData();
                // 更新采购订单总金额
                if (purchaseId > 0) {
                    updatePurchaseOrderTotalAmount(purchaseId);
                }
                clearDetailForm();
            } else {
                JOptionPane.showMessageDialog(this, "删除采购明细失败！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "明细ID格式不正确！请输入数字", "错误", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "删除采购明细失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearOrderForm() {
        tfPurchaseId.setText("");
        supplierComboBox.setSelectedIndex(-1);
        tfPurchaseDate.setText("");
        tfTotalAmount.setText("");
        tfStatus.setText("待入库");
    }

    private void clearDetailForm() {
        tfDetailId.setText("");
        purchaseIdComboBox.setSelectedIndex(-1);
        medicineComboBox.setSelectedIndex(-1);
        tfQuantity.setText("");
        tfUnitPrice.setText("");
        tfAmount.setText("");
        tfRemark.setText("");
    }

    /**
     * 更新采购订单总金额
     */
    private void updatePurchaseOrderTotalAmount(int purchaseId) {
        try {
            // 查询该采购单的所有明细
            List<PurchaseDetail> details = purchaseDetailDAO.getDetailsByPurchaseId(purchaseId);
            
            // 计算总金额
            BigDecimal totalAmount = BigDecimal.ZERO;
            for (PurchaseDetail detail : details) {
                totalAmount = totalAmount.add(detail.getAmount());
            }
            
            // 更新采购订单总金额
            PurchaseOrder order = new PurchaseOrder();
            order.setPurchaseId(purchaseId);
            order.setTotalAmount(totalAmount);
            
            String sql = "UPDATE purchase_order SET total_amount=? WHERE purchase_id=?";
            purchaseOrderDAO.executeUpdate(sql, totalAmount, purchaseId);
            
            // 刷新订单数据
            loadOrderData();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "更新采购订单总金额失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
}