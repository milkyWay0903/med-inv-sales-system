-- 重置数据
-- 重置表
TRUNCATE TABLE medicine;
-- 禁用外键
SET FOREIGN_KEY_CHECKS = 0;
-- 启用外键
SET FOREIGN_KEY_CHECKS = 1;


SELECT * FROM medicine;

-- 用户表添加数据（用于登录）
INSERT INTO system_user (username, password, role, real_name) VALUES ('zhangsan', '123456', 'admin', '张三');
