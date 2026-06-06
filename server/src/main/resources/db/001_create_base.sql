-- =====================================================
-- wo -- 游戏交流社区 数据库初始化脚本
-- =====================================================

CREATE DATABASE IF NOT EXISTS wo_dev
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

USE wo_dev;

-- 用户表
CREATE TABLE users (
    id            BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    openid        VARCHAR(64)   NOT NULL DEFAULT '' COMMENT '微信openid',
    unionid       VARCHAR(64)   NOT NULL DEFAULT '' COMMENT '微信unionid',
    phone         VARCHAR(20)   NOT NULL DEFAULT '' COMMENT '手机号',
    nickname      VARCHAR(50)   NOT NULL DEFAULT '' COMMENT '昵称',
    avatar        VARCHAR(500)  NOT NULL DEFAULT '' COMMENT '头像URL',
    bio           VARCHAR(200)  NOT NULL DEFAULT '' COMMENT '个人简介',
    gender        TINYINT       NOT NULL DEFAULT 0 COMMENT '性别 0未知 1男 2女',
    status        TINYINT       NOT NULL DEFAULT 1 COMMENT '1正常 2禁用',
    role          VARCHAR(20)   NOT NULL DEFAULT 'user' COMMENT '角色',
    post_count    INT UNSIGNED  NOT NULL DEFAULT 0 COMMENT '发帖数',
    like_count    INT UNSIGNED  NOT NULL DEFAULT 0 COMMENT '获赞数',
    fans_count    INT UNSIGNED  NOT NULL DEFAULT 0 COMMENT '粉丝数',
    follow_count  INT UNSIGNED  NOT NULL DEFAULT 0 COMMENT '关注数',
    created_at    DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_openid (openid),
    INDEX idx_phone (phone),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 游戏分类表
CREATE TABLE game_categories (
    id            INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    name          VARCHAR(50)   NOT NULL COMMENT '分类名',
    icon          VARCHAR(500)  NOT NULL DEFAULT '' COMMENT '图标URL',
    sort_order    INT           NOT NULL DEFAULT 0,
    post_count    INT UNSIGNED  NOT NULL DEFAULT 0,
    status        TINYINT       NOT NULL DEFAULT 1,
    created_at    DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 初始化游戏分类数据
INSERT INTO game_categories (name, sort_order) VALUES
('英雄联盟', 1),
('王者荣耀', 2),
('原神', 3),
('崩坏：星穹铁道', 4),
('永劫无间', 5),
('CS2', 6),
('DOTA2', 7),
('绝地求生', 8),
('魔兽世界', 9),
('其他游戏', 99);
