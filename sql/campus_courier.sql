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

 Date: 08/03/2025 21:52:10
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
INSERT INTO `sys_admin` VALUES (1, 'admin', 'e10adc3949ba59abbe56e057f20f883e', '系统管理员', 'http://127.0.0.1:9001/static/images/d711dfa8-b77b-43be-9dce-851c8eb12c38.png', 1, '2025-02-14 06:50:21');
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
-- Table structure for sys_balance_record
-- ----------------------------
DROP TABLE IF EXISTS `sys_balance_record`;
CREATE TABLE `sys_balance_record`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `student_id` int(0) NOT NULL COMMENT '学生ID',
  `amount` decimal(10, 2) NOT NULL COMMENT '变动金额',
  `type` tinyint(0) NOT NULL COMMENT '变动类型：1-充值，2-提现，3-收入，4-支出',
  `before_balance` decimal(10, 2) NOT NULL COMMENT '变动前余额',
  `after_balance` decimal(10, 2) NOT NULL COMMENT '变动后余额',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '变动说明',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '余额变动记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_balance_record
-- ----------------------------
INSERT INTO `sys_balance_record` VALUES (6, 28, 10.00, 1, 0.00, 10.00, '充值10.00元', '2025-02-28 15:24:29');
INSERT INTO `sys_balance_record` VALUES (7, 28, 10.00, 1, 10.00, 20.00, '充值10.00元', '2025-02-28 15:27:00');
INSERT INTO `sys_balance_record` VALUES (8, 28, 20.00, 4, 20.00, 0.00, '发布任务[排队喜茶],支付金额20.00元', '2025-03-01 04:22:26');
INSERT INTO `sys_balance_record` VALUES (9, 28, 200.00, 1, 0.00, 200.00, '充值200.00元', '2025-03-01 04:27:37');
INSERT INTO `sys_balance_record` VALUES (10, 28, 70.00, 4, 200.00, 130.00, '发布任务[我要学习],支付金额70.00元', '2025-03-01 04:28:32');
INSERT INTO `sys_balance_record` VALUES (11, 28, 20.00, 3, 130.00, 150.00, '取消任务[排队喜茶],退还金额20.00元', '2025-03-01 04:28:41');
INSERT INTO `sys_balance_record` VALUES (12, 27, 70.00, 3, 0.00, 70.00, '完成任务[我要学习],获得报酬70.00元', '2025-03-01 04:38:06');
INSERT INTO `sys_balance_record` VALUES (13, 27, 20.00, 3, 70.00, 90.00, '纠纷处理-发布者胜诉退款，任务：食堂窗口排队', '2025-03-01 11:26:36');
INSERT INTO `sys_balance_record` VALUES (14, 28, 22.00, 1, 150.00, 172.00, '充值22.00元', '2025-03-02 10:20:57');
INSERT INTO `sys_balance_record` VALUES (15, 28, 247.00, 3, 172.00, 419.00, '纠纷处理-跑腿者胜诉收款，任务：购买创新实验材料', '2025-03-02 14:11:37');
INSERT INTO `sys_balance_record` VALUES (16, 27, 30.00, 4, 90.00, 60.00, '发布任务[黄焖鸡],支付金额30.00元', '2025-03-08 13:00:33');
INSERT INTO `sys_balance_record` VALUES (17, 27, 30.00, 3, 60.00, 90.00, '纠纷处理-发布者胜诉退款，任务：黄焖鸡', '2025-03-08 13:13:57');

-- ----------------------------
-- Table structure for sys_dispute
-- ----------------------------
DROP TABLE IF EXISTS `sys_dispute`;
CREATE TABLE `sys_dispute`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `task_id` bigint(0) NOT NULL COMMENT '关联的任务ID',
  `initiator_id` bigint(0) NOT NULL COMMENT '发起人ID(学生ID)',
  `respondent_id` bigint(0) NOT NULL COMMENT '被投诉人ID(学生ID)',
  `type` tinyint(0) NOT NULL COMMENT '纠纷类型:1-送达与实际不符,2-态度恶劣,3-商品损坏,4-其他',
  `status` tinyint(0) NOT NULL DEFAULT 0 COMMENT '纠纷状态:0-待处理,1-处理中,2-已结束,3-已发布纠纷大厅',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '纠纷标题',
  `content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '纠纷内容描述',
  `evidence` json NULL COMMENT '证据图片列表,JSON数组存储图片URL',
  `admin_remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '管理员处理备注',
  `result` tinyint(0) NULL DEFAULT NULL COMMENT '处理结果:1-支持发起人,2-支持被投诉人',
  `compensation_amount` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '赔偿金额',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `deadline` datetime(0) NULL DEFAULT NULL COMMENT '纠结大厅截止时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单纠纷表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dispute
