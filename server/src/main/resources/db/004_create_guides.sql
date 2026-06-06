-- 攻略表
CREATE TABLE guides (
    id            BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    user_id       BIGINT UNSIGNED NOT NULL,
    game_id       INT UNSIGNED    NOT NULL DEFAULT 0,
    title         VARCHAR(200)    NOT NULL,
    cover_url     VARCHAR(500)    NOT NULL DEFAULT '',
    summary       VARCHAR(500)    NOT NULL DEFAULT '' COMMENT '摘要',
    view_count    INT UNSIGNED    NOT NULL DEFAULT 0,
    like_count    INT UNSIGNED    NOT NULL DEFAULT 0,
    favorite_count INT UNSIGNED   NOT NULL DEFAULT 0,
    section_count INT UNSIGNED    NOT NULL DEFAULT 0,
    status        TINYINT         NOT NULL DEFAULT 1,
    created_at    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_game_status (game_id, status, created_at DESC),
    INDEX idx_user_id (user_id),
    FULLTEXT idx_title_summary (title, summary) WITH PARSER ngram
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 攻略章节表
CREATE TABLE guide_sections (
    id            BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    guide_id      BIGINT UNSIGNED NOT NULL,
    sort_order    INT             NOT NULL DEFAULT 0,
    subtitle      VARCHAR(200)    NOT NULL DEFAULT '',
    content       MEDIUMTEXT      NOT NULL,
    image_url     VARCHAR(500)    NOT NULL DEFAULT '',
    INDEX idx_guide_id (guide_id, sort_order)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 攻略收藏表
CREATE TABLE guide_favorites (
    id            BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    user_id       BIGINT UNSIGNED NOT NULL,
    guide_id      BIGINT UNSIGNED NOT NULL,
    created_at    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_guide (user_id, guide_id),
    INDEX idx_user_created (user_id, created_at DESC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
