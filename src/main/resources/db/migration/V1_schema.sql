CREATE TABLE IF NOT EXISTS file_uploads (
      id BIGINT PRIMARY KEY AUTO_INCREMENT,
      original_filename VARCHAR(255) NOT NULL,  -- 原始文件名（如 test.jpg）
      storage_path VARCHAR(512) NOT NULL,        -- 存储路径（如 /uploads/2023/abc123.jpg）
      file_size BIGINT NOT NULL,                 -- 文件大小（字节）
      file_type VARCHAR(100),                    -- MIME类型（如 image/jpeg）
      upload_time DATETIME DEFAULT CURRENT_TIMESTAMP,
      download_count BIGINT DEFAULT 0,            -- 下载次数（默认为0）
      uploader_id BIGINT,                        -- 关联用户ID（如有）
      sha256_hash CHAR(64),                      -- 文件sha256校验值
      status TINYINT DEFAULT 1                  -- 状态（1-正常, 2-审核中, 3-已删除）
);