-- ----------------------------
INSERT INTO `sys_dispute` VALUES (1, 14, 27, 28, 5, 2, '送错了', '我要吃的，不要喝的', '[\"http://127.0.0.1:9001/static/images/357a04ec-015a-42f4-9369-04a86e0b3e4e.png\", \"http://127.0.0.1:9001/static/images/0a2d1a0d-f37a-44f8-9cd3-bfd475b2c147.png\"]', '发布者是合理的', 1, 0.00, '2025-02-28 15:00:15', '2025-03-01 19:15:14');
INSERT INTO `sys_dispute` VALUES (10, 11, 27, 28, 10, 2, '不合适 不行', '这是啥啊  这根本就不对 ', '[\"http://127.0.0.1:9001/static/images/5a5a9a88-be0a-460b-905b-6f38fc03324a.png\"]', '人家送的没错，就是这样', 2, 0.00, '2025-03-02 21:58:56', '2025-03-02 22:01:13');
INSERT INTO `sys_dispute` VALUES (11, 19, 27, 28, 10, 2, '价格不对', '多要我10块钱', '[\"http://127.0.0.1:9001/static/images/8433a713-d883-4fa6-b385-653b7167cc61.png\"]', '123213', 1, 0.00, '2025-03-08 21:08:01', '2025-03-08 21:12:12');

-- ----------------------------
-- Table structure for sys_dispute_support
-- ----------------------------
DROP TABLE IF EXISTS `sys_dispute_support`;
CREATE TABLE `sys_dispute_support`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `dispute_id` bigint(0) NOT NULL COMMENT '关联的纠纷ID',
  `student_id` bigint(0) NOT NULL COMMENT '支持者ID(学生ID)',
  `support_type` tinyint(0) NOT NULL COMMENT '支持类型:1-支持发布者,2-支持跑腿者',
  `reason` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '支持原因',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '纠纷支持表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dispute_support
-- ----------------------------
INSERT INTO `sys_dispute_support` VALUES (14, 10, 26, 2, '就是这样啊 没错的', '2025-03-02 22:08:22');
INSERT INTO `sys_dispute_support` VALUES (15, 11, 26, 1, '123123', '2025-03-08 21:11:36');

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
  `balance` decimal(19, 2) NOT NULL DEFAULT 0.00 COMMENT '账户余额',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '学生信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_student
