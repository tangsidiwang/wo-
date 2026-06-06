-- 帖子表
CREATE TABLE posts (
    id            BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    user_id       BIGINT UNSIGNED NOT NULL COMMENT '作者ID',
    category_id   INT UNSIGNED    NOT NULL DEFAULT 0 COMMENT '游戏分类ID',
    title         VARCHAR(200)    NOT NULL DEFAULT '' COMMENT '标题',
    content       MEDIUMTEXT      NOT NULL COMMENT '正文(HTML)',
    content_text  MEDIUMTEXT      NOT NULL COMMENT '正文(纯文本)',
    cover_url     VARCHAR(500)    NOT NULL DEFAULT '' COMMENT '封面图',
    view_count    INT UNSIGNED    NOT NULL DEFAULT 0,
    like_count    INT UNSIGNED    NOT NULL DEFAULT 0,
    comment_count INT UNSIGNED    NOT NULL DEFAULT 0,
    favorite_count INT UNSIGNED   NOT NULL DEFAULT 0,
    is_pinned     TINYINT         NOT NULL DEFAULT 0,
    is_essence    TINYINT         NOT NULL DEFAULT 0,
    status        TINYINT         NOT NULL DEFAULT 1 COMMENT '1正常 2审核中 3已删除 4违规',
    created_at    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted_at    DATETIME        NULL,
    INDEX idx_user_id (user_id),
    INDEX idx_category (category_id, status, created_at DESC),
    INDEX idx_status_created (status, created_at DESC),
    INDEX idx_like_count (like_count DESC),
    FULLTEXT idx_content_text (content_text) WITH PARSER ngram
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 帖子图片关联表
CREATE TABLE post_images (
    id            BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    post_id       BIGINT UNSIGNED NOT NULL,
    url           VARCHAR(500)    NOT NULL DEFAULT '',
    thumb_url     VARCHAR(500)    NOT NULL DEFAULT '',
    width         INT             NOT NULL DEFAULT 0,
    height        INT             NOT NULL DEFAULT 0,
    sort_order    INT             NOT NULL DEFAULT 0,
    INDEX idx_post_id (post_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 标签表
CREATE TABLE tags (
    id            INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    name          VARCHAR(30)   NOT NULL,
    post_count    INT UNSIGNED  NOT NULL DEFAULT 0,
    status        TINYINT       NOT NULL DEFAULT 1,
    UNIQUE KEY uk_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 帖子-标签关联表
CREATE TABLE post_tags (
    id            BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    post_id       BIGINT UNSIGNED NOT NULL,
    tag_id        INT UNSIGNED    NOT NULL,
    UNIQUE KEY uk_post_tag (post_id, tag_id),
    INDEX idx_tag_id (tag_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 点赞表
CREATE TABLE post_likes (
    id            BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    user_id       BIGINT UNSIGNED NOT NULL,
    post_id       BIGINT UNSIGNED NOT NULL,
    created_at    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_post (user_id, post_id),
    INDEX idx_post_id (post_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 收藏表
CREATE TABLE post_favorites (
    id            BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    user_id       BIGINT UNSIGNED NOT NULL,
    post_id       BIGINT UNSIGNED NOT NULL,
    created_at    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_post (user_id, post_id),
    INDEX idx_user_created (user_id, created_at DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
