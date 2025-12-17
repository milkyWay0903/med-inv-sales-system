package com.dayu.system.view;

import com.dayu.system.dao.impl.MedicineDAO;
import com.dayu.system.dao.impl.StockDAO;
import com.dayu.system.entity.Medicine;
import com.dayu.system.entity.Stock;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

/**
 * 库存与盘点管理面板
 */
public class InventoryPanel extends JPanel {
    // DAO对象
    private final StockDAO stockDAO = new StockDAO();
    private final MedicineDAO medicineDAO = new MedicineDAO();
    
    // 组件声明
    private JTable stockTable;
    private DefaultTableModel tableModel;
    private JTextField tfStockId, tfMedicineId, tfCurrentQuantity, tfMinQuantity, tfMaxQuantity, tfLocation, tfStatus;
    private JButton btnAdd, btnUpdate, btnDelete, btnRefresh, btnSearch;
    private JTextField tfSearchMedicine;

    public InventoryPanel() {
        // 设置布局
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 创建顶部查询面板
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        TitledBorder searchBorder = BorderFactory.createTitledBorder("库存查询");
        searchBorder.setTitleFont(new Font("微软雅黑", Font.PLAIN, 12));
        searchPanel.setBorder(searchBorder);
        JLabel searchLabel = new JLabel("药品名称：");
        searchLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        searchPanel.add(searchLabel);
        tfSearchMedicine = new JTextField(20);
        tfSearchMedicine.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        searchPanel.add(tfSearchMedicine);
        btnSearch = new JButton("查询");
        btnSearch.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        searchPanel.add(btnSearch);
        btnRefresh = new JButton("刷新");
        btnRefresh.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        searchPanel.add(btnRefresh);

        // 创建中部表单面板
        JPanel formPanel = new JPanel(new GridBagLayout());
        TitledBorder formBorder = BorderFactory.createTitledBorder("库存信息");
        formBorder.setTitleFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.setBorder(formBorder);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // 第一行
        gbc.gridx = 0; gbc.gridy = 0;
        JLabel stockIdLabel = new JLabel("库存ID：");
        stockIdLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(stockIdLabel, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        tfStockId = new JTextField(20);
        tfStockId.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(tfStockId, gbc);
        
        gbc.gridx = 2; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        JLabel medicineIdLabel = new JLabel("药品ID：");
        medicineIdLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(medicineIdLabel, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        tfMedicineId = new JTextField(20);
        tfMedicineId.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(tfMedicineId, gbc);
        
        // 第二行
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        JLabel currentQtyLabel = new JLabel("当前数量：");
        currentQtyLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(currentQtyLabel, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        tfCurrentQuantity = new JTextField(20);
        tfCurrentQuantity.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(tfCurrentQuantity, gbc);
        
        gbc.gridx = 2; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        JLabel minQtyLabel = new JLabel("最小数量：");
        minQtyLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(minQtyLabel, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        tfMinQuantity = new JTextField(20);
        tfMinQuantity.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(tfMinQuantity, gbc);
        
        // 第三行
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        JLabel maxQtyLabel = new JLabel("最大数量：");
        maxQtyLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(maxQtyLabel, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        tfMaxQuantity = new JTextField(20);
        tfMaxQuantity.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(tfMaxQuantity, gbc);
        
        gbc.gridx = 2; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        JLabel locationLabel = new JLabel("库位：");
        locationLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(locationLabel, gbc);
        
        gbc.gridx = 3; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        tfLocation = new JTextField(20);
        tfLocation.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(tfLocation, gbc);
        
        // 第四行
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        JLabel statusLabel = new JLabel("状态：");
        statusLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(statusLabel, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        tfStatus = new JTextField(20);
        tfStatus.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(tfStatus, gbc);
        
        gbc.gridx = 2; gbc.weightx = 0; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel(""), gbc); // 占位
        
        gbc.gridx = 3; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(new JLabel(""), gbc); // 占位

        // 创建底部按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnAdd = new JButton("新增库存");
        btnAdd.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        btnUpdate = new JButton("修改库存");
        btnUpdate.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        btnDelete = new JButton("删除库存");
        btnDelete.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);

        // 创建表格面板
        String[] columnNames = {"库存ID", "药品ID", "药品名称", "当前数量", "最小数量", "最大数量", "库位", "状态", "更新时间"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        stockTable = new JTable(tableModel);
        stockTable.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        stockTable.setRowHeight(20);
        JScrollPane tableScrollPane = new JScrollPane(stockTable);
        tableScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        tableScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        TitledBorder tableBorder = BorderFactory.createTitledBorder("库存列表");
        tableBorder.setTitleFont(new Font("微软雅黑", Font.PLAIN, 12));
        tableScrollPane.setBorder(tableBorder);

        // 组装面板
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(searchPanel, BorderLayout.NORTH);
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);

        // 加载初始数据 - 直接显示所有库存数据
        loadAllStockData();

        // 绑定事件
        bindEvents();
    }

    /**
     * 绑定事件
     */
    private void bindEvents() {
        // 查询按钮
        btnSearch.addActionListener(e -> {
            String keyword = tfSearchMedicine.getText().trim();
            loadStockData(keyword);
        });
        
        // 刷新按钮
        btnRefresh.addActionListener(e -> loadAllStockData());
        
        // 新增按钮
        btnAdd.addActionListener(e -> addStock());
        
        // 修改按钮
        btnUpdate.addActionListener(e -> updateStock());
        
        // 删除按钮
        btnDelete.addActionListener(e -> deleteStock());
        
        // 表格选中事件
        stockTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && stockTable.getSelectedRow() != -1) {
                int selectedRow = stockTable.getSelectedRow();
                tfStockId.setText(tableModel.getValueAt(selectedRow, 0).toString());
                tfMedicineId.setText(tableModel.getValueAt(selectedRow, 1).toString());
                tfCurrentQuantity.setText(tableModel.getValueAt(selectedRow, 3).toString());
                tfMinQuantity.setText(tableModel.getValueAt(selectedRow, 4).toString());
                tfMaxQuantity.setText(tableModel.getValueAt(selectedRow, 5).toString());
                tfLocation.setText(tableModel.getValueAt(selectedRow, 6).toString());
                tfStatus.setText(tableModel.getValueAt(selectedRow, 7).toString());
            }
        });
    }

    /**
     * 加载所有库存数据（直接显示库存表数据）
     */
    private void loadAllStockData() {
        try {
            tableModel.setRowCount(0);
            
            // 查询所有库存
            List<Stock> stocks = stockDAO.getAllStocks();
            for (Stock stock : stocks) {
                // 获取药品信息
                Medicine medicine = medicineDAO.getMedicineById(stock.getMedicineId());
                String medicineName = (medicine != null) ? medicine.getMedicineName() : "未知药品";
                
                Object[] rowData = {
                    stock.getStockId(),
                    stock.getMedicineId(),
                    medicineName,
                    stock.getCurrentQuantity(),
                    stock.getMinQuantity(),
                    stock.getMaxQuantity(),
                    stock.getLocation(),
                    stock.getStatus(),
                    stock.getUpdateTime()
                };
                tableModel.addRow(rowData);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "加载库存数据失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 加载库存数据（根据药品名称查询）
     * @param keyword 药品名称查询关键字
     */
    private void loadStockData(String keyword) {
        try {
            tableModel.setRowCount(0);
            
            // 如果有查询关键字，则根据药品名称查询
            if (keyword != null && !keyword.isEmpty()) {
                List<Medicine> medicines = medicineDAO.getMedicinesByName(keyword);
                for (Medicine medicine : medicines) {
                    Stock stock = stockDAO.getStockByMedicineId(medicine.getMedicineId());
                    if (stock != null) {
                        Object[] rowData = {
                            stock.getStockId(),
                            stock.getMedicineId(),
                            medicine.getMedicineName(),
                            stock.getCurrentQuantity(),
                            stock.getMinQuantity(),
                            stock.getMaxQuantity(),
                            stock.getLocation(),
                            stock.getStatus(),
                            stock.getUpdateTime()
                        };
                        tableModel.addRow(rowData);
                    }
                }
            } else {
                // 查询所有库存
                loadAllStockData();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "加载库存数据失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 新增库存
     */
    private void addStock() {
        try {
            // 获取表单数据
            String medicineIdStr = tfMedicineId.getText().trim();
            String currentQuantityStr = tfCurrentQuantity.getText().trim();
            String minQuantityStr = tfMinQuantity.getText().trim();
            String maxQuantityStr = tfMaxQuantity.getText().trim();
            String location = tfLocation.getText().trim();
            String status = tfStatus.getText().trim();
            
            if (medicineIdStr.isEmpty() || currentQuantityStr.isEmpty() || minQuantityStr.isEmpty() || maxQuantityStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请填写完整信息！", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int medicineId = Integer.parseInt(medicineIdStr);
            int currentQuantity = Integer.parseInt(currentQuantityStr);
            int minQuantity = Integer.parseInt(minQuantityStr);
            int maxQuantity = Integer.parseInt(maxQuantityStr);
            
            // 不再检查是否已存在该药品的库存，允许同一药品有多个库存记录
            
            // 封装实体
            Stock stock = new Stock();
            stock.setMedicineId(medicineId);
            stock.setCurrentQuantity(currentQuantity);
            stock.setMinQuantity(minQuantity);
            stock.setMaxQuantity(maxQuantity);
            stock.setLocation(location.isEmpty() ? "默认库位" : location);
            stock.setStatus(status.isEmpty() ? "正常" : status);
            stock.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            
            // 调用DAO新增（使用新的addStock方法）
            int rows = stockDAO.addStock(stock);
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "新增库存成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                loadAllStockData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "新增库存失败！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "数据格式不正确！", "错误", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "新增库存失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 修改库存
     */
    private void updateStock() {
        try {
            String stockIdStr = tfStockId.getText().trim();
            if (stockIdStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请选择要修改的库存记录！", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // 获取表单数据
            String medicineIdStr = tfMedicineId.getText().trim();
            String currentQuantityStr = tfCurrentQuantity.getText().trim();
            String minQuantityStr = tfMinQuantity.getText().trim();
            String maxQuantityStr = tfMaxQuantity.getText().trim();
            String location = tfLocation.getText().trim();
            String status = tfStatus.getText().trim();
            
            if (medicineIdStr.isEmpty() || currentQuantityStr.isEmpty() || minQuantityStr.isEmpty() || maxQuantityStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请填写完整信息！", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int stockId = Integer.parseInt(stockIdStr);
            int medicineId = Integer.parseInt(medicineIdStr);
            int currentQuantity = Integer.parseInt(currentQuantityStr);
            int minQuantity = Integer.parseInt(minQuantityStr);
            int maxQuantity = Integer.parseInt(maxQuantityStr);
            
            // 封装实体
            Stock stock = new Stock();
            stock.setStockId(stockId);
            stock.setMedicineId(medicineId);
            stock.setCurrentQuantity(currentQuantity);
            stock.setMinQuantity(minQuantity);
            stock.setMaxQuantity(maxQuantity);
            stock.setLocation(location.isEmpty() ? "默认库位" : location);
            stock.setStatus(status.isEmpty() ? "正常" : status);
            stock.setUpdateTime(new Timestamp(System.currentTimeMillis()));
            
            // 调用DAO修改
            int rows = stockDAO.updateStock(stock);
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "修改库存成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                loadAllStockData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "修改库存失败！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "数据格式不正确！", "错误", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "修改库存失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 删除库存
     */
    private void deleteStock() {
        try {
            String stockIdStr = tfStockId.getText().trim();
            if (stockIdStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请选择要删除的库存记录！", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int confirm = JOptionPane.showConfirmDialog(this, "确定要删除该库存记录吗？", "确认删除", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
            
            int stockId = Integer.parseInt(stockIdStr);
            
            String sql = "DELETE FROM stock WHERE stock_id=?";
            int rows = stockDAO.executeUpdate(sql, stockId);
            
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "删除库存成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                loadAllStockData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "删除库存失败！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "库存ID格式不正确！", "错误", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "删除库存失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 清空表单
     */
    private void clearForm() {
        tfStockId.setText("");
        tfMedicineId.setText("");
        tfCurrentQuantity.setText("");
        tfMinQuantity.setText("");
        tfMaxQuantity.setText("");
        tfLocation.setText("");
        tfStatus.setText("");
    }
}