-- ----------------------------
INSERT INTO `sys_student` VALUES (15, '202413422342', 'a198b5dce46e01d65288e9ac0035a7a0', '王溜溜', 1, 'http://127.0.0.1:9001/static/images/33f439b8-36b1-4651-b254-1fb24589e310.png', 'qweasd@qq.com', '理学院', '化学', '2024级', '化学2401班', 2024, 4, 1, '2025-02-15 17:55:10', 0.00);
INSERT INTO `sys_student` VALUES (17, '20200102', '573fbcaea229c2302cd7dac43ee95bcf', '李四', 2, NULL, 'lisi@example.com', '信息科学与工程学院', '计算机科学与技术', '2023级', '计科2301班', 2023, 4, 1, '2025-02-15 17:55:48', 0.00);
INSERT INTO `sys_student` VALUES (18, '20200103', '8ad203790042a5d5eb3b682e0c188c9b', '王五', 1, 'http://127.0.0.1:9001/static/images/33f439b8-36b1-4651-b254-1fb24589e310.png', '1wangwu@example.com', '信息科学与工程学院', '计算机科学与技术', '2023级', '计科2301班', 2023, 4, 1, '2025-02-15 17:55:48', 0.00);
INSERT INTO `sys_student` VALUES (19, '20200201', '072bc5a7fb648b0fe67a4d57e2ce09e3', '赵六', 2, NULL, 'zhaoliu@example.com', '信息科学与工程学院', '计算机科学与技术', '2023级', '计科2301班', 2023, 4, 1, '2025-02-15 17:55:48', 0.00);
INSERT INTO `sys_student` VALUES (20, '20200202', '1dcd326eb25842fe087eca055ec599f2', '钱七', 1, NULL, 'qianqi@example.com', '信息科学与工程学院', '计算机科学与技术', '2023级', '计科2301班', 2023, 4, 1, '2025-02-15 17:55:48', 0.00);
INSERT INTO `sys_student` VALUES (21, '20210101', '06fb8c6aacc8c25e6eef07703894ccc3', '孙八', 2, NULL, 'sunba@example.com', '信息科学与工程学院', '计算机科学与技术', '2023级', '计科2302班', 2023, 4, 1, '2025-02-15 17:55:48', 0.00);
INSERT INTO `sys_student` VALUES (22, '20210102', '2281532db98eb10945563ca5b4be0e59', '周九', 1, NULL, 'zhoujiu@example.com', '信息科学与工程学院', '计算机科学与技术', '2023级', '计科2302班', 2023, 4, 1, '2025-02-15 17:55:48', 0.00);
INSERT INTO `sys_student` VALUES (23, '20210201', 'd2e51e667322337b41ee97984d6c76f7', '吴十', 2, NULL, 'wushi@example.com', '信息科学与工程学院', '计算机科学与技术', '2023级', '计科2302班', 2023, 4, 1, '2025-02-15 17:55:48', 0.00);
INSERT INTO `sys_student` VALUES (24, '20210202', '20210202Z', '郑十一', 1, NULL, 'zheng11@example.com', '信息科学与工程学院', '计算机科学与技术', '2023级', '计科2302班', 2023, 4, 1, '2025-02-15 17:55:48', 0.00);
INSERT INTO `sys_student` VALUES (25, '20220101', '9893b72c6c4243c0f88979cfe8682f1e', '王十二', 2, NULL, 'wang12@example.com', '信息科学与工程学院', '计算机科学与技术', '2023级', '计科2302班', 2023, 4, 1, '2025-02-15 17:55:48', 0.00);
INSERT INTO `sys_student` VALUES (26, '20220102', '5aeb0ff370c065fa5b2f92132514d6fb', '李十三', 1, NULL, 'li13@example.com', '信息科学与工程学院', '计算机科学与技术', '2024级', '计科2403班', 2024, 4, 1, '2025-02-15 17:55:48', 0.00);
INSERT INTO `sys_student` VALUES (27, '20220201', '2e2ef9057a96e8b26677985672aa5c76', '张十四', 2, NULL, 'zhang14@example.com', '信息科学与工程学院', '计算机科学与技术', '2024级', '计科2403班', 2024, 4, 1, '2025-02-15 17:55:48', 90.00);
INSERT INTO `sys_student` VALUES (28, '20220202', '7186dde092fae447be6a242809b3a7a7', '刘十五', 1, 'http://127.0.0.1:9001/static/images/7a1c4c45-f252-4708-93ab-16ca0cbdd79e.png', 'liu15@example.com', '信息科学与工程学院', '计算机科学与技术', '2024级', '计科2403班', 2024, 4, 1, '2025-02-15 17:55:48', 419.00);

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
) ENGINE = InnoDB AUTO_INCREMENT = 58 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '学生记录表' ROW_FORMAT = Dynamic;

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
INSERT INTO `sys_student_record` VALUES (56, 17, 2, '重置密码为：20200102L', '2025-02-28 12:39:00');
INSERT INTO `sys_student_record` VALUES (57, 24, 2, '重置密码为：20210202Z', '2025-02-28 12:39:08');
INSERT INTO `sys_student_record` VALUES (58, 17, 2, '重置密码为：20200102L', '2025-02-28 12:41:52');

-- ----------------------------
-- Table structure for sys_task
-- ----------------------------
DROP TABLE IF EXISTS `sys_task`;
CREATE TABLE `sys_task`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `task_no` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '任务编号',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '任务标题',
  `description` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '任务详细描述',
  `publisher_id` int(0) NOT NULL COMMENT '发布者用户ID',
  `runner_id` int(0) NULL DEFAULT NULL COMMENT '接单者用户ID',
  `task_type_id` int(0) NOT NULL COMMENT '任务类型ID',
  `status` tinyint(0) NOT NULL DEFAULT 0 COMMENT '任务状态：0-待接单，1-已接单，2-进行中，3-待确认，4-已完成，5-已取消，6-已过期，7-存在纠纷',
  `reward` decimal(10, 2) NOT NULL COMMENT '任务报酬',
  `urgent` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否加急：0-否，1-是',
  `urgent_reward` decimal(10, 2) NULL DEFAULT NULL COMMENT '加急费',
  `pickup_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '取件地址',
  `delivery_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '送达地址',
  `estimated_weight` decimal(5, 2) NULL DEFAULT NULL COMMENT '预估重量(kg)',
  `estimated_size` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '预估大小（如小、中、大）',
  `expected_finish_time` datetime(0) NOT NULL COMMENT '预期完成时间',
  `contact_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '联系人姓名',
  `contact_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '联系电话',
  `images` json NULL COMMENT '图片(JSON格式)',
  `notes` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注说明',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '任务表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_task
