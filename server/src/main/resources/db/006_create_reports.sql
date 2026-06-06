-- 举报表
CREATE TABLE reports (
    id            BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    reporter_id   BIGINT UNSIGNED NOT NULL COMMENT '举报人ID',
    target_type   VARCHAR(20)     NOT NULL COMMENT 'post|comment|guide',
    target_id     BIGINT UNSIGNED NOT NULL,
    reason_type   VARCHAR(30)     NOT NULL,
    description   VARCHAR(500)    NOT NULL DEFAULT '',
    status        TINYINT         NOT NULL DEFAULT 1 COMMENT '1待处理 2已处理 3已驳回',
    handler_id    BIGINT UNSIGNED NOT NULL DEFAULT 0,
    result        VARCHAR(200)    NOT NULL DEFAULT '',
    created_at    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_target (target_type, target_id),
    INDEX idx_status (status, created_at DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 通知表
CREATE TABLE notifications (
    id            BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    user_id       BIGINT UNSIGNED NOT NULL COMMENT '接收者ID',
    sender_id     BIGINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '触发者ID',
    type          VARCHAR(30)     NOT NULL COMMENT 'like|comment|reply|follow|system',
    target_type   VARCHAR(20)     NOT NULL DEFAULT '',
    target_id     BIGINT UNSIGNED NOT NULL DEFAULT 0,
    content       VARCHAR(200)    NOT NULL DEFAULT '',
    is_read       TINYINT         NOT NULL DEFAULT 0,
    created_at    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user_read (user_id, is_read, created_at DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
