package com.dayu.system.view;

import com.dayu.system.dao.impl.MedicineDAO;
import com.dayu.system.dao.impl.SupplierDAO;
import com.dayu.system.entity.Medicine;
import com.dayu.system.entity.Supplier;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

/**
 * 药品与供应商管理面板：增删改查功能
 */
public class MedAndSuppPanel extends JPanel {
    // DAO对象
    private final MedicineDAO medicineDAO = new MedicineDAO();
    private final SupplierDAO supplierDAO = new SupplierDAO();
    // 组件声明
    private JTable medicineTable;
    private JTable supplierTable;
    private DefaultTableModel medicineTableModel;
    private DefaultTableModel supplierTableModel;
    private JTextField tfMedicineId, tfMedicineName, tfSpecification, tfUnit, tfManufacturer, tfCategory, tfPrice, tfStatus;
    private JTextField tfSupplierId, tfSupplierName, tfContact, tfPhone, tfAddress, tfSupplierStatus;
    
    // 切换面板相关组件
    private CardLayout cardLayout;
    private JPanel mainCardPanel;
    private JButton medManageButton;
    private JButton suppManageButton;

    public MedAndSuppPanel() {
        // 1. 面板布局（BorderLayout）
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 创建切换按钮面板
        JPanel switchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        medManageButton = new JButton("药品管理");
        medManageButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        suppManageButton = new JButton("供应商管理");
        suppManageButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        switchPanel.add(medManageButton);
        switchPanel.add(suppManageButton);
        
        // 设置按钮样式
        setupButtonStyle(medManageButton, true);   // 药品管理按钮默认选中
        setupButtonStyle(suppManageButton, false); // 供应商管理按钮默认未选中

        // 创建卡片布局的主面板
        cardLayout = new CardLayout();
        mainCardPanel = new JPanel(cardLayout);
        
        // 创建药品管理面板
        JPanel medicinePanel = createMedicinePanel();
        
        // 创建供应商管理面板
        JPanel supplierPanel = createSupplierPanel();
        
        // 将面板添加到卡片布局中
        mainCardPanel.add(medicinePanel, "MEDICINE_PANEL");
        mainCardPanel.add(supplierPanel, "SUPPLIER_PANEL");

        // 组装面板
        add(switchPanel, BorderLayout.NORTH);
        add(mainCardPanel, BorderLayout.CENTER);

        // 6. 加载初始数据
        loadMedicineData(null);
        loadSupplierData(null);

        // 按钮事件绑定
        medManageButton.addActionListener(e -> {
            showMedicinePanel();
        });
        
        suppManageButton.addActionListener(e -> {
            showSupplierPanel();
        });
    }
    
    // 显示药品管理面板
    private void showMedicinePanel() {
        cardLayout.show(mainCardPanel, "MEDICINE_PANEL");
        setupButtonStyle(medManageButton, true);
        setupButtonStyle(suppManageButton, false);
    }
    
    // 显示供应商管理面板
    private void showSupplierPanel() {
        cardLayout.show(mainCardPanel, "SUPPLIER_PANEL");
        setupButtonStyle(medManageButton, false);
        setupButtonStyle(suppManageButton, true);
    }
    