-- ----------------------------
INSERT INTO `sys_task` VALUES (2, 'TASK2025022612520384', '帮我取肯德基晚餐', '在学校西门肯德基取一份晚餐，包含：1份原味鸡桶、2份薯条、1份可乐', 28, NULL, 1, 0, 15.00, 1, 5.00, '学校西门肯德基店', '第三宿舍楼B栋428', 2.50, '中件', '2025-02-26 19:00:00', '王同学', '13812345678', '[]', '请尽快送达，肯德基店已经下单，取餐码：A3842', '2025-02-26 12:52:03');
INSERT INTO `sys_task` VALUES (3, 'TASK2025022612540912', '取快递5个（都是小件）', '有5个快递在菜鸟驿站，都是小件衣服，驿站柜台取件，取件码已经发到手机', 28, NULL, 2, 0, 20.00, 0, NULL, '北区菜鸟驿站', '研究生公寓3号楼503室', 3.00, '中件', '2025-02-26 18:00:00', '李研究生', '13923456789', '[]', '取件码会私发，请轻拿轻放，谢谢！', '2025-02-26 12:54:09');
INSERT INTO `sys_task` VALUES (4, 'TASK2025022612562145', '食堂排队买饭', '今天晚饭时间食堂人很多，帮忙排队买份鱼香肉丝饭和一份紫菜汤', 28, NULL, 3, 0, 12.00, 1, 8.00, '第一食堂二楼', '教学楼B201', 1.00, '小件', '2025-02-26 17:30:00', '陈老师', '13687654321', '[]', '上课前需要吃到饭，请务必在5:30前送到', '2025-02-26 12:56:21');
INSERT INTO `sys_task` VALUES (5, 'TASK2025022613014587', '帮搬新买的显示器', '刚买的27寸显示器需要从快递点搬到宿舍，比较重要小心轻放', 28, NULL, 5, 0, 35.00, 0, NULL, '南门京东快递点', '男生宿舍12栋305', 5.20, '大件', '2025-02-26 16:00:00', '张同学', '13598765432', '[]', '纸箱上有易碎标识，请小心搬运，不要磕碰', '2025-02-26 13:01:45');
INSERT INTO `sys_task` VALUES (6, 'TASK2025022613043278', '收十本教材', '辅导员让去领教材，太重了搬不动，麻烦帮忙取一下', 28, NULL, 2, 0, 25.00, 0, NULL, '教材中心', '第二教学楼508', 8.00, '大件', '2025-02-26 17:00:00', '赵学委', '13776543210', '[]', '需要登记学号和姓名，会提前给你', '2025-02-26 13:04:32');
INSERT INTO `sys_task` VALUES (7, 'TASK2025022613061953', '急需打印论文初稿', '有一份毕业论文初稿需要打印，共计35页，双面打印，彩色封面', 28, NULL, 7, 0, 15.00, 1, 10.00, '图书馆打印店', '研究生公寓5栋204', 0.20, '小件', '2025-02-26 15:30:00', '吴硕士', '13387654321', '[\"http://127.0.0.1:9001/static/images/33f439b8-36b1-4651-b254-1fb24589e310.png\"]', '文件已经发到邮箱，请打印完整，需要装订', '2025-02-26 13:06:19');
INSERT INTO `sys_task` VALUES (8, 'TASK2025022613082760', '宵夜烧烤外卖', '想吃烧烤，麻烦帮忙从校外带份宵夜，10串羊肉串，5串鸡翅，2份烤馕', 28, NULL, 14, 0, 20.00, 0, NULL, '校门口老王烧烤店', '第四宿舍楼A座506', 1.50, '小件', '2025-02-26 22:00:00', '刘夜猫', '13612345678', '[]', '记得多要些辣椒和烧烤酱，谢谢！', '2025-02-26 13:08:27');
INSERT INTO `sys_task` VALUES (9, 'TASK2025022613101538', '明早占自习室座位', '明早有重要考试，需要占个靠窗座位复习，图书馆5楼自习室', 28, NULL, 20, 0, 30.00, 1, 15.00, '图书馆', '图书馆5楼东侧自习室', NULL, NULL, '2025-02-27 07:00:00', '孙考生', '13898765432', '[]', '请尽可能占到靠窗位置，放一本红色笔记本和一支笔作为标记', '2025-02-26 13:10:15');
INSERT INTO `sys_task` VALUES (10, 'TASK2025022613123425', '帮带午餐盖饭', '上课没时间吃饭，帮忙带份黄焖鸡米饭，微辣，加个卤蛋', 28, NULL, 22, 0, 12.00, 1, 15.00, '北门黄焖鸡米饭店', '教学楼C区304教室', 0.80, '小件', '2025-02-26 11:30:00', '钱同学', '13512345678', '[]', '送餐时请轻声，课堂不要打扰', '2025-02-26 13:12:34');
INSERT INTO `sys_task` VALUES (11, 'TASK2025022613154267', '购买创新实验材料', '需要去文具店购买一些创新实验材料，清单已经列好', 27, 28, 23, 8, 25.00, 1, 222.00, '校外新华文具店', '工程学院实验楼204', 1.20, '中件', '2025-02-26 16:30:00', '杨教授', '13698765432', '[\"http://127.0.0.1:9001/static/images/33f439b8-36b1-4651-b254-1fb24589e310.png\"]', '采购清单已上传，如有缺货请电话联系确认是否替换', '2025-02-26 13:15:42');
INSERT INTO `sys_task` VALUES (12, 'TASK2025022613182039', '中午奶茶配送', '帮忙买5杯奶茶，给实验室小伙伴们解暑', 27, NULL, 1, 5, 15.00, 0, NULL, '东门蜜雪冰城', '理工楼B区502实验室', 1.50, '中件', '2025-02-26 13:45:00', '周组长', '13876543210', '[]', '5杯不同口味：2杯多肉葡萄，1杯芋泥波波，2杯椰椰奶冻，糖度冰度常规', '2025-02-26 13:18:20');
INSERT INTO `sys_task` VALUES (13, 'TASK2025022613202354', '打印课程PPT和资料', '老师课件和复习资料打印，大约80页，黑白双面打印', 27, NULL, 7, 0, 20.00, 0, NULL, '教学楼下打印店', '宿舍9栋328', 0.50, '小件', '2025-02-26 19:00:00', '郑同学', '13912345678', '[\"http://127.0.0.1:9001/static/images/33f439b8-36b1-4651-b254-1fb24589e310.png\"]', '文件已压缩上传，请全部打印出来并装订成册', '2025-02-26 13:20:23');
INSERT INTO `sys_task` VALUES (14, 'TASK2025022613224183', '食堂窗口排队', '中午食堂特价窗口人太多，帮忙排队买份红烧排骨套餐', 27, 28, 3, 5, 15.00, 1, 5.00, '第二食堂一楼特价窗口', '学生活动中心排练室', 0.80, '小件', '2025-02-26 12:00:00', '冯社长', '13887654321', '[]', '今天中午有特价活动，可能需要排队15-20分钟，辛苦了', '2025-02-26 13:22:41');
INSERT INTO `sys_task` VALUES (15, 'TASK2025022613252697', '借篮球场地并买水', '帮忙去体育馆借一个篮球并预约半场地，顺便带两瓶水', 27, 28, 29, 8, 20.00, 0, NULL, '大学生活动中心', '篮球场', 1.50, '中件', '2025-02-26 15:00:00', '潘队长', '13698765432', '[]', '需要用学生证借篮球，我的学生证会给你，请帮忙预约16:00-18:00的场地', '2025-02-26 13:25:26');
INSERT INTO `sys_task` VALUES (16, 'TASK2025022613281504', '取三个京东快递', '帮忙取三个京东快递，都是衣服和书籍', 28, NULL, 2, 5, 18.00, 0, NULL, '西区京东派送点', '文科楼教师办公室302', 4.00, '中件', '2025-02-26 16:30:00', '何老师', '13566778899', '[]', '取件码将通过短信发送，请送到办公室，如果我不在请放前台', '2025-02-26 13:28:15');
INSERT INTO `sys_task` VALUES (17, 'TASK202503011222267674', '排队喜茶', '喜茶出了新品，需要进行排队，大概五个小时', 28, NULL, 13, 5, 20.00, 0, 0.00, '喜茶 西门店', '宿舍楼101', 0.20, '小件', '2025-03-01 13:00:00', '张三', '15612345678', '[\"http://127.0.0.1:9001/static/images/33f439b8-36b1-4651-b254-1fb24589e310.png\"]', '一定要坚持，加油', '2025-03-01 12:22:27');
INSERT INTO `sys_task` VALUES (18, 'TASK202503011228326816', '我要学习', '我需要抢位置，抢一个好位置', 28, 27, 20, 8, 50.00, 1, 20.00, '图书馆', '图书馆', 0.00, '小件', '2025-04-01 00:00:00', '王五', '15612312312', '[\"http://127.0.0.1:9001/static/images/33f439b8-36b1-4651-b254-1fb24589e310.png\"]', '', '2025-03-01 12:28:33');
INSERT INTO `sys_task` VALUES (19, 'TASK202503082100332043', '黄焖鸡', '我要吃黄焖鸡', 27, 28, 12, 5, 20.00, 1, 10.00, '食堂', '宿舍', 1.00, '小件', '2025-03-09 12:00:00', '张三', '15611111111', '[\"http://tmp/AOoCIw8vN5xda51175ff7b1001c440a17f1fa9ce710e.png\"]', '微辣', '2025-03-08 21:00:34');

