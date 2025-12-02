-- ===============================
-- 1. UUID 확장 설치 (PostgreSQL)
-- ===============================
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- ===============================
-- 2. base_entity 역할: 모든 테이블 공통 컬럼 포함
-- created_at, updated_at은 애플리케이션에서 관리
-- ===============================

-- ===============================
-- 3. users 테이블
-- ===============================
CREATE TABLE IF NOT EXISTS users (
                                     id UUID PRIMARY KEY,
                                     username VARCHAR(255) NOT NULL,
    profile_id UUID NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
    );

-- ===============================
-- 4. channels 테이블
-- ===============================
CREATE TABLE IF NOT EXISTS channels (
                                        id UUID PRIMARY KEY,
                                        name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
    );

-- ===============================
-- 5. messages 테이블
-- ===============================
CREATE TABLE IF NOT EXISTS messages (
                                        id UUID PRIMARY KEY,
                                        content VARCHAR(1000) NOT NULL,
    author_id UUID NOT NULL,
    channel_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_messages_user
    FOREIGN KEY (author_id)
    REFERENCES users(id)
    ON DELETE CASCADE,

    CONSTRAINT fk_messages_channel
    FOREIGN KEY (channel_id)
    REFERENCES channels(id)
    ON DELETE CASCADE
    );

-- ===============================
-- 6. binary_contents 테이블
-- ===============================
CREATE TABLE IF NOT EXISTS binary_contents (
                                               id UUID PRIMARY KEY,
                                               file_name VARCHAR(255) NOT NULL,
    size BIGINT NOT NULL,
    content_type VARCHAR(255) NOT NULL,

    message_id UUID NULL,
    user_id UUID NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_binary_message
    FOREIGN KEY (message_id)
    REFERENCES messages(id)
    ON DELETE SET NULL,

    CONSTRAINT fk_binary_user
    FOREIGN KEY (user_id)
    REFERENCES users(id)
    ON DELETE SET NULL
    );

-- ===============================
-- 7. read_status 테이블
-- ===============================
CREATE TABLE IF NOT EXISTS read_status (
                                           id UUID PRIMARY KEY,
                                           user_id UUID NOT NULL,
                                           channel_id UUID NOT NULL,
                                           last_read_at TIMESTAMP NOT NULL,
                                           created_at TIMESTAMP NOT NULL,
                                           updated_at TIMESTAMP NOT NULL,

                                           CONSTRAINT fk_readstatus_user
                                           FOREIGN KEY (user_id)
    REFERENCES users(id)
    ON DELETE CASCADE,

    CONSTRAINT fk_readstatus_channel
    FOREIGN KEY (channel_id)
    REFERENCES channels(id)
    ON DELETE CASCADE
    );

-- ===============================
-- 8. user_status 테이블
-- ===============================
CREATE TABLE IF NOT EXISTS user_status (
                                           id UUID PRIMARY KEY,
                                           user_id UUID NOT NULL UNIQUE,
                                           last_active_at TIMESTAMP NOT NULL,
                                           created_at TIMESTAMP NOT NULL,
                                           updated_at TIMESTAMP NOT NULL,

                                           CONSTRAINT fk_userstatus_user
                                           FOREIGN KEY (user_id)
    REFERENCES users(id)
    ON DELETE CASCADE
    );

