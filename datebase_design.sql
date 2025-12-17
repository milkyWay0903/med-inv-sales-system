-- ============================================
-- 药品存销管理系统数据库建表脚本
-- 数据库名：med_inv_sales
-- 字符集：utf8mb4
-- ============================================

-- 1. 药品表 (D1)
CREATE TABLE IF NOT EXISTS medicine (
    medicine_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '药品ID',
    medicine_name VARCHAR(100) NOT NULL COMMENT '药品名称',
    specification VARCHAR(50) COMMENT '规格',
    unit VARCHAR(20) COMMENT '单位',
    manufacturer VARCHAR(100) COMMENT '生产厂家',
    category VARCHAR(50) COMMENT '类别',
    price DECIMAL(10, 2) DEFAULT 0.00 COMMENT '单价',
    status VARCHAR(10) DEFAULT '正常' COMMENT '状态:正常/停用',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_medicine_name (medicine_name),
    INDEX idx_category (category)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='药品信息表';

-- 2. 供应商表 (D2)
CREATE TABLE IF NOT EXISTS supplier (
    supplier_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '供应商ID',
    supplier_name VARCHAR(100) NOT NULL COMMENT '供应商名称',
    contact VARCHAR(50) COMMENT '联系人',
    phone VARCHAR(20) COMMENT '电话',
    address VARCHAR(200) COMMENT '地址',
    status VARCHAR(10) DEFAULT '合作中' COMMENT '状态:合作中/已终止',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_supplier_name (supplier_name),
    UNIQUE KEY uk_supplier_name (supplier_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='供应商表';

-- 3. 库存表 (D3)
CREATE TABLE IF NOT EXISTS stock (
    stock_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '库存ID',
    medicine_id INT NOT NULL COMMENT '药品ID',
    current_quantity INT DEFAULT 0 COMMENT '当前库存数量',
    min_quantity INT DEFAULT 10 COMMENT '最低库存量',
    max_quantity INT DEFAULT 1000 COMMENT '最高库存量',
    location VARCHAR(50) COMMENT '存放位置',
    status VARCHAR(10) DEFAULT '正常' COMMENT '状态:正常/缺货/积压',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (medicine_id) REFERENCES medicine(medicine_id) ON DELETE CASCADE,
    INDEX idx_medicine_id (medicine_id),
    INDEX idx_location (location)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存表';

-- 4. 采购单表 (D4)
CREATE TABLE IF NOT EXISTS purchase_order (
    purchase_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '采购单ID',
    supplier_id INT NOT NULL COMMENT '供应商ID',
    purchase_date DATE NOT NULL COMMENT '采购日期',
    total_amount DECIMAL(12, 2) DEFAULT 0.00 COMMENT '总金额',
    status VARCHAR(20) DEFAULT '待入库' COMMENT '状态:待入库/已完成/已取消',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (supplier_id) REFERENCES supplier(supplier_id) ON DELETE RESTRICT,
    INDEX idx_supplier_id (supplier_id),
    INDEX idx_purchase_date (purchase_date),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采购单表';

-- 5. 采购单明细表 (D5)
CREATE TABLE IF NOT EXISTS purchase_detail (
    purchase_detail_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '采购明细ID',
    purchase_id INT NOT NULL COMMENT '采购单ID',
    medicine_id INT NOT NULL COMMENT '药品ID',
    quantity INT NOT NULL COMMENT '采购数量',
    unit_price DECIMAL(10, 2) NOT NULL COMMENT '采购单价',
    amount DECIMAL(10, 2) GENERATED ALWAYS AS (quantity * unit_price) STORED COMMENT '金额',
    remark VARCHAR(200) COMMENT '备注',
    FOREIGN KEY (purchase_id) REFERENCES purchase_order(purchase_id) ON DELETE CASCADE,
    FOREIGN KEY (medicine_id) REFERENCES medicine(medicine_id) ON DELETE RESTRICT,
    INDEX idx_purchase_id (purchase_id),
    INDEX idx_medicine_id (medicine_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采购单明细表';

-- 6. 销售单表 (D6)
CREATE TABLE IF NOT EXISTS sales_order (
    sales_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '销售单ID',
    customer_name VARCHAR(100) NOT NULL COMMENT '客户名称',
    sales_date DATE NOT NULL COMMENT '销售日期',
    total_amount DECIMAL(12, 2) DEFAULT 0.00 COMMENT '总金额',
    status VARCHAR(20) DEFAULT '待出库' COMMENT '状态:待出库/已完成/已退货',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_customer_name (customer_name),
    INDEX idx_sales_date (sales_date),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='销售单表';

-- 7. 销售单明细表 (D7)
CREATE TABLE IF NOT EXISTS sales_detail (
    sales_detail_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '销售明细ID',
    sales_id INT NOT NULL COMMENT '销售单ID',
    medicine_id INT NOT NULL COMMENT '药品ID',
    quantity INT NOT NULL COMMENT '销售数量',
    unit_price DECIMAL(10, 2) NOT NULL COMMENT '销售单价',
    amount DECIMAL(10, 2) GENERATED ALWAYS AS (quantity * unit_price) STORED COMMENT '金额',
    remark VARCHAR(200) COMMENT '备注',
    FOREIGN KEY (sales_id) REFERENCES sales_order(sales_id) ON DELETE CASCADE,
    FOREIGN KEY (medicine_id) REFERENCES medicine(medicine_id) ON DELETE RESTRICT,
    INDEX idx_sales_id (sales_id),
    INDEX idx_medicine_id (medicine_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='销售单明细表';

-- 8. 系统用户表
CREATE TABLE IF NOT EXISTS system_user (
    user_id INT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    role VARCHAR(20) NOT NULL COMMENT '角色:admin/purchaser/storekeeper/salesman',
    real_name VARCHAR(50) COMMENT '姓名',
    phone VARCHAR(20) COMMENT '电话',
    status VARCHAR(10) DEFAULT '正常' COMMENT '状态:正常/停用',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_username (username),
    INDEX idx_role (role)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';

-- ============================================
-- 插入初始数据（每个表至少20条记录）
-- ============================================

-- 1. 药品表 (medicine) 初始数据 (21条)
INSERT INTO medicine (medicine_name, specification, unit, manufacturer, category, price, status) VALUES
('阿莫西林胶囊', '0.25g*24粒', '盒', '华北制药股份有限公司', '抗生素', 12.50, '正常'),
('布洛芬缓释胶囊', '0.4g*20粒', '盒', '芬必得制药有限公司', '解热镇痛', 18.80, '正常'),
('感冒灵颗粒', '10g*9袋', '盒', '华润三九医药股份有限公司', '感冒用药', 9.90, '正常'),
('奥美拉唑肠溶胶囊', '20mg*14粒', '盒', '阿斯利康制药有限公司', '肠胃用药', 35.60, '正常'),
('硝苯地平缓释片', '20mg*30片', '盒', '拜耳医药保健有限公司', '心脑血管', 28.50, '正常'),
('氨溴索口服溶液', '100ml:300mg', '瓶', '勃林格殷格翰药业', '止咳化痰', 45.80, '正常'),
('头孢克肟分散片', '0.1g*12片', '盒', '白云山制药总厂', '抗生素', 22.30, '正常'),
('氯雷他定片', '10mg*7片', '盒', '上海先灵葆雅制药', '抗过敏', 16.70, '正常'),
('健胃消食片', '0.8g*60片', '盒', '江中药业股份有限公司', '消化用药', 8.50, '正常'),
('复方丹参滴丸', '27mg*180丸', '瓶', '天士力制药集团', '心脑血管', 29.80, '正常'),
('葡萄糖酸钙口服液', '10ml*12支', '盒', '哈药集团三精制药', '补钙', 25.60, '正常'),
('阿奇霉素分散片', '0.25g*6片', '盒', '辉瑞制药有限公司', '抗生素', 32.90, '正常'),
('对乙酰氨基酚片', '0.5g*24片', '盒', '东北制药集团', '解热镇痛', 5.80, '正常'),
('蒙脱石散', '3g*10袋', '盒', '博福-益普生制药', '止泻', 19.80, '正常'),
('硝酸甘油片', '0.5mg*100片', '瓶', '北京益民药业', '心脑血管', 15.20, '正常'),
('盐酸二甲双胍片', '0.5g*48片', '盒', '中美上海施贵宝', '糖尿病', 21.50, '正常'),
('蒲地蓝消炎口服液', '10ml*10支', '盒', '济川药业集团', '清热解毒', 38.90, '正常'),
('维生素C片', '0.1g*100片', '瓶', '华北制药股份有限公司', '维生素类', 6.50, '正常'),
('红霉素软膏', '10g:10万单位', '支', '马应龙药业集团', '外用抗菌', 4.90, '正常'),
('云南白药气雾剂', '85g+30g', '套', '云南白药集团', '外用止血', 42.80, '正常'),
('甲钴胺片', '0.5mg*20片', '盒', '卫材(中国)药业', '神经用药', 30.50, '正常');

-- 2. 供应商表 (supplier) 初始数据 (21条)
INSERT INTO supplier (supplier_name, contact, phone, address, status) VALUES
('华北制药供应商', '张三', '13800138001', '河北省石家庄市长安区和平东路', '合作中'),
('芬必得制药经销商', '李四', '13800138002', '北京市朝阳区建国路88号', '合作中'),
('华润三九供应商', '王五', '13800138003', '广东省深圳市罗湖区深南东路', '合作中'),
('阿斯利康医药代理', '赵六', '13800138004', '上海市浦东新区张江高科技园区', '合作中'),
('拜耳医药经销商', '孙七', '13800138005', '江苏省南京市鼓楼区中山路32号', '合作中'),
('勃林格殷格翰代理', '周八', '13800138006', '上海市静安区南京西路1266号', '合作中'),
('白云山制药供应商', '吴九', '13800138007', '广东省广州市白云区同和街', '合作中'),
('上海先灵葆雅代理', '郑十', '13800138008', '上海市黄浦区南京东路800号', '合作中'),
('江中药业经销商', '钱一', '13800138009', '江西省南昌市高新区火炬大街', '合作中'),
('天士力制药供应商', '孙二', '13800138010', '天津市北辰区普济河东道', '合作中'),
('哈药集团经销商', '周三', '13800138011', '黑龙江省哈尔滨市南岗区学府路', '合作中'),
('辉瑞制药代理', '吴四', '13800138012', '北京市海淀区中关村南大街', '合作中'),
('东北制药供应商', '郑五', '13800138013', '辽宁省沈阳市铁西区重工北街', '合作中'),
('博福-益普生代理', '王六', '13800138014', '天津市和平区南京路189号', '合作中'),
('北京益民药业经销商', '刘七', '13800138015', '北京市丰台区南四环西路', '合作中'),
('中美上海施贵宝供应商', '黄八', '13800138016', '上海市闵行区剑川路', '合作中'),
('济川药业代理', '周九', '13800138017', '江苏省泰州市泰兴市大庆中路', '合作中'),
('马应龙药业经销商', '朱十', '13800138018', '湖北省武汉市武昌区南湖路', '合作中'),
('云南白药集团供应商', '胡一', '13800138019', '云南省昆明市五华区东风西路', '合作中'),
('卫材中国代理', '林二', '13800138020', '江苏省苏州市工业园区星湖街', '合作中'),
('国药控股股份有限公司', '高三', '13800138021', '上海市黄浦区龙华东路810号', '合作中');

-- 3. 库存表 (stock) 初始数据 (21条，关联medicine_id)
INSERT INTO stock (medicine_id, current_quantity, min_quantity, max_quantity, location, status) VALUES
(1, 150, 10, 1000, 'A区1排1号', '正常'),
(2, 120, 10, 1000, 'A区1排2号', '正常'),
(3, 200, 10, 1000, 'A区1排3号', '正常'),
(4, 80, 10, 1000, 'A区1排4号', '正常'),
(5, 95, 10, 1000, 'A区1排5号', '正常'),
(6, 70, 10, 1000, 'A区2排1号', '正常'),
(7, 110, 10, 1000, 'A区2排2号', '正常'),
(8, 130, 10, 1000, 'A区2排3号', '正常'),
(9, 250, 10, 1000, 'A区2排4号', '正常'),
(10, 60, 10, 1000, 'A区2排5号', '正常'),
(11, 85, 10, 1000, 'A区3排1号', '正常'),
(12, 90, 10, 1000, 'A区3排2号', '正常'),
(13, 300, 10, 1000, 'A区3排3号', '正常'),
(14, 75, 10, 1000, 'A区3排4号', '正常'),
(15, 50, 10, 1000, 'A区3排5号', '缺货'),
(16, 100, 10, 1000, 'B区1排1号', '正常'),
(17, 65, 10, 1000, 'B区1排2号', '正常'),
(18, 400, 10, 1000, 'B区1排3号', '积压'),
(19, 180, 10, 1000, 'B区1排4号', '正常'),
(20, 55, 10, 1000, 'B区1排5号', '正常'),
(21, 78, 10, 1000, 'B区2排1号', '正常');

-- 4. 采购单表 (purchase_order) 初始数据 (20条，关联supplier_id)
INSERT INTO purchase_order (supplier_id, purchase_date, total_amount, status) VALUES
(1, '2025-01-05', 0.00, '已完成'),
(2, '2025-01-08', 0.00, '已完成'),
(3, '2025-01-10', 0.00, '已完成'),
(4, '2025-01-12', 0.00, '已完成'),
(5, '2025-01-15', 0.00, '已完成'),
(6, '2025-01-18', 0.00, '已完成'),
(7, '2025-01-20', 0.00, '已完成'),
(8, '2025-01-22', 0.00, '已完成'),
(9, '2025-01-25', 0.00, '已完成'),
(10, '2025-01-28', 0.00, '已完成'),
(11, '2025-02-01', 0.00, '已完成'),
(12, '2025-02-05', 0.00, '已完成'),
(13, '2025-02-08', 0.00, '已完成'),
(14, '2025-02-10', 0.00, '已完成'),
(15, '2025-02-12', 0.00, '已完成'),
(16, '2025-02-15', 0.00, '已完成'),
(17, '2025-02-18', 0.00, '已完成'),
(18, '2025-02-20', 0.00, '已完成'),
(19, '2025-02-22', 0.00, '已完成'),
(20, '2025-02-28', 0.00, '待入库');

-- 5. 采购单明细表 (purchase_detail) 初始数据 (60+条，1个采购单对应多条明细，单价与medicine一致)
INSERT INTO purchase_detail (purchase_id, medicine_id, quantity, unit_price, remark) VALUES
-- 采购单1（supplier_id=1）：3条明细
(1, 1, 300, 12.50, '常规采购'),
(1, 18, 500, 6.50, '常规采购'),
(1, 19, 400, 4.90, '常规采购'),
-- 采购单2（supplier_id=2）：3条明细
(2, 2, 200, 18.80, '常规采购'),
(2, 13, 300, 5.80, '常规采购'),
(2, 8, 150, 16.70, '常规采购'),
-- 采购单3（supplier_id=3）：3条明细
(3, 3, 400, 9.90, '常规采购'),
(3, 9, 200, 8.50, '常规采购'),
(3, 15, 100, 15.20, '常规采购'),
-- 采购单4（supplier_id=4）：3条明细
(4, 4, 150, 35.60, '常规采购'),
(4, 16, 200, 21.50, '常规采购'),
(4, 21, 120, 30.50, '常规采购'),
-- 采购单5（supplier_id=5）：3条明细
(5, 5, 250, 28.50, '常规采购'),
(5, 10, 180, 29.80, '常规采购'),
(5, 7, 160, 22.30, '常规采购'),
-- 采购单6（supplier_id=6）：3条明细
(6, 6, 120, 45.80, '常规采购'),
(6, 14, 180, 19.80, '常规采购'),
(6, 20, 90, 42.80, '常规采购'),
-- 采购单7（supplier_id=7）：3条明细
(7, 7, 300, 22.30, '常规采购'),
(7, 1, 200, 12.50, '补货'),
(7, 17, 100, 38.90, '常规采购'),
-- 采购单8（supplier_id=8）：3条明细
(8, 8, 250, 16.70, '常规采购'),
(8, 11, 150, 25.60, '常规采购'),
(8, 4, 80, 35.60, '补货'),
-- 采购单9（supplier_id=9）：3条明细
(9, 9, 400, 8.50, '常规采购'),
(9, 3, 200, 9.90, '补货'),
(9, 18, 300, 6.50, '常规采购'),
-- 采购单10（supplier_id=10）：3条明细
(10, 10, 220, 29.80, '常规采购'),
(10, 5, 100, 28.50, '补货'),
(10, 12, 150, 32.90, '常规采购'),
-- 采购单11（supplier_id=11）：3条明细
(11, 11, 300, 25.60, '常规采购'),
(11, 2, 180, 18.80, '补货'),
(11, 19, 200, 4.90, '常规采购'),
-- 采购单12（supplier_id=12）：3条明细
(12, 12, 200, 32.90, '常规采购'),
(12, 6, 100, 45.80, '补货'),
(12, 8, 120, 16.70, '补货'),
-- 采购单13（supplier_id=13）：3条明细
(13, 13, 500, 5.80, '常规采购'),
(13, 1, 150, 12.50, '补货'),
(13, 9, 200, 8.50, '补货'),
-- 采购单14（supplier_id=14）：3条明细
(14, 14, 250, 19.80, '常规采购'),
(14, 7, 180, 22.30, '补货'),
(14, 3, 100, 9.90, '补货'),
-- 采购单15（supplier_id=15）：3条明细
(15, 15, 300, 15.20, '常规采购'),
(15, 10, 120, 29.80, '补货'),
(15, 5, 80, 28.50, '补货'),
-- 采购单16（supplier_id=16）：3条明细
(16, 16, 280, 21.50, '常规采购'),
(16, 4, 100, 35.60, '补货'),
(16, 17, 90, 38.90, '常规采购'),
-- 采购单17（supplier_id=17）：3条明细
(17, 17, 200, 38.90, '常规采购'),
(17, 6, 80, 45.80, '补货'),
(17, 20, 100, 42.80, '常规采购'),
-- 采购单18（supplier_id=18）：3条明细
(18, 18, 400, 6.50, '常规采购'),
(18, 11, 120, 25.60, '补货'),
(18, 19, 300, 4.90, '常规采购'),
-- 采购单19（supplier_id=19）：3条明细
(19, 19, 500, 4.90, '常规采购'),
(19, 13, 200, 5.80, '补货'),
(19, 1, 250, 12.50, '常规采购'),
-- 采购单20（supplier_id=20）：4条明细（待入库）
(20, 20, 150, 42.80, '备货采购'),
(20, 21, 200, 30.50, '备货采购'),
(20, 12, 180, 32.90, '备货采购'),
(20, 16, 150, 21.50, '备货采购');

-- 6. 销售单表 (sales_order) 初始数据 (20条)
INSERT INTO sales_order (customer_name, sales_date, total_amount, status) VALUES
('XX连锁药店', '2025-01-06', 0.00, '已完成'),
('XX社区医院', '2025-01-09', 0.00, '已完成'),
('XX零售药店', '2025-01-11', 0.00, '已完成'),
('XX民营医院', '2025-01-13', 0.00, '已完成'),
('XX诊所', '2025-01-16', 0.00, '已完成'),
('XX大药房', '2025-01-19', 0.00, '已完成'),
('XX卫生院', '2025-01-21', 0.00, '已完成'),
('XX医药超市', '2025-01-23', 0.00, '已完成'),
('XX康复医院', '2025-01-26', 0.00, '已完成'),
('XX中医馆', '2025-01-29', 0.00, '已完成'),
('XX儿童医院', '2025-02-02', 0.00, '已完成'),
('XX老年病医院', '2025-02-06', 0.00, '已完成'),
('XX平价药店', '2025-02-09', 0.00, '已完成'),
('XX专科医院', '2025-02-11', 0.00, '已完成'),
('XX养生会所', '2025-02-13', 0.00, '已完成'),
('XX企事业单位医务室', '2025-02-16', 0.00, '已完成'),
('XX宠物医院', '2025-02-19', 0.00, '已完成'),
('XX校医院', '2025-02-21', 0.00, '已完成'),
('XX养老院', '2025-02-23', 0.00, '已完成'),
('XX线上医药平台', '2025-03-01', 0.00, '待出库');

-- 7. 销售单明细表 (sales_detail) 初始数据 (60+条，1个销售单对应多条明细，单价与medicine一致)
INSERT INTO sales_detail (sales_id, medicine_id, quantity, unit_price, remark) VALUES
-- 销售单1：3条明细
(1, 1, 150, 12.50, '常规销售'),
(1, 3, 200, 9.90, '常规销售'),
(1, 18, 300, 6.50, '常规销售'),
-- 销售单2：3条明细
(2, 2, 100, 18.80, '常规销售'),
(2, 5, 80, 28.50, '常规销售'),
(2, 10, 60, 29.80, '常规销售'),
-- 销售单3：3条明细
(3, 4, 50, 35.60, '常规销售'),
(3, 7, 90, 22.30, '常规销售'),
(3, 12, 70, 32.90, '常规销售'),
-- 销售单4：3条明细
(4, 6, 80, 45.80, '常规销售'),
(4, 14, 100, 19.80, '常规销售'),
(4, 20, 50, 42.80, '常规销售'),
-- 销售单5：3条明细
(5, 8, 120, 16.70, '常规销售'),
(5, 9, 150, 8.50, '常规销售'),
(5, 13, 200, 5.80, '常规销售'),
-- 销售单6：3条明细
(6, 11, 90, 25.60, '常规销售'),
(6, 16, 80, 21.50, '常规销售'),
(6, 17, 70, 38.90, '常规销售'),
-- 销售单7：3条明细
(7, 15, 100, 15.20, '常规销售'),
(7, 19, 180, 4.90, '常规销售'),
(7, 1, 80, 12.50, '补货销售'),
-- 销售单8：3条明细
(8, 21, 60, 30.50, '常规销售'),
(8, 4, 40, 35.60, '补货销售'),
(8, 8, 50, 16.70, '补货销售'),
-- 销售单9：3条明细
(9, 2, 70, 18.80, '补货销售'),
(9, 6, 50, 45.80, '补货销售'),
(9, 10, 40, 29.80, '补货销售'),
-- 销售单10：3条明细
(10, 3, 120, 9.90, '补货销售'),
(10, 5, 60, 28.50, '补货销售'),
(10, 7, 80, 22.30, '补货销售'),
-- 销售单11：3条明细
(11, 12, 90, 32.90, '常规销售'),
(11, 14, 70, 19.80, '常规销售'),
(11, 18, 150, 6.50, '常规销售'),
-- 销售单12：3条明细
(12, 16, 100, 21.50, '常规销售'),
(12, 17, 80, 38.90, '常规销售'),
(12, 20, 60, 42.80, '常规销售'),
-- 销售单13：3条明细
(13, 1, 90, 12.50, '常规销售'),
(13, 9, 120, 8.50, '常规销售'),
(13, 13, 180, 5.80, '常规销售'),
-- 销售单14：3条明细
(14, 2, 80, 18.80, '常规销售'),
(14, 4, 60, 35.60, '常规销售'),
(14, 15, 90, 15.20, '常规销售'),
-- 销售单15：3条明细
(15, 5, 70, 28.50, '常规销售'),
(15, 8, 90, 16.70, '常规销售'),
(15, 19, 120, 4.90, '常规销售'),
-- 销售单16：3条明细
(16, 7, 100, 22.30, '常规销售'),
(16, 11, 80, 25.60, '常规销售'),
(16, 21, 70, 30.50, '常规销售'),
-- 销售单17：3条明细
(17, 6, 90, 45.80, '常规销售'),
(17, 10, 70, 29.80, '常规销售'),
(17, 12, 80, 32.90, '常规销售'),
-- 销售单18：3条明细
(18, 3, 150, 9.90, '常规销售'),
(18, 13, 200, 5.80, '常规销售'),
(18, 18, 100, 6.50, '常规销售'),
-- 销售单19：3条明细
(19, 4, 80, 35.60, '常规销售'),
(19, 16, 90, 21.50, '常规销售'),
(19, 17, 60, 38.90, '常规销售'),
-- 销售单20：4条明细（待出库）
(20, 1, 120, 12.50, '线上订单'),
(20, 2, 80, 18.80, '线上订单'),
(20, 5, 70, 28.50, '线上订单'),
(20, 21, 100, 30.50, '线上订单');

-- 关键：自动更新采购单总金额（与明细表汇总一致）
UPDATE purchase_order po
JOIN (
    SELECT purchase_id, SUM(amount) AS total_amount
    FROM purchase_detail
    GROUP BY purchase_id
) pd ON po.purchase_id = pd.purchase_id
SET po.total_amount = pd.total_amount;

-- 关键：自动更新销售单总金额（与明细表汇总一致）
UPDATE sales_order so
JOIN (
    SELECT sales_id, SUM(amount) AS total_amount
    FROM sales_detail
    GROUP BY sales_id
) sd ON so.sales_id = sd.sales_id
SET so.total_amount = sd.total_amount;

-- ============================================
-- 数据验证查询
-- ============================================

SELECT '药品表记录数' AS 表名, COUNT(*) AS 记录数 FROM medicine
UNION ALL
SELECT '供应商表记录数', COUNT(*) FROM supplier
UNION ALL
SELECT '库存表记录数', COUNT(*) FROM stock
UNION ALL
SELECT '采购单表记录数', COUNT(*) FROM purchase_order
UNION ALL
SELECT '采购明细表记录数', COUNT(*) FROM purchase_detail
UNION ALL
SELECT '销售单表记录数', COUNT(*) FROM sales_order
UNION ALL
SELECT '销售明细表记录数', COUNT(*) FROM sales_detail
UNION ALL
SELECT '用户表记录数', COUNT(*) FROM system_user;

-- 验证各表记录数
SELECT 
    (SELECT COUNT(*) FROM medicine) AS 药品数量,
    (SELECT COUNT(*) FROM supplier) AS 供应商数量,
    (SELECT COUNT(*) FROM stock) AS 库存记录数,
    (SELECT COUNT(*) FROM purchase_order) AS 采购单数量,
    (SELECT COUNT(*) FROM purchase_detail) AS 采购明细数,
    (SELECT COUNT(*) FROM sales_order) AS 销售单数量,
    (SELECT COUNT(*) FROM sales_detail) AS 销售明细数,
    (SELECT COUNT(*) FROM system_user) AS 用户数量;