-- ----------------------------
-- Table structure for sys_task_evaluation
-- ----------------------------
DROP TABLE IF EXISTS `sys_task_evaluation`;
CREATE TABLE `sys_task_evaluation`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `task_id` int(0) NOT NULL COMMENT '任务ID',
  `publisher_id` int(0) NOT NULL COMMENT '发布者ID（评价人）',
  `runner_id` int(0) NOT NULL COMMENT '跑腿者ID（被评价人）',
  `score` decimal(2, 1) NOT NULL COMMENT '评分（1-5分，支持半分）',
  `content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '评价内容',
  `tags` json NULL COMMENT '评价标签（如：准时、态度好、服务周到等）',
  `anonymous` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否匿名评价（0-否，1-是）',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '任务评价表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_task_evaluation
-- ----------------------------
INSERT INTO `sys_task_evaluation` VALUES (1, 15, 27, 28, 5.0, '非常棒的', '[\"服务周到\", \"态度友好\"]', 0, '2025-02-27 21:30:24');
INSERT INTO `sys_task_evaluation` VALUES (5, 18, 28, 27, 5.0, '', '[\"态度友好\", \"沟通顺畅\"]', 0, '2025-03-01 12:38:42');
INSERT INTO `sys_task_evaluation` VALUES (6, 11, 27, 28, 5.0, '好评', '[\"态度友好\"]', 0, '2025-03-08 21:07:35');

-- ----------------------------
-- Table structure for sys_task_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_task_log`;
CREATE TABLE `sys_task_log`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `task_id` int(0) NOT NULL COMMENT '任务ID',
  `user_id` int(0) NULL DEFAULT NULL COMMENT '操作人id',
  `action` tinyint(0) NOT NULL COMMENT '操作类型',
  `images` json NULL COMMENT '图片json',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注说明',
  `action_time` datetime(0) NOT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 53 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '任务日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_task_log
