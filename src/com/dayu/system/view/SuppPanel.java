package com.dayu.system.view;

import com.dayu.system.dao.impl.SupplierDAO;
import com.dayu.system.entity.Supplier;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

/**
 * 供应商管理面板：增删改查功能
 */
public class SuppPanel extends JPanel {
    // DAO对象
    private final SupplierDAO supplierDAO = new SupplierDAO();
    // 组件声明
    private JTable supplierTable;
    private DefaultTableModel tableModel;
    private JTextField tfSupplierId, tfSupplierName, tfContactPerson, tfPhone, tfAddress, tfStatus;

    public SuppPanel() {
        // 1. 面板布局（BorderLayout）
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 2. 顶部查询&操作区
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        // 2.1 查询面板
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
        // 2.2 操作按钮面板
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

        // 3. 中间表单录入区
        JPanel formPanel = new JPanel(new GridLayout(4, 4, 10, 10));
        // 表单字段
        JLabel idLabel = new JLabel("供应商ID（查询/删除用）：");
        idLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(idLabel);
        tfSupplierId = new JTextField();
        tfSupplierId.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(tfSupplierId);
        
        JLabel nameLabel = new JLabel("供应商名称：");
        nameLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(nameLabel);
        tfSupplierName = new JTextField();
        tfSupplierName.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(tfSupplierName);
        
        JLabel contactLabel = new JLabel("联系人：");
        contactLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(contactLabel);
        tfContactPerson = new JTextField();
        tfContactPerson.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(tfContactPerson);
        
        JLabel phoneLabel = new JLabel("联系电话：");
        phoneLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(phoneLabel);
        tfPhone = new JTextField();
        tfPhone.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(tfPhone);
        
        JLabel addressLabel = new JLabel("地址：");
        addressLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(addressLabel);
        tfAddress = new JTextField();
        tfAddress.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(tfAddress);
        
        JLabel statusLabel = new JLabel("状态（正常/停用）：");
        statusLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(statusLabel);
        tfStatus = new JTextField();
        tfStatus.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        formPanel.add(tfStatus);
        
        // 占位（凑4行4列）
        formPanel.add(new JLabel(""));
        formPanel.add(new JLabel(""));

        // 4. 底部数据展示区（表格）
        // 表格列名
        String[] columnNames = {"供应商ID", "供应商名称", "联系人", "联系电话", "地址", "状态", "创建时间", "更新时间"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        supplierTable = new JTable(tableModel);
        supplierTable.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        supplierTable.setRowHeight(20);
        JScrollPane tableScrollPane = new JScrollPane(supplierTable);

        // 5. 组装面板
        add(topPanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(tableScrollPane, BorderLayout.SOUTH);

        // 6. 加载初始数据
        loadSupplierData(null);

        // 7. 按钮事件绑定
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
                tfSupplierId.setText(tableModel.getValueAt(selectedRow, 0).toString());
                tfSupplierName.setText(tableModel.getValueAt(selectedRow, 1).toString());
                tfContactPerson.setText(tableModel.getValueAt(selectedRow, 2).toString());
                tfPhone.setText(tableModel.getValueAt(selectedRow, 3).toString());
                tfAddress.setText(tableModel.getValueAt(selectedRow, 4).toString());
                tfStatus.setText(tableModel.getValueAt(selectedRow, 5).toString());
            }
        });
    }

    /**
     * 加载供应商数据到表格
     */
    private void loadSupplierData(String keyword) {
        try {
            tableModel.setRowCount(0);
            List<Supplier> supplierList;
            if (keyword == null || keyword.isEmpty()) {
                // 补充DAO方法：查询所有供应商
                supplierList = supplierDAO.getAllSuppliers();
            } else {
                supplierList = supplierDAO.getSuppliersByName(keyword);
            }
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
                tableModel.addRow(rowData);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "加载供应商数据失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
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
                JOptionPane.showMessageDialog(this, "供应商名称和联系电话不能为空！", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // 封装实体
            Supplier supplier = new Supplier();
            supplier.setSupplierName(supplierName);
            supplier.setContact(tfContactPerson.getText().trim());
            supplier.setPhone(phone);
            supplier.setAddress(tfAddress.getText().trim());
            supplier.setStatus(tfStatus.getText().trim().isEmpty() ? "正常" : tfStatus.getText().trim());
            Timestamp now = new Timestamp(System.currentTimeMillis());
            supplier.setCreateTime(now);
            supplier.setUpdateTime(now);

            // 调用DAO新增
            int rows = supplierDAO.addSupplier(supplier);
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "新增供应商成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                loadSupplierData(null);
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "新增供应商失败！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "新增供应商失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
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
                JOptionPane.showMessageDialog(this, "请选择要修改的供应商！", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Integer supplierId = Integer.parseInt(idStr);

            // 封装实体
            Supplier supplier = new Supplier();
            supplier.setSupplierId(supplierId);
            supplier.setSupplierName(tfSupplierName.getText().trim());
            supplier.setContact(tfContactPerson.getText().trim());
            supplier.setPhone(tfPhone.getText().trim());
            supplier.setAddress(tfAddress.getText().trim());
            supplier.setStatus(tfStatus.getText().trim().isEmpty() ? "正常" : tfStatus.getText().trim());
            supplier.setUpdateTime(new Timestamp(System.currentTimeMillis()));

            // 调用DAO修改（补充通用update方法）
            String sql = "UPDATE supplier SET supplier_name=?, contact_person=?, phone=?, address=?, status=?, update_time=? WHERE supplier_id=?";
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
                JOptionPane.showMessageDialog(this, "修改供应商成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                loadSupplierData(null);
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "修改供应商失败（ID不存在）！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "供应商ID必须是数字！", "错误", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "修改供应商失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 删除供应商
     */
    private void deleteSupplier() {
        int confirm = JOptionPane.showConfirmDialog(this, "确定要删除该供应商吗？", "确认删除", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            String idStr = tfSupplierId.getText().trim();
            if (idStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请输入要删除的供应商ID！", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Integer supplierId = Integer.parseInt(idStr);

            int rows = supplierDAO.deleteSupplier(supplierId);
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "删除供应商成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                loadSupplierData(null);
                clearForm();
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
     * 清空表单
     */
    private void clearForm() {
        tfSupplierId.setText("");
        tfSupplierName.setText("");
        tfContactPerson.setText("");
        tfPhone.setText("");
        tfAddress.setText("");
        tfStatus.setText("");
    }
}