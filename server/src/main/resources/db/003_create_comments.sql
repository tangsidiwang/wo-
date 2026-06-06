-- 评论表
CREATE TABLE comments (
    id            BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    post_id       BIGINT UNSIGNED NOT NULL COMMENT '所属帖子ID',
    user_id       BIGINT UNSIGNED NOT NULL COMMENT '评论者ID',
    parent_id     BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '父评论ID 0=一级评论',
    reply_to_uid  BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '回复目标用户ID',
    content       TEXT            NOT NULL,
    like_count    INT UNSIGNED    NOT NULL DEFAULT 0,
    reply_count   INT UNSIGNED    NOT NULL DEFAULT 0,
    status        TINYINT         NOT NULL DEFAULT 1 COMMENT '1正常 2已删除',
    created_at    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_post_parent (post_id, parent_id, created_at ASC),
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 评论点赞表
CREATE TABLE comment_likes (
    id            BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    user_id       BIGINT UNSIGNED NOT NULL,
    comment_id    BIGINT UNSIGNED NOT NULL,
    created_at    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_comment (user_id, comment_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