-- ----------------------------
INSERT INTO `sys_task_log` VALUES (21, 15, 28, 2, NULL, '接受任务:借篮球场地并买水', '2025-02-27 08:47:10');
INSERT INTO `sys_task_log` VALUES (22, 15, 28, 2, NULL, '接受任务:借篮球场地并买水', '2025-02-27 09:08:46');
INSERT INTO `sys_task_log` VALUES (23, 15, 28, 4, NULL, '开始执行任务:借篮球场地并买水', '2025-02-27 09:20:10');
INSERT INTO `sys_task_log` VALUES (24, 14, 28, 2, '[]', '接受任务:食堂窗口排队', '2025-02-27 10:47:02');
INSERT INTO `sys_task_log` VALUES (25, 14, 28, 4, '[]', '开始执行任务:食堂窗口排队', '2025-02-27 19:42:37');
INSERT INTO `sys_task_log` VALUES (26, 15, 28, 5, '[]', '送达任务:借篮球场地并买水', '2025-02-27 19:54:29');
INSERT INTO `sys_task_log` VALUES (27, 12, 27, 3, '[]', '取消任务:中午奶茶配送', '2025-02-27 20:10:36');
INSERT INTO `sys_task_log` VALUES (28, 15, 27, 6, '[]', '确认完成任务:借篮球场地并买水', '2025-02-27 20:17:06');
INSERT INTO `sys_task_log` VALUES (29, 14, 28, 5, '[]', '送达任务:食堂窗口排队', '2025-02-28 12:30:51');
INSERT INTO `sys_task_log` VALUES (30, 14, 27, 7, '[\"http://127.0.0.1:9001/static/images/357a04ec-015a-42f4-9369-04a86e0b3e4e.png\", \"http://127.0.0.1:9001/static/images/0a2d1a0d-f37a-44f8-9cd3-bfd475b2c147.png\"]', '提交纠纷：送错了', '2025-02-28 15:00:15');
INSERT INTO `sys_task_log` VALUES (31, 17, 28, 1, '[]', '发布任务:排队喜茶', '2025-03-01 12:22:27');
INSERT INTO `sys_task_log` VALUES (32, 18, 28, 1, '[]', '发布任务:我要学习', '2025-03-01 12:28:33');
INSERT INTO `sys_task_log` VALUES (33, 17, 28, 3, '[]', '取消任务:排队喜茶', '2025-03-01 12:28:42');
INSERT INTO `sys_task_log` VALUES (34, 18, 27, 2, '[]', '接受任务:我要学习', '2025-03-01 12:36:35');
INSERT INTO `sys_task_log` VALUES (35, 18, 27, 4, '[]', '开始执行任务:我要学习', '2025-03-01 12:36:40');
INSERT INTO `sys_task_log` VALUES (36, 18, 27, 5, '[]', '送达任务:我要学习', '2025-03-01 12:36:46');
INSERT INTO `sys_task_log` VALUES (37, 18, 28, 6, '[]', '确认完成任务:我要学习', '2025-03-01 12:37:11');
INSERT INTO `sys_task_log` VALUES (38, 18, 28, 6, '[]', '确认完成任务:我要学习', '2025-03-01 12:38:06');
INSERT INTO `sys_task_log` VALUES (39, 14, 0, 8, '[]', '纠纷处理结果：存在疑问，处理说明：，展示时长：72小时', '2025-03-01 16:33:14');
INSERT INTO `sys_task_log` VALUES (40, 14, 0, 8, '[]', '纠纷处理结果：发布者胜诉，处理说明：发布者是合理的', '2025-03-01 19:26:36');
INSERT INTO `sys_task_log` VALUES (41, 11, 28, 2, '[]', '接受任务:购买创新实验材料', '2025-03-02 21:56:22');
INSERT INTO `sys_task_log` VALUES (42, 11, 28, 4, '[]', '开始执行任务:购买创新实验材料', '2025-03-02 21:56:36');
INSERT INTO `sys_task_log` VALUES (43, 11, 28, 5, '[]', '送达任务:购买创新实验材料', '2025-03-02 21:56:39');
INSERT INTO `sys_task_log` VALUES (44, 11, 27, 7, '[\"http://127.0.0.1:9001/static/images/5a5a9a88-be0a-460b-905b-6f38fc03324a.png\"]', '提交纠纷：不合适 不行', '2025-03-02 21:58:56');
INSERT INTO `sys_task_log` VALUES (45, 11, 0, 8, '[]', '纠纷处理结果：存在疑问，处理说明：，展示时长：72小时', '2025-03-02 22:01:13');
INSERT INTO `sys_task_log` VALUES (46, 11, 0, 8, '[]', '纠纷处理结果：跑腿者胜诉，处理说明：人家送的没错，就是这样', '2025-03-02 22:11:38');
INSERT INTO `sys_task_log` VALUES (47, 19, 27, 1, '[]', '发布任务:黄焖鸡', '2025-03-08 21:00:34');
INSERT INTO `sys_task_log` VALUES (48, 19, 28, 2, '[]', '接受任务:黄焖鸡', '2025-03-08 21:02:28');
INSERT INTO `sys_task_log` VALUES (49, 19, 28, 4, '[]', '开始执行任务:黄焖鸡', '2025-03-08 21:02:40');
INSERT INTO `sys_task_log` VALUES (50, 19, 28, 5, '[]', '送达任务:黄焖鸡', '2025-03-08 21:06:25');
INSERT INTO `sys_task_log` VALUES (51, 19, 27, 7, '[\"http://127.0.0.1:9001/static/images/8433a713-d883-4fa6-b385-653b7167cc61.png\"]', '提交纠纷：价格不对', '2025-03-08 21:08:01');
INSERT INTO `sys_task_log` VALUES (52, 19, 0, 8, '[]', '纠纷处理结果：存在疑问，处理说明：，展示时长：72小时', '2025-03-08 21:09:12');
INSERT INTO `sys_task_log` VALUES (53, 19, 0, 8, '[]', '纠纷处理结果：发布者胜诉，处理说明：123213', '2025-03-08 21:13:57');

