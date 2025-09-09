/*
 Navicat Premium Dump SQL

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50744 (5.7.44-log)
 Source Host           : localhost:3306
 Source Schema         : machine-backend

 Target Server Type    : MySQL
 Target Server Version : 50744 (5.7.44-log)
 File Encoding         : 65001

 Date: 07/07/2025 16:02:54
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for cabinet
-- ----------------------------
DROP TABLE IF EXISTS `cabinet`;
CREATE TABLE `cabinet`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `room_id` int(11) NOT NULL COMMENT '机房ID，关联room表',
  `row_id` int(11) NOT NULL COMMENT '所属机柜排ID, 关联cabinet_row表',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '机柜名称',
  `number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `column_number` int(11) NULL DEFAULT NULL COMMENT '机柜所在列数',
  `image_front` int(11) NULL DEFAULT NULL COMMENT '正面图ID，关联image_resource表',
  `image_back` int(11) NULL DEFAULT NULL COMMENT '背面图ID，关联image_resource表',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '介绍',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq_room_cabinet_number`(`room_id`, `number`) USING BTREE,
  INDEX `fk_cabinet_row`(`row_id`) USING BTREE,
  CONSTRAINT `fk_cabinet_room` FOREIGN KEY (`room_id`) REFERENCES `room` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_cabinet_row` FOREIGN KEY (`row_id`) REFERENCES `cabinet_row` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '机柜表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for cabinet_row
-- ----------------------------
DROP TABLE IF EXISTS `cabinet_row`;
CREATE TABLE `cabinet_row`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `room_id` int(11) NOT NULL COMMENT '所属机房ID, 关联room表',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '排的名称或编号 (例如: A排, 第1排)',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '介绍',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq_room_row_name`(`room_id`, `name`) USING BTREE,
  INDEX `fk_row_room`(`room_id`) USING BTREE,
  CONSTRAINT `fk_row_room` FOREIGN KEY (`room_id`) REFERENCES `room` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '机柜排表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for device
-- ----------------------------
DROP TABLE IF EXISTS `device`;
CREATE TABLE `device`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `cabinet_id` int(11) NOT NULL COMMENT '机柜ID，关联cabinet表',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备名称',
  `number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `image_front` int(11) NULL DEFAULT NULL COMMENT '正面图ID，关联image_resource表',
  `image_back` int(11) NULL DEFAULT NULL COMMENT '背面图ID，关联image_resource表',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '介绍',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq_cabinet_device_number`(`cabinet_id`, `number`) USING BTREE,
  CONSTRAINT `fk_device_cabinet` FOREIGN KEY (`cabinet_id`) REFERENCES `cabinet` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '设备表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for image_resource
-- ----------------------------
DROP TABLE IF EXISTS `image_resource`;
CREATE TABLE `image_resource`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '图片URL',
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图片类型, 例如: room, home_carousel 等',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 65 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '图片资源表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for interface
-- ----------------------------
DROP TABLE IF EXISTS `interface`;
CREATE TABLE `interface`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `device_id` int(11) NOT NULL COMMENT '设备ID，关联device表',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '接口名称',
  `number` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '接口编号（设备下唯一）',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '介绍',
  `target_id` int(11) NULL DEFAULT NULL COMMENT '目标接口ID，用于表示接口之间的连接关系 (可为空)',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '接口状态: 1=开启, 0=关闭',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq_device_interface_number`(`device_id`, `number`) USING BTREE,
  INDEX `fk_interface_target`(`target_id`) USING BTREE,
  CONSTRAINT `fk_interface_device` FOREIGN KEY (`device_id`) REFERENCES `device` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `fk_interface_target` FOREIGN KEY (`target_id`) REFERENCES `interface` (`id`) ON DELETE SET NULL ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '接口表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for marker
-- ----------------------------
DROP TABLE IF EXISTS `marker`;
CREATE TABLE `marker`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `parent_type` enum('cabinet','device') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '所属类型（cabinet/device）',
  `parent_id` int(11) NOT NULL COMMENT '所属ID',
  `image_type` enum('front','back') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '正面/背面',
  `x` float NOT NULL COMMENT 'X坐标（百分比）',
  `y` float NOT NULL COMMENT 'Y坐标（百分比）',
  `ref_type` enum('device','interface') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '关联类型（device/interface）',
  `ref_id` int(11) NOT NULL COMMENT '关联设备/接口ID',
  `icon` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '?' COMMENT '图标样式（如emoji或CSS类名）',
  `info` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '备注',
  `width` float NULL DEFAULT 0.1 COMMENT '宽度（占父容器百分比）',
  `height` float NULL DEFAULT 0.1 COMMENT '高度（占父容器百分比）',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_marker_parent`(`parent_type`, `parent_id`) USING BTREE,
  INDEX `idx_marker_ref`(`ref_type`, `ref_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 30 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '图标标注表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for room
-- ----------------------------
DROP TABLE IF EXISTS `room`;
CREATE TABLE `room`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '机房名称',
  `location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '位置',
  `image_id` int(11) NULL DEFAULT NULL COMMENT '图片资源ID，关联image_resource表',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '介绍',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '机房表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for system_content
-- ----------------------------
DROP TABLE IF EXISTS `system_content`;
CREATE TABLE `system_content`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `content_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '内容唯一键',
  `content_value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '内容值',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '内容描述',
  `created_at` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uniq_content_key`(`content_key`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统动态内容配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名，必须唯一',
  `password_hash` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '加密后的密码哈希',
  `role` enum('ADMIN','USER') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'USER' COMMENT '用户角色，默认为普通用户',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '用户创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
