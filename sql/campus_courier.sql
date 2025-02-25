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

 Date: 23/02/2025 14:36:12
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
) ENGINE = InnoDB AUTO_INCREMENT = 55 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '学生记录表' ROW_FORMAT = Dynamic;

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

SET FOREIGN_KEY_CHECKS = 1;