-- ----------------------------
-- Table structure for sys_task_message
-- ----------------------------
DROP TABLE IF EXISTS `sys_task_message`;
CREATE TABLE `sys_task_message`  (
  `id` int(0) NOT NULL AUTO_INCREMENT COMMENT '留言ID',
  `task_id` int(0) NOT NULL COMMENT '任务ID',
  `user_id` int(0) NOT NULL COMMENT '留言用户ID',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '留言内容',
  `images` json NULL COMMENT '图片列表（JSON格式）',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 28 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '任务留言表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_task_message
-- ----------------------------
INSERT INTO `sys_task_message` VALUES (1, 14, 28, 'asdasd', '[]', '2025-02-27 18:11:13');
INSERT INTO `sys_task_message` VALUES (2, 14, 28, '123123', '[\"http://127.0.0.1:9001/static/images/357a04ec-015a-42f4-9369-04a86e0b3e4e.png\", \"http://127.0.0.1:9001/static/images/0a2d1a0d-f37a-44f8-9cd3-bfd475b2c147.png\"]', '2025-02-27 18:19:01');
INSERT INTO `sys_task_message` VALUES (3, 14, 28, '123123', '[]', '2025-02-27 18:19:04');
INSERT INTO `sys_task_message` VALUES (4, 14, 28, '123123', '[]', '2025-02-27 18:19:07');
INSERT INTO `sys_task_message` VALUES (5, 14, 28, '123123', '[]', '2025-02-27 19:19:40');
INSERT INTO `sys_task_message` VALUES (6, 14, 28, '你好', '[null]', '2025-02-27 19:19:50');
INSERT INTO `sys_task_message` VALUES (7, 14, 28, '123', '[]', '2025-02-27 19:24:00');
INSERT INTO `sys_task_message` VALUES (8, 14, 28, '123', '[]', '2025-02-27 19:24:03');
INSERT INTO `sys_task_message` VALUES (9, 14, 28, '3333', '[]', '2025-02-27 19:24:05');
INSERT INTO `sys_task_message` VALUES (10, 14, 28, '1323123', '[]', '2025-02-27 19:26:29');
INSERT INTO `sys_task_message` VALUES (11, 15, 28, '我已经购买完毕', '[null]', '2025-02-27 19:27:04');
INSERT INTO `sys_task_message` VALUES (12, 15, 28, '好的 我已经购买完毕', '[]', '2025-02-27 19:29:54');
INSERT INTO `sys_task_message` VALUES (13, 15, 28, '请你查看', '[\"http://127.0.0.1:9001/static/images/357a04ec-015a-42f4-9369-04a86e0b3e4e.png\", \"http://127.0.0.1:9001/static/images/0a2d1a0d-f37a-44f8-9cd3-bfd475b2c147.png\"]', '2025-02-27 19:30:01');
INSERT INTO `sys_task_message` VALUES (14, 14, 28, '123123', '[\"http://127.0.0.1:9001/static/images/357a04ec-015a-42f4-9369-04a86e0b3e4e.png\", \"http://127.0.0.1:9001/static/images/0a2d1a0d-f37a-44f8-9cd3-bfd475b2c147.png\"]', '2025-02-27 19:32:20');
INSERT INTO `sys_task_message` VALUES (15, 15, 28, '你看', '[\"http://127.0.0.1:9001/static/images/357a04ec-015a-42f4-9369-04a86e0b3e4e.png\", \"http://127.0.0.1:9001/static/images/0a2d1a0d-f37a-44f8-9cd3-bfd475b2c147.png\"]', '2025-02-27 19:34:44');
INSERT INTO `sys_task_message` VALUES (16, 15, 28, '123123', '[\"http://127.0.0.1:9001/static/images/357a04ec-015a-42f4-9369-04a86e0b3e4e.png\", \"http://127.0.0.1:9001/static/images/0a2d1a0d-f37a-44f8-9cd3-bfd475b2c147.png\"]', '2025-02-27 19:35:47');
INSERT INTO `sys_task_message` VALUES (17, 15, 28, '123', '[]', '2025-02-27 19:36:21');
INSERT INTO `sys_task_message` VALUES (18, 15, 27, '好的 看到了', '[]', '2025-02-27 21:34:02');
INSERT INTO `sys_task_message` VALUES (19, 14, 27, '是这个', '[\"http://127.0.0.1:9001/static/images/357a04ec-015a-42f4-9369-04a86e0b3e4e.png\", \"http://127.0.0.1:9001/static/images/0a2d1a0d-f37a-44f8-9cd3-bfd475b2c147.png\"]', '2025-02-27 22:30:40');
INSERT INTO `sys_task_message` VALUES (20, 15, 27, '123123', '[]', '2025-02-27 22:56:31');
INSERT INTO `sys_task_message` VALUES (21, 15, 27, '12313', '[\"http://127.0.0.1:9001/static/images/f7284408-d2a8-4a1c-a0d4-704c876df5ce.png\"]', '2025-02-27 22:56:35');
INSERT INTO `sys_task_message` VALUES (22, 18, 28, '和那帮', '[]', '2025-03-01 12:38:00');
INSERT INTO `sys_task_message` VALUES (23, 18, 28, '好的', '[]', '2025-03-02 18:20:28');
INSERT INTO `sys_task_message` VALUES (24, 11, 28, '你好你好', '[]', '2025-03-02 21:56:57');
INSERT INTO `sys_task_message` VALUES (25, 11, 28, '物品已经送达，请你准时进行收获', '[]', '2025-03-02 21:57:10');
INSERT INTO `sys_task_message` VALUES (26, 19, 27, '重辣', '[]', '2025-03-08 21:01:28');
INSERT INTO `sys_task_message` VALUES (27, 19, 28, 'www', '[]', '2025-03-08 21:02:50');

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
) ENGINE = InnoDB AUTO_INCREMENT = 44 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '任务类型表' ROW_FORMAT = Dynamic;

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
INSERT INTO `sys_task_types` VALUES (13, '奶茶外卖', 'https://cdn-icons-png.flaticon.com/128/2906/2906120.png', '校内外奶茶店饮品配送服务', 94, 1, '2025-02-25 11:40:16');
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