    // 创建药品管理面板
    private JPanel createMedicinePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        
        // 顶部查询&操作区
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        // 查询面板
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel searchLabel = new JLabel("药品名称：");
        searchLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        searchPanel.add(searchLabel);
        JTextField tfSearchName = new JTextField(20);
        tfSearchName.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        JButton btnSearch = new JButton("查询");
        btnSearch.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        searchPanel.add(tfSearchName);
        searchPanel.add(btnSearch);
        // 操作按钮面板
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAdd = new JButton("新增");
        btnAdd.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        JButton btnUpdate = new JButton("修改");
        btnUpdate.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        JButton btnDelete = new JButton("删除");
        btnDelete.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        JButton btnRefresh = new JButton("刷新");
        btnRefresh.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnRefresh);
        // 组装顶部面板
        topPanel.add(searchPanel, BorderLayout.WEST);
        topPanel.add(btnPanel, BorderLayout.EAST);

        // 中间表单录入区
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // 表单字段
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel medIdLabel = new JLabel("药品ID：");
        medIdLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(medIdLabel, gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        tfMedicineId = new JTextField(20);
        tfMedicineId.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(tfMedicineId, gbc);
        
        gbc.gridx = 2; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        JLabel medNameLabel = new JLabel("药品名称：");
        medNameLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(medNameLabel, gbc);
        gbc.gridx = 3; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        tfMedicineName = new JTextField(20);
        tfMedicineName.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(tfMedicineName, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        JLabel specLabel = new JLabel("规格：");
        specLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(specLabel, gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        tfSpecification = new JTextField(20);
        tfSpecification.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(tfSpecification, gbc);
        
        gbc.gridx = 2; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        JLabel unitLabel = new JLabel("单位：");
        unitLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(unitLabel, gbc);
        gbc.gridx = 3; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        tfUnit = new JTextField(20);
        tfUnit.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(tfUnit, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        JLabel manuLabel = new JLabel("生产厂家：");
        manuLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(manuLabel, gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        tfManufacturer = new JTextField(20);
        tfManufacturer.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(tfManufacturer, gbc);
        
        gbc.gridx = 2; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        JLabel cateLabel = new JLabel("类别：");
        cateLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(cateLabel, gbc);
        gbc.gridx = 3; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        tfCategory = new JTextField(20);
        tfCategory.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(tfCategory, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        JLabel priceLabel = new JLabel("单价：");
        priceLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(priceLabel, gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        tfPrice = new JTextField(20);
        tfPrice.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(tfPrice, gbc);
        
        gbc.gridx = 2; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        JLabel statusLabel = new JLabel("状态：");
        statusLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(statusLabel, gbc);
        gbc.gridx = 3; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        tfStatus = new JTextField(20);
        tfStatus.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(tfStatus, gbc);
        
        // 底部数据展示区
        String[] medicineColumnNames = {"药品ID", "药品名称", "规格", "单位", "生产厂家", "类别", "单价", "状态", "创建时间", "更新时间"};
        medicineTableModel = new DefaultTableModel(medicineColumnNames, 0) {
            // 表格单元格不可编辑
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        medicineTable = new JTable(medicineTableModel);
        medicineTable.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        medicineTable.setRowHeight(20);
        JScrollPane medicineScrollPane = new JScrollPane(medicineTable);
        medicineScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        medicineScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        // 组装药品面板
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(medicineScrollPane, BorderLayout.SOUTH);
        
        // 按钮事件绑定
        // 刷新按钮
        btnRefresh.addActionListener(e -> loadMedicineData(null));
        // 查询按钮
        btnSearch.addActionListener(e -> {
            String keyword = tfSearchName.getText().trim();
            loadMedicineData(keyword);
        });
        // 新增按钮
        btnAdd.addActionListener(e -> addMedicine());
        // 修改按钮
        btnUpdate.addActionListener(e -> updateMedicine());
        // 删除按钮
        btnDelete.addActionListener(e -> deleteMedicine());
        // 表格选中回显
        medicineTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && medicineTable.getSelectedRow() != -1) {
                int selectedRow = medicineTable.getSelectedRow();
                // 回显数据到文本框
                tfMedicineId.setText(medicineTableModel.getValueAt(selectedRow, 0).toString());
                tfMedicineName.setText(medicineTableModel.getValueAt(selectedRow, 1).toString());
                tfSpecification.setText(medicineTableModel.getValueAt(selectedRow, 2).toString());
                tfUnit.setText(medicineTableModel.getValueAt(selectedRow, 3).toString());
                tfManufacturer.setText(medicineTableModel.getValueAt(selectedRow, 4).toString());
                tfCategory.setText(medicineTableModel.getValueAt(selectedRow, 5).toString());
                tfPrice.setText(medicineTableModel.getValueAt(selectedRow, 6).toString());
                tfStatus.setText(medicineTableModel.getValueAt(selectedRow, 7).toString());
            }
        });
        
        return panel;
    }
    
    // 创建供应商管理面板
    private JPanel createSupplierPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        
        // 顶部查询&操作区
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        // 查询面板
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel searchLabel = new JLabel("供应商名称：");
        searchLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        searchPanel.add(searchLabel);
        JTextField tfSearchName = new JTextField(20);
        tfSearchName.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        JButton btnSearch = new JButton("查询");
        btnSearch.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        searchPanel.add(tfSearchName);
        searchPanel.add(btnSearch);
        // 操作按钮面板
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAdd = new JButton("新增");
        btnAdd.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        JButton btnUpdate = new JButton("修改");
        btnUpdate.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        JButton btnDelete = new JButton("删除");
        btnDelete.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        JButton btnRefresh = new JButton("刷新");
        btnRefresh.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnRefresh);
        // 组装顶部面板
        topPanel.add(searchPanel, BorderLayout.WEST);
        topPanel.add(btnPanel, BorderLayout.EAST);

        // 中间表单录入区
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // 表单字段
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel suppIdLabel = new JLabel("供应商ID：");
        suppIdLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(suppIdLabel, gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        tfSupplierId = new JTextField(20);
        tfSupplierId.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(tfSupplierId, gbc);
        
        gbc.gridx = 2; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        JLabel suppNameLabel = new JLabel("供应商名称：");
        suppNameLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(suppNameLabel, gbc);
        gbc.gridx = 3; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        tfSupplierName = new JTextField(20);
        tfSupplierName.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(tfSupplierName, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        JLabel contactLabel = new JLabel("联系人：");
        contactLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(contactLabel, gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        tfContact = new JTextField(20);
        tfContact.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(tfContact, gbc);
        
        gbc.gridx = 2; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        JLabel phoneLabel = new JLabel("联系电话：");
        phoneLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(phoneLabel, gbc);
        gbc.gridx = 3; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        tfPhone = new JTextField(20);
        tfPhone.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(tfPhone, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        JLabel addressLabel = new JLabel("地址：");
        addressLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(addressLabel, gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        tfAddress = new JTextField(20);
        tfAddress.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(tfAddress, gbc);
        
        gbc.gridx = 2; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        JLabel statusLabel = new JLabel("状态：");
        statusLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(statusLabel, gbc);
        gbc.gridx = 3; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        tfSupplierStatus = new JTextField(20);
        tfSupplierStatus.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(tfSupplierStatus, gbc);
        
        // 底部数据展示区
        String[] supplierColumnNames = {"供应商ID", "供应商名称", "联系人", "联系电话", "地址", "状态", "创建时间", "更新时间"};
        supplierTableModel = new DefaultTableModel(supplierColumnNames, 0) {
            // 表格单元格不可编辑
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        supplierTable = new JTable(supplierTableModel);
        supplierTable.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        supplierTable.setRowHeight(20);
        JScrollPane supplierScrollPane = new JScrollPane(supplierTable);
        supplierScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        supplierScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        // 组装供应商面板
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(supplierScrollPane, BorderLayout.SOUTH);
        
        // 按钮事件绑定
        // 刷新按钮
        btnRefresh.addActionListener(e -> loadSupplierData(null));
        // 查询按钮
        btnSearch.addActionListener(e -> {
            String keyword = tfSearchName.getText().trim();
            loadSupplierData(keyword);
        });
        // 新增按钮
        btnAdd.addActionListener(e -> addSupplier());
        // 修改按钮
        btnUpdate.addActionListener(e -> updateSupplier());
        // 删除按钮
        btnDelete.addActionListener(e -> deleteSupplier());
        // 表格选中回显
        supplierTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && supplierTable.getSelectedRow() != -1) {
                int selectedRow = supplierTable.getSelectedRow();
                // 回显数据到文本框
                tfSupplierId.setText(supplierTableModel.getValueAt(selectedRow, 0).toString());
                tfSupplierName.setText(supplierTableModel.getValueAt(selectedRow, 1).toString());
                tfContact.setText(supplierTableModel.getValueAt(selectedRow, 2).toString());
                tfPhone.setText(supplierTableModel.getValueAt(selectedRow, 3).toString());
                tfAddress.setText(supplierTableModel.getValueAt(selectedRow, 4).toString());
                tfSupplierStatus.setText(supplierTableModel.getValueAt(selectedRow, 5).toString());
            }
        });
        
        return panel;
    }

    /**
     * 加载药品数据到表格
     * @param keyword 药品名称模糊查询关键词（null则查全部）
     */
    private void loadMedicineData(String keyword) {
        try {
            // 清空表格
            medicineTableModel.setRowCount(0);
            // 查询数据
            List<Medicine> medicineList;
            if (keyword == null || keyword.isEmpty()) {
                medicineList = medicineDAO.getAllMedicines(); // 查全部
            } else {
                // 按名称模糊查询
                medicineList = medicineDAO.getMedicinesByName(keyword);
            }
            // 填充表格
            for (Medicine med : medicineList) {
                Object[] rowData = {
                        med.getMedicineId(),
                        med.getMedicineName(),
                        med.getSpecification(),
                        med.getUnit(),
                        med.getManufacturer(),
                        med.getCategory(),
                        med.getPrice(),
                        med.getStatus(),
                        med.getCreateTime(),
                        med.getUpdateTime()
                };
                medicineTableModel.addRow(rowData);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "加载药品数据失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * 加载供应商数据到表格
     * @param keyword 供应商名称模糊查询关键词（null则查全部）
     */
    private void loadSupplierData(String keyword) {
        try {
            // 清空表格
            supplierTableModel.setRowCount(0);
            // 查询数据
            List<Supplier> supplierList;
            if (keyword == null || keyword.isEmpty()) {
                supplierList = supplierDAO.getAllSuppliers();
            } else {
                supplierList = supplierDAO.getSuppliersByName(keyword);
            }
            // 填充表格
            for (Supplier sup : supplierList) {
                Object[] rowData = {
                        sup.getSupplierId(),
                        sup.getSupplierName(),
                        sup.getContact(),
                        sup.getPhone(),
                        sup.getAddress(),
                        sup.getStatus(),
                        sup.getCreateTime(),
                        sup.getUpdateTime()
                };
                supplierTableModel.addRow(rowData);
            }
            
            // 如果没有找到数据，给出提示
            if (supplierList.isEmpty() && keyword != null && !keyword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "未找到包含 \"" + keyword + "\" 的供应商", "提示", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "加载供应商数据失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 新增药品
     */
    private void addMedicine() {
        try {
            // 1. 校验必填字段
            String medicineName = tfMedicineName.getText().trim();
            if (medicineName.isEmpty()) {
                // 如果药品名称为空，则不执行新增操作
                return;
            }
            // 2. 封装实体
            Medicine medicine = new Medicine();
            medicine.setMedicineName(medicineName);
            medicine.setSpecification(tfSpecification.getText().trim());
            medicine.setUnit(tfUnit.getText().trim());
            medicine.setManufacturer(tfManufacturer.getText().trim());
            medicine.setCategory(tfCategory.getText().trim());
            // 处理单价（转换BigDecimal）
            String priceStr = tfPrice.getText().trim();
            medicine.setPrice(priceStr.isEmpty() ? new BigDecimal("0.00") : new BigDecimal(priceStr));
            // 处理状态（默认正常）
            medicine.setStatus(tfStatus.getText().trim().isEmpty() ? "正常" : tfStatus.getText().trim());
            // 时间字段
            Timestamp now = new Timestamp(System.currentTimeMillis());
            medicine.setCreateTime(now);
            medicine.setUpdateTime(now);

            // 3. 调用DAO新增
            int rows = medicineDAO.addMedicine(medicine);
            if (rows > 0) {
                // 4. 刷新表格&清空表单中药品部分
                loadMedicineData(null);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "单价必须是数字！", "错误", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "新增药品失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * 新增供应商
     */
    private void addSupplier() {
        try {
            // 校验必填字段
            String supplierName = tfSupplierName.getText().trim();
            String phone = tfPhone.getText().trim();
            if (supplierName.isEmpty() || phone.isEmpty()) {
                // 如果供应商名称或联系电话为空，则不执行新增操作
                return;
            }
            // 封装实体
            Supplier supplier = new Supplier();
            supplier.setSupplierName(supplierName);
            supplier.setContact(tfContact.getText().trim());
            supplier.setPhone(phone);
            supplier.setAddress(tfAddress.getText().trim());
            supplier.setStatus(tfSupplierStatus.getText().trim().isEmpty() ? "正常" : tfSupplierStatus.getText().trim());
            Timestamp now = new Timestamp(System.currentTimeMillis());
            supplier.setCreateTime(now);
            supplier.setUpdateTime(now);

            // 调用DAO新增
            int rows = supplierDAO.addSupplier(supplier);
            if (rows > 0) {
                loadSupplierData(null);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "新增供应商失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 修改药品
     */
    private void updateMedicine() {
        try {
            // 1. 校验ID
            String idStr = tfMedicineId.getText().trim();
            if (idStr.isEmpty()) {
                // 如果没有选择药品，则不执行修改操作
                return;
            }
            Integer medicineId = Integer.parseInt(idStr);

            // 2. 封装实体
            Medicine medicine = new Medicine();
            medicine.setMedicineId(medicineId);
            medicine.setMedicineName(tfMedicineName.getText().trim());
            medicine.setSpecification(tfSpecification.getText().trim());
            medicine.setUnit(tfUnit.getText().trim());
            medicine.setManufacturer(tfManufacturer.getText().trim());
            medicine.setCategory(tfCategory.getText().trim());
            // 处理单价
            String priceStr = tfPrice.getText().trim();
            medicine.setPrice(priceStr.isEmpty() ? new BigDecimal("0.00") : new BigDecimal(priceStr));
            // 处理状态
            medicine.setStatus(tfStatus.getText().trim().isEmpty() ? "正常" : tfStatus.getText().trim());
            // 更新时间
            medicine.setUpdateTime(new Timestamp(System.currentTimeMillis()));

            // 3. 调用DAO修改（需补充DAO的updateMedicine方法）
            String sql = "UPDATE medicine SET medicine_name=?, specification=?, unit=?, manufacturer=?, category=?, price=?, status=?, update_time=? WHERE medicine_id=?";
            Object[] params = {
                    medicine.getMedicineName(),
                    medicine.getSpecification(),
                    medicine.getUnit(),
                    medicine.getManufacturer(),
                    medicine.getCategory(),
                    medicine.getPrice(),
                    medicine.getStatus(),
                    medicine.getUpdateTime(),
                    medicine.getMedicineId()
            };
            int rows = medicineDAO.executeUpdate(sql, params);

            if (rows > 0) {
                loadMedicineData(null);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "药品ID必须是数字！", "错误", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "修改药品失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * 修改供应商
     */
    private void updateSupplier() {
        try {
            // 校验ID
            String idStr = tfSupplierId.getText().trim();
            if (idStr.isEmpty()) {
                // 如果没有选择供应商，则不执行修改操作
                return;
            }
            Integer supplierId = Integer.parseInt(idStr);

            // 封装实体
            Supplier supplier = new Supplier();
            supplier.setSupplierId(supplierId);
            supplier.setSupplierName(tfSupplierName.getText().trim());
            supplier.setContact(tfContact.getText().trim());
            supplier.setPhone(tfPhone.getText().trim());
            supplier.setAddress(tfAddress.getText().trim());
            supplier.setStatus(tfSupplierStatus.getText().trim().isEmpty() ? "正常" : tfSupplierStatus.getText().trim());
            supplier.setUpdateTime(new Timestamp(System.currentTimeMillis()));

            // 调用DAO修改（补充通用update方法）
            String sql = "UPDATE supplier SET supplier_name=?, contact=?, phone=?, address=?, status=?, update_time=? WHERE supplier_id=?";
            Object[] params = {
                    supplier.getSupplierName(),
                    supplier.getContact(),
                    supplier.getPhone(),
                    supplier.getAddress(),
                    supplier.getStatus(),
                    supplier.getUpdateTime(),
                    supplier.getSupplierId()
            };
            int rows = supplierDAO.executeUpdate(sql, params);

            if (rows > 0) {
                loadSupplierData(null);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "供应商ID必须是数字！", "错误", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "修改供应商失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 删除药品
     */
    private void deleteMedicine() {
        // 1. 确认删除
        int confirm = JOptionPane.showConfirmDialog(this, "确定要删除该药品吗？", "确认删除", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            // 2. 校验ID
            String idStr = tfMedicineId.getText().trim();
            if (idStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请输入/选择要删除的药品ID！", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Integer medicineId = Integer.parseInt(idStr);

            // 3. 调用DAO删除
            int rows = medicineDAO.deleteMedicine(medicineId);
            if (rows > 0) {
                loadMedicineData(null);
                clearMedicineForm();
            } else {
                JOptionPane.showMessageDialog(this, "删除药品失败（ID不存在）！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "药品ID必须是数字！", "错误", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "删除药品失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * 删除供应商
     */
    private void deleteSupplier() {
        // 1. 确认删除
        int confirm = JOptionPane.showConfirmDialog(this, "确定要删除该供应商吗？", "确认删除", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            // 2. 校验ID
            String idStr = tfSupplierId.getText().trim();
            if (idStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请输入要删除的供应商ID！", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Integer supplierId = Integer.parseInt(idStr);

            // 3. 调用DAO删除
            int rows = supplierDAO.deleteSupplier(supplierId);
            if (rows > 0) {
                loadSupplierData(null);
                clearSupplierForm();
            } else {
                JOptionPane.showMessageDialog(this, "删除供应商失败（ID不存在）！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "供应商ID必须是数字！", "错误", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "删除供应商失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 清空药品表单
     */
    private void clearMedicineForm() {
        tfMedicineId.setText("");
        tfMedicineName.setText("");
        tfSpecification.setText("");
        tfUnit.setText("");
        tfManufacturer.setText("");
        tfCategory.setText("");
        tfPrice.setText("");
        tfStatus.setText("");
    }
    
    /**
     * 清空供应商表单
     */
    private void clearSupplierForm() {
        tfSupplierId.setText("");
        tfSupplierName.setText("");
        tfContact.setText("");
        tfPhone.setText("");
        tfAddress.setText("");
        tfSupplierStatus.setText("");
    }
    
    /**
     * 设置按钮样式
     * @param button 按钮对象
     * @param isSelected 是否选中
     */
    private void setupButtonStyle(JButton button, boolean isSelected) {
        button.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        if (isSelected) {
            button.setBackground(Color.BLUE);
            button.setForeground(Color.WHITE);
        } else {
            button.setBackground(new Color(238, 238, 238)); // 浅灰色
            button.setForeground(Color.BLACK);
        }
    }
}