/*
 Navicat Premium Data Transfer

 Source Server         : docker-mysql8
 Source Server Type    : MySQL
 Source Server Version : 80031
 Source Host           : localhost:3306
 Source Schema         : campus_courier

 Target Server Type    : MySQL
 Target Server Version : 80031
 File Encoding         : 65001

 Date: 25/02/2025 19:41:26
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_admin
-- ----------------------------
DROP TABLE IF EXISTS `sys_admin`;
CREATE TABLE `sys_admin`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '头像URL',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态：1-正常，0-禁用',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统管理员表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_admin
-- ----------------------------
INSERT INTO `sys_admin` VALUES (1, 'admin', 'e10adc3949ba59abbe56e057f20f883e', '系统管理员', 'http://127.0.0.1:9001/static/images/33f439b8-36b1-4651-b254-1fb24589e310.png', 1, '2025-02-14 06:50:21');
INSERT INTO `sys_admin` VALUES (3, 'manager', 'e10adc3949ba59abbe56e057f20f883e', '系统管理员', 'http://127.0.0.1:9001/static/images/33f439b8-36b1-4651-b254-1fb24589e310.png', 1, '2024-01-02 11:30:00');
INSERT INTO `sys_admin` VALUES (4, 'zhangsan', 'e10adc3949ba59abbe56e057f20f883e', '张三', 'http://127.0.0.1:9001/static/images/33f439b8-36b1-4651-b254-1fb24589e310.png', 1, '2024-01-03 14:20:00');
INSERT INTO `sys_admin` VALUES (5, 'lisi', 'e10adc3949ba59abbe56e057f20f883e', '李四', 'http://127.0.0.1:9001/static/images/33f439b8-36b1-4651-b254-1fb24589e310.png', 0, '2024-01-04 09:15:00');
INSERT INTO `sys_admin` VALUES (6, 'wangwu', 'e10adc3949ba59abbe56e057f20f883e', '王五', 'http://127.0.0.1:9001/static/images/33f439b8-36b1-4651-b254-1fb24589e310.png', 1, '2024-01-05 16:40:00');
INSERT INTO `sys_admin` VALUES (7, 'zhaoliu', 'e10adc3949ba59abbe56e057f20f883e', '赵六', 'http://127.0.0.1:9001/static/images/33f439b8-36b1-4651-b254-1fb24589e310.png', 0, '2024-01-06 13:25:00');
INSERT INTO `sys_admin` VALUES (8, 'qianqi', 'e10adc3949ba59abbe56e057f20f883e', '钱七', 'http://127.0.0.1:9001/static/images/33f439b8-36b1-4651-b254-1fb24589e310.png', 1, '2024-01-07 11:10:00');
INSERT INTO `sys_admin` VALUES (9, 'sunba', 'e10adc3949ba59abbe56e057f20f883e', '孙八', 'http://127.0.0.1:9001/static/images/33f439b8-36b1-4651-b254-1fb24589e310.png', 0, '2024-01-08 15:50:00');
INSERT INTO `sys_admin` VALUES (10, 'zhoujiu', 'e10adc3949ba59abbe56e057f20f883e', '周九', 'http://127.0.0.1:9001/static/images/33f439b8-36b1-4651-b254-1fb24589e310.png', 1, '2024-01-09 10:30:00');
INSERT INTO `sys_admin` VALUES (11, 'wushi', 'e10adc3949ba59abbe56e057f20f883e', '吴十', 'http://127.0.0.1:9001/static/images/33f439b8-36b1-4651-b254-1fb24589e310.png', 0, '2024-01-10 14:45:00');
INSERT INTO `sys_admin` VALUES (12, 'zhengyi', 'e10adc3949ba59abbe56e057f20f883e', '郑一', 'http://127.0.0.1:9001/static/images/33f439b8-36b1-4651-b254-1fb24589e310.png', 1, '2024-01-11 09:20:00');
INSERT INTO `sys_admin` VALUES (13, 'wanger', 'e10adc3949ba59abbe56e057f20f883e', '王二', 'http://127.0.0.1:9001/static/images/33f439b8-36b1-4651-b254-1fb24589e310.png', 0, '2024-01-12 16:15:00');
INSERT INTO `sys_admin` VALUES (14, 'chensan', 'e10adc3949ba59abbe56e057f20f883e', '陈三', 'http://127.0.0.1:9001/static/images/33f439b8-36b1-4651-b254-1fb24589e310.png', 1, '2024-01-13 11:40:00');
INSERT INTO `sys_admin` VALUES (15, 'yangsi', 'e10adc3949ba59abbe56e057f20f883e', '杨四', 'http://127.0.0.1:9001/static/images/33f439b8-36b1-4651-b254-1fb24589e310.png', 0, '2024-01-14 13:55:00');
INSERT INTO `sys_admin` VALUES (16, 'liuwu', 'e10adc3949ba59abbe56e057f20f883e', '刘五A3', 'http://127.0.0.1:9001/static/images/33f439b8-36b1-4651-b254-1fb24589e310.png', 1, '2024-01-15 15:30:00');

-- ----------------------------
-- Table structure for sys_student
-- ----------------------------
DROP TABLE IF EXISTS `sys_student`;
CREATE TABLE `sys_student`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `student_no` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '学号（登录账号）',
  `password` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '姓名',
  `gender` tinyint(1) NOT NULL DEFAULT 1 COMMENT '性别：1-男，2-女',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像URL',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `department` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '院系',
  `major` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '专业',
  `grade` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '年级',
  `class_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '班级',
  `enrollment_year` int(0) NOT NULL COMMENT '入学年份',
  `identity_type` tinyint(0) NOT NULL COMMENT '身份类型',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态：1-正常，0-禁用',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '学生信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_student
-- ----------------------------
INSERT INTO `sys_student` VALUES (15, '202413422342', 'a198b5dce46e01d65288e9ac0035a7a0', '王溜溜', 1, 'http://127.0.0.1:9001/static/images/33f439b8-36b1-4651-b254-1fb24589e310.png', 'qweasd@qq.com', '理学院', '化学', '2024级', '化学2401班', 2024, 4, 1, '2025-02-15 17:55:10');
INSERT INTO `sys_student` VALUES (17, '20200102', '20200102L', '李四', 2, NULL, 'lisi@example.com', '信息科学与工程学院', '计算机科学与技术', '2023级', '计科2301班', 2023, 4, 1, '2025-02-15 17:55:48');
INSERT INTO `sys_student` VALUES (18, '20200103', '8ad203790042a5d5eb3b682e0c188c9b', '王五', 1, 'http://127.0.0.1:9001/static/images/33f439b8-36b1-4651-b254-1fb24589e310.png', '1wangwu@example.com', '信息科学与工程学院', '计算机科学与技术', '2023级', '计科2301班', 2023, 4, 1, '2025-02-15 17:55:48');
INSERT INTO `sys_student` VALUES (19, '20200201', '072bc5a7fb648b0fe67a4d57e2ce09e3', '赵六', 2, NULL, 'zhaoliu@example.com', '信息科学与工程学院', '计算机科学与技术', '2023级', '计科2301班', 2023, 4, 1, '2025-02-15 17:55:48');
INSERT INTO `sys_student` VALUES (20, '20200202', '1dcd326eb25842fe087eca055ec599f2', '钱七', 1, NULL, 'qianqi@example.com', '信息科学与工程学院', '计算机科学与技术', '2023级', '计科2301班', 2023, 4, 1, '2025-02-15 17:55:48');
INSERT INTO `sys_student` VALUES (21, '20210101', '06fb8c6aacc8c25e6eef07703894ccc3', '孙八', 2, NULL, 'sunba@example.com', '信息科学与工程学院', '计算机科学与技术', '2023级', '计科2302班', 2023, 4, 1, '2025-02-15 17:55:48');
INSERT INTO `sys_student` VALUES (22, '20210102', '2281532db98eb10945563ca5b4be0e59', '周九', 1, NULL, 'zhoujiu@example.com', '信息科学与工程学院', '计算机科学与技术', '2023级', '计科2302班', 2023, 4, 1, '2025-02-15 17:55:48');
INSERT INTO `sys_student` VALUES (23, '20210201', 'd2e51e667322337b41ee97984d6c76f7', '吴十', 2, NULL, 'wushi@example.com', '信息科学与工程学院', '计算机科学与技术', '2023级', '计科2302班', 2023, 4, 1, '2025-02-15 17:55:48');
INSERT INTO `sys_student` VALUES (24, '20210202', '1c47fbacb378e46ed3db1c9fa17894cc', '郑十一', 1, NULL, 'zheng11@example.com', '信息科学与工程学院', '计算机科学与技术', '2023级', '计科2302班', 2023, 4, 1, '2025-02-15 17:55:48');
INSERT INTO `sys_student` VALUES (25, '20220101', '9893b72c6c4243c0f88979cfe8682f1e', '王十二', 2, NULL, 'wang12@example.com', '信息科学与工程学院', '计算机科学与技术', '2023级', '计科2302班', 2023, 4, 1, '2025-02-15 17:55:48');
INSERT INTO `sys_student` VALUES (26, '20220102', '5aeb0ff370c065fa5b2f92132514d6fb', '李十三', 1, NULL, 'li13@example.com', '信息科学与工程学院', '计算机科学与技术', '2024级', '计科2403班', 2024, 4, 0, '2025-02-15 17:55:48');
INSERT INTO `sys_student` VALUES (27, '20220201', '2e2ef9057a96e8b26677985672aa5c76', '张十四', 2, NULL, 'zhang14@example.com', '信息科学与工程学院', '计算机科学与技术', '2024级', '计科2403班', 2024, 4, 1, '2025-02-15 17:55:48');
INSERT INTO `sys_student` VALUES (28, '20220202', '7186dde092fae447be6a242809b3a7a7', '刘十五', 1, NULL, 'liu15@example.com', '信息科学与工程学院', '计算机科学与技术', '2024级', '计科2403班', 2024, 4, 1, '2025-02-15 17:55:48');

-- ----------------------------
-- Table structure for sys_student_record
-- ----------------------------
DROP TABLE IF EXISTS `sys_student_record`;
CREATE TABLE `sys_student_record`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `student_id` int(0) NOT NULL COMMENT '学生ID',
  `type` tinyint(0) NOT NULL COMMENT '记录类型(1:创建账号,2:重置密码,3:加入社团,4:退出社团,5:身份变更,6:禁用账号,7:启用账号)',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '详细内容',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 56 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '学生记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_student_record
-- ----------------------------
INSERT INTO `sys_student_record` VALUES (1, 15, 1, '创建账号，初始密码为：202413422342W', '2025-02-15 09:55:09');
INSERT INTO `sys_student_record` VALUES (2, 16, 1, '创建账号，初始密码为：20200101Z', '2025-02-15 09:55:48');
INSERT INTO `sys_student_record` VALUES (3, 17, 1, '创建账号，初始密码为：20200102L', '2025-02-15 09:55:48');
INSERT INTO `sys_student_record` VALUES (4, 18, 1, '创建账号，初始密码为：20200103W', '2025-02-15 09:55:48');
INSERT INTO `sys_student_record` VALUES (5, 19, 1, '创建账号，初始密码为：20200201Z', '2025-02-15 09:55:48');
INSERT INTO `sys_student_record` VALUES (6, 20, 1, '创建账号，初始密码为：20200202Q', '2025-02-15 09:55:48');
INSERT INTO `sys_student_record` VALUES (7, 21, 1, '创建账号，初始密码为：20210101S', '2025-02-15 09:55:48');
INSERT INTO `sys_student_record` VALUES (8, 22, 1, '创建账号，初始密码为：20210102Z', '2025-02-15 09:55:48');
INSERT INTO `sys_student_record` VALUES (9, 23, 1, '创建账号，初始密码为：20210201W', '2025-02-15 09:55:48');
INSERT INTO `sys_student_record` VALUES (10, 24, 1, '创建账号，初始密码为：20210202Z', '2025-02-15 09:55:48');
INSERT INTO `sys_student_record` VALUES (11, 25, 1, '创建账号，初始密码为：20220101W', '2025-02-15 09:55:48');
INSERT INTO `sys_student_record` VALUES (12, 26, 1, '创建账号，初始密码为：20220102L', '2025-02-15 09:55:48');
INSERT INTO `sys_student_record` VALUES (13, 27, 1, '创建账号，初始密码为：20220201Z', '2025-02-15 09:55:48');
INSERT INTO `sys_student_record` VALUES (14, 28, 1, '创建账号，初始密码为：20220202L', '2025-02-15 09:55:48');
INSERT INTO `sys_student_record` VALUES (15, 16, 2, '重置密码为：20200101Z', '2025-02-15 09:58:30');
INSERT INTO `sys_student_record` VALUES (16, 16, 4, '账号被禁用', '2025-02-15 09:58:37');
INSERT INTO `sys_student_record` VALUES (17, 16, 3, '账号被启用', '2025-02-15 09:58:40');
INSERT INTO `sys_student_record` VALUES (18, 16, 2, '重置密码为：20200101Z', '2025-02-15 10:05:33');
INSERT INTO `sys_student_record` VALUES (19, 16, 2, '重置密码为：20200101Z', '2025-02-15 10:28:37');
INSERT INTO `sys_student_record` VALUES (20, 17, 4, '账号被禁用', '2025-02-15 10:29:03');
INSERT INTO `sys_student_record` VALUES (21, 17, 2, '重置密码为：20200102L', '2025-02-15 10:29:08');
INSERT INTO `sys_student_record` VALUES (22, 17, 3, '账号被启用', '2025-02-16 04:00:29');
INSERT INTO `sys_student_record` VALUES (55, 17, 2, '重置密码为：20200102L', '2025-02-23 06:30:59');

-- ----------------------------
-- Table structure for sys_task_types
-- ----------------------------
DROP TABLE IF EXISTS `sys_task_types`;
CREATE TABLE `sys_task_types`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '任务类型ID',
  `type_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '类型名称',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '类型图标',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '类型描述',
  `priority` int(0) NOT NULL DEFAULT 0 COMMENT '优先级(数字越大优先级越高)',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态(0-禁用,1-启用)',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '任务类型表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_task_types
-- ----------------------------
INSERT INTO `sys_task_types` VALUES (1, '外卖配送', 'https://cdn-icons-png.flaticon.com/128/7835/7835563.png', '提供专业的外卖配送服务，满足校园内快递相关需求，快速便捷。', 20, 1, '2025-02-25 19:19:47');
INSERT INTO `sys_task_types` VALUES (2, '快递代取', 'https://cdn-icons-png.flaticon.com/128/2271/2271048.png', '帮助取回快递包裹，指定时间送达', 95, 1, '2025-02-25 11:40:16');
INSERT INTO `sys_task_types` VALUES (3, '代寄快递', 'https://cdn-icons-png.flaticon.com/128/4305/4305512.png', '帮助打包并寄送快递，提供全流程服务', 88, 1, '2025-02-25 11:40:16');
INSERT INTO `sys_task_types` VALUES (4, '生鲜配送', 'https://cdn-icons-png.flaticon.com/128/3724/3724788.png', '新鲜水果蔬菜配送到宿舍', 75, 0, '2025-02-25 11:40:16');
INSERT INTO `sys_task_types` VALUES (5, '大件物流', 'https://cdn-icons-png.flaticon.com/128/3028/3028626.png', '大型物品搬运和配送服务', 82, 1, '2025-02-25 11:40:16');
INSERT INTO `sys_task_types` VALUES (6, '快递柜代取', 'https://cdn-icons-png.flaticon.com/128/8872/8872558.png', '帮助从校内自提柜取件', 90, 1, '2025-02-25 11:40:16');
INSERT INTO `sys_task_types` VALUES (7, '文档打印', 'https://cdn-icons-png.flaticon.com/128/3094/3094837.png', '黑白/彩色文档打印及装订服务', 85, 1, '2025-02-25 11:40:16');
INSERT INTO `sys_task_types` VALUES (8, '照片打印', 'https://cdn-icons-png.flaticon.com/128/1484/1484262.png', '高质量照片冲印服务', 70, 1, '2025-02-25 11:40:16');
INSERT INTO `sys_task_types` VALUES (9, '海报印刷', 'https://cdn-icons-png.flaticon.com/128/1355/1355394.png', '大型海报、展板设计与打印', 65, 0, '2025-02-25 11:40:16');
INSERT INTO `sys_task_types` VALUES (10, '论文打印', 'https://cdn-icons-png.flaticon.com/128/3497/3497086.png', '毕业论文打印及装订全套服务', 88, 1, '2025-02-25 11:40:16');
INSERT INTO `sys_task_types` VALUES (11, '证件照打印', 'https://cdn-icons-png.flaticon.com/128/2659/2659360.png', '标准证件照拍摄与打印', 78, 0, '2025-02-25 11:40:16');
INSERT INTO `sys_task_types` VALUES (12, '食堂打饭', 'https://cdn-icons-png.flaticon.com/128/1147/1147805.png', '校内食堂餐点配送到宿舍或教学楼', 92, 1, '2025-02-25 11:40:16');
INSERT INTO `sys_task_types` VALUES (13, '奶茶外卖', 'https://cdn-icons-png.flaticon.com/128/5742/5742527.png', '校内外奶茶店饮品配送服务', 94, 1, '2025-02-25 11:40:16');
INSERT INTO `sys_task_types` VALUES (14, '夜宵配送', 'https://cdn-icons-png.flaticon.com/128/6147/6147137.png', '深夜食品、零食配送服务', 86, 1, '2025-02-25 11:40:16');
INSERT INTO `sys_task_types` VALUES (15, '水果配送', 'https://cdn-icons-png.flaticon.com/128/3194/3194591.png', '新鲜水果购买与配送', 76, 0, '2025-02-25 11:40:16');
INSERT INTO `sys_task_types` VALUES (16, '蛋糕订制', 'https://cdn-icons-png.flaticon.com/128/3348/3348089.png', '生日、节日蛋糕预订与配送', 72, 1, '2025-02-25 11:40:16');
INSERT INTO `sys_task_types` VALUES (17, '资料代借', 'https://cdn-icons-png.flaticon.com/128/2232/2232688.png', '图书馆图书代借代还服务', 83, 1, '2025-02-25 11:40:16');
INSERT INTO `sys_task_types` VALUES (18, '笔记整理', 'https://cdn-icons-png.flaticon.com/128/2119/2119751.png', '课堂笔记整理与分享', 77, 0, '2025-02-25 11:40:16');
INSERT INTO `sys_task_types` VALUES (19, '考研资料', 'https://cdn-icons-png.flaticon.com/128/3049/3049490.png', '考研复习资料收集与分享', 80, 1, '2025-02-25 11:40:16');
INSERT INTO `sys_task_types` VALUES (20, '自习位代抢', 'https://cdn-icons-png.flaticon.com/128/3028/3028155.png', '图书馆、自习室座位预占服务', 89, 1, '2025-02-25 11:40:16');
INSERT INTO `sys_task_types` VALUES (21, '作业辅导', 'https://cdn-icons-png.flaticon.com/128/2641/2641409.png', '各学科作业解答与辅导', 81, 0, '2025-02-25 11:40:16');
INSERT INTO `sys_task_types` VALUES (22, '宿舍代收', 'https://cdn-icons-png.flaticon.com/128/1670/1670080.png', '宿舍物品接收与保管服务', 87, 1, '2025-02-25 11:40:16');
INSERT INTO `sys_task_types` VALUES (23, '代跑腿', 'https://cdn-icons-png.flaticon.com/128/2382/2382533.png', '校内外各类跑腿、办事服务', 93, 1, '2025-02-25 11:40:16');
INSERT INTO `sys_task_types` VALUES (24, '洗衣服务', 'https://cdn-icons-png.flaticon.com/128/2438/2438078.png', '衣物代送洗衣店与取回服务', 79, 0, '2025-02-25 11:40:16');
INSERT INTO `sys_task_types` VALUES (25, '充值服务', 'https://cdn-icons-png.flaticon.com/128/4722/4722922.png', '校园卡、水电费等充值服务', 84, 1, '2025-02-25 11:40:16');
INSERT INTO `sys_task_types` VALUES (26, '搬运服务', 'https://cdn-icons-png.flaticon.com/128/4216/4216805.png', '宿舍搬迁、大型物品搬运', 74, 1, '2025-02-25 11:40:16');
INSERT INTO `sys_task_types` VALUES (27, '运动陪练', 'https://cdn-icons-png.flaticon.com/128/2964/2964514.png', '各类运动项目陪练服务', 68, 0, '2025-02-25 11:40:16');
INSERT INTO `sys_task_types` VALUES (28, '场地预约', 'https://cdn-icons-png.flaticon.com/128/2043/2043006.png', '体育场地代预约服务', 73, 1, '2025-02-25 11:40:16');
INSERT INTO `sys_task_types` VALUES (29, '体育用品', 'https://cdn-icons-png.flaticon.com/128/706/706164.png', '体育用品租借与配送', 67, 1, '2025-02-25 11:40:16');
INSERT INTO `sys_task_types` VALUES (30, '跑步陪跑', 'https://cdn-icons-png.flaticon.com/128/4070/4070388.png', '晨跑/夜跑陪跑与指导服务', 63, 0, '2025-02-25 11:40:16');
INSERT INTO `sys_task_types` VALUES (31, '电影票代订', 'https://cdn-icons-png.flaticon.com/128/3658/3658959.png', '校内外电影票预订服务', 71, 1, '2025-02-25 11:40:16');
INSERT INTO `sys_task_types` VALUES (32, '游戏陪玩', 'https://cdn-icons-png.flaticon.com/128/3097/3097980.png', '各类电子游戏陪玩服务', 69, 0, '2025-02-25 11:40:16');
INSERT INTO `sys_task_types` VALUES (33, '摄影服务', 'https://cdn-icons-png.flaticon.com/128/5585/5585517.png', '校园内专业摄影与后期服务', 66, 1, '2025-02-25 11:40:16');
INSERT INTO `sys_task_types` VALUES (34, 'K歌陪唱', 'https://cdn-icons-png.flaticon.com/128/9409/9409969.png', 'KTV陪唱与预订服务', 60, 0, '2025-02-25 11:40:16');
INSERT INTO `sys_task_types` VALUES (35, '自习室预约', 'https://cdn-icons-png.flaticon.com/128/4336/4336543.png', '校内自习室、研讨室预约服务', 86, 1, '2025-02-25 11:40:16');
INSERT INTO `sys_task_types` VALUES (36, '失物代寻', 'https://cdn-icons-png.flaticon.com/128/3209/3209207.png', '校内失物招领与查找服务', 81, 1, '2025-02-25 11:40:16');
INSERT INTO `sys_task_types` VALUES (37, '校园卡充值', 'https://cdn-icons-png.flaticon.com/128/5064/5064969.png', '校园卡线上/线下充值服务', 91, 1, '2025-02-25 11:40:16');
INSERT INTO `sys_task_types` VALUES (38, '校园引导', 'https://cdn-icons-png.flaticon.com/128/5167/5167630.png', '校园导览与新生引导服务', 64, 0, '2025-02-25 11:40:16');
INSERT INTO `sys_task_types` VALUES (39, '证明代办', 'https://cdn-icons-png.flaticon.com/128/2666/2666505.png', '学生证明文件代办服务', 78, 1, '2025-02-25 11:40:16');
INSERT INTO `sys_task_types` VALUES (40, '维修服务', 'https://cdn-icons-png.flaticon.com/128/4359/4359846.png', '电子设备、家具等维修服务', 77, 0, '2025-02-25 11:40:16');
INSERT INTO `sys_task_types` VALUES (41, '咨询解答', 'https://cdn-icons-png.flaticon.com/128/5726/5726775.png', '校园生活各类问题咨询服务', 75, 1, '2025-02-25 11:40:16');
INSERT INTO `sys_task_types` VALUES (42, '代领物资', 'https://cdn-icons-png.flaticon.com/128/2382/2382629.png', '校内各类物资代领服务', 82, 1, '2025-02-25 11:40:16');
INSERT INTO `sys_task_types` VALUES (43, '代跑签到', 'https://cdn-icons-png.flaticon.com/128/4712/4712142.png', '课程、活动代签到服务', 70, 0, '2025-02-25 11:40:16');

SET FOREIGN_KEY_CHECKS = 1;
