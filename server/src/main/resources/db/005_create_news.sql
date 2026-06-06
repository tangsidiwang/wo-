-- 资讯表
CREATE TABLE news (
    id            BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    title         VARCHAR(200)    NOT NULL,
    cover_url     VARCHAR(500)    NOT NULL DEFAULT '',
    summary       VARCHAR(500)    NOT NULL DEFAULT '',
    content       MEDIUMTEXT      NOT NULL,
    category      VARCHAR(30)     NOT NULL DEFAULT '' COMMENT 'game_update|esports|industry',
    source        VARCHAR(100)    NOT NULL DEFAULT '',
    source_url    VARCHAR(500)    NOT NULL DEFAULT '',
    view_count    INT UNSIGNED    NOT NULL DEFAULT 0,
    is_top        TINYINT         NOT NULL DEFAULT 0,
    status        TINYINT         NOT NULL DEFAULT 1,
    published_at  DATETIME        NOT NULL,
    created_at    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_category_published (category, published_at DESC),
    INDEX idx_status_published (status, published_at DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
