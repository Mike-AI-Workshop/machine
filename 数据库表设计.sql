-- 1. 机房表（room）
CREATE TABLE `room` (
  `id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
  `name` VARCHAR(100) NOT NULL COMMENT '机房名称',
  `location` VARCHAR(255) COMMENT '位置',
  `image_id` INT COMMENT '图片资源ID，关联image_resource表',
  `description` TEXT COMMENT '介绍'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='机房表';

-- 2. 机柜表（cabinet）
CREATE TABLE `cabinet` (
  `id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
  `room_id` INT NOT NULL COMMENT '机房ID，关联room表 (冗余字段，便于查询)',
  `row_id` INT NOT NULL COMMENT '所属机柜排ID, 关联cabinet_row表',
  `name` VARCHAR(100) NOT NULL COMMENT '机柜名称',
  `number` VARCHAR(100) NOT NULL COMMENT '机柜编号（机房下唯一）',
  `column_number` INT COMMENT '机柜在本排中的位置/列数',
  `image_front` INT COMMENT '正面图ID，关联image_resource表',
  `image_back` INT COMMENT '背面图ID，关联image_resource表',
  `description` TEXT COMMENT '介绍',
  UNIQUE KEY `uniq_room_cabinet_number` (`room_id`, `number`),
  CONSTRAINT `fk_cabinet_room` FOREIGN KEY (`room_id`) REFERENCES `room`(`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_cabinet_row` FOREIGN KEY (`row_id`) REFERENCES `cabinet_row`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='机柜表';

-- 3. 设备表（device）
CREATE TABLE `device` (
  `id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
  `cabinet_id` INT NOT NULL COMMENT '机柜ID，关联cabinet表',
  `name` VARCHAR(100) NOT NULL COMMENT '设备名称',
  `number` VARCHAR(100) NOT NULL COMMENT '设备编号（机柜下唯一）',
  `image_front` INT COMMENT '正面图ID，关联image_resource表',
  `image_back` INT COMMENT '背面图ID，关联image_resource表',
  `description` TEXT COMMENT '介绍',
  UNIQUE KEY `uniq_cabinet_device_number` (`cabinet_id`, `number`),
  CONSTRAINT `fk_device_cabinet` FOREIGN KEY (`cabinet_id`) REFERENCES `cabinet`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设备表';

-- 4. 接口表（interface）
CREATE TABLE `interface` (
  `id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
  `device_id` INT NOT NULL COMMENT '设备ID，关联device表',
  `name` VARCHAR(100) NOT NULL COMMENT '接口名称',
  `number` VARCHAR(100) NOT NULL COMMENT '接口编号（设备下唯一）',
  `description` TEXT COMMENT '介绍',
  `target_id` INT DEFAULT NULL COMMENT '目标接口ID，用于表示接口之间的连接关系 (可为空)',
  UNIQUE KEY `uniq_device_interface_number` (`device_id`, `number`),
  CONSTRAINT `fk_interface_device` FOREIGN KEY (`device_id`) REFERENCES `device`(`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_interface_target` FOREIGN KEY (`target_id`) REFERENCES `interface`(`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='接口表';

-- 5. 图片资源表（image_resource）
CREATE TABLE `image_resource` (
  `id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
  `url` VARCHAR(255) NOT NULL COMMENT '图片URL',
  `type` VARCHAR(50) COMMENT '图片类型, 例如: room, cabinet_front, device_back 等',
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='图片资源表';

-- 6. 图标标注表（marker）
CREATE TABLE `marker` (
  `id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
  `parent_type` ENUM('cabinet','device') NOT NULL COMMENT '所属类型（cabinet/device）',
  `parent_id` INT NOT NULL COMMENT '所属ID',
  `image_type` ENUM('front','back') NOT NULL COMMENT '正面/背面',
  `x` FLOAT NOT NULL COMMENT 'X坐标（百分比）',
  `y` FLOAT NOT NULL COMMENT 'Y坐标（百分比）',
  `ref_type` ENUM('device','interface') NOT NULL COMMENT '关联类型（device/interface）',
  `ref_id` INT NOT NULL COMMENT '关联设备/接口ID',
  `info` TEXT COMMENT '备注',
  `name` VARCHAR(100) DEFAULT '' NOT NULL COMMENT '名称',
  `icon` VARCHAR(20) DEFAULT '' NOT NULL COMMENT '图标',
  INDEX `idx_marker_parent` (`parent_type`, `parent_id`),
  INDEX `idx_marker_ref` (`ref_type`, `ref_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='图标标注表';

-- 7. 机柜排表 (cabinet_row)
CREATE TABLE `cabinet_row` (
  `id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
  `room_id` INT NOT NULL COMMENT '所属机房ID, 关联room表',
  `name` VARCHAR(100) NOT NULL COMMENT '排的名称或编号 (例如: A排, 第1排)',
  `description` TEXT COMMENT '介绍',
  UNIQUE KEY `uniq_room_row_name` (`room_id`, `name`),
  CONSTRAINT `fk_row_room` FOREIGN KEY (`room_id`) REFERENCES `room`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='机柜排表';

-- 外键约束说明：
-- marker表的parent_id和ref_id未加外键约束，因其多态（多表），如需严格约束可用触发器实现。

-- 说明：
-- 1. 所有唯一性约束均已加在父级+编号上，防止重复。
-- 2. 所有外键均设置ON DELETE CASCADE或SET NULL，保证数据完整性。
-- 3. 所有表和字段均有详细中文注释，便于维护。

-- ----------------------------
-- 以下为针对MySQL 5.7的修改命令，可直接在控制台执行
-- ----------------------------

-- 步骤1: 修改cabinet表的number字段类型
ALTER TABLE `cabinet` MODIFY COLUMN `number` VARCHAR(100) NOT NULL COMMENT '机柜编号（机房下唯一）';

-- 步骤2: 修改device表的number字段类型
ALTER TABLE `device` MODIFY COLUMN `number` VARCHAR(100) NOT NULL COMMENT '设备编号（机柜下唯一）';

-- 步骤3: 修改interface表的number字段类型和target_id的注释
ALTER TABLE `interface` MODIFY COLUMN `number` VARCHAR(100) NOT NULL COMMENT '接口编号（设备下唯一）',
                        MODIFY COLUMN `target_id` INT DEFAULT NULL COMMENT '目标接口ID，用于表示接口之间的连接关系 (可为空)';

-- 步骤4: 修改image_resource表的type字段类型
ALTER TABLE `image_resource` MODIFY COLUMN `type` VARCHAR(50) COMMENT '图片类型, 例如: room, cabinet_front, device_back 等';