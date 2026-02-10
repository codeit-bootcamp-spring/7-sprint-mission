-- ============================================
-- Discodeit Database Schema
-- ============================================

-- 확장 기능: UUID 생성
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- ============================================
-- 1. users 테이블
-- ============================================
CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    profile_id UUID,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    role varchar(20) not null default 'USER'

    CONSTRAINT fk_users_profile FOREIGN KEY (profile_id)
        REFERENCES binary_contents(id) ON DELETE SET NULL
);

CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);

-- ============================================
-- 2. binary_contents 테이블
-- ============================================
CREATE TABLE IF NOT EXISTS binary_contents (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    file_name VARCHAR(255) NOT NULL,
    size BIGINT NOT NULL,
    content_type VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ============================================
-- 3. channels 테이블
-- ============================================
CREATE TABLE IF NOT EXISTS channels (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(255),
    type VARCHAR(50) NOT NULL CHECK (type IN ('PUBLIC', 'PRIVATE')),
    description TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_channels_type ON channels(type);

-- ============================================
-- 4. channel_participants (ManyToMany 조인 테이블)
-- ============================================
CREATE TABLE IF NOT EXISTS channel_participants (
    channel_id UUID NOT NULL,
    user_id UUID NOT NULL,

    PRIMARY KEY (channel_id, user_id),

    CONSTRAINT fk_channel_participants_channel FOREIGN KEY (channel_id)
        REFERENCES channels(id) ON DELETE CASCADE,
    CONSTRAINT fk_channel_participants_user FOREIGN KEY (user_id)
        REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_channel_participants_channel ON channel_participants(channel_id);
CREATE INDEX idx_channel_participants_user ON channel_participants(user_id);

-- ============================================
-- 5. messages 테이블
-- ============================================
CREATE TABLE IF NOT EXISTS messages (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    author_id UUID NOT NULL,
    channel_id UUID,
    content TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,

    CONSTRAINT fk_messages_author FOREIGN KEY (author_id)
        REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_messages_channel FOREIGN KEY (channel_id)
        REFERENCES channels(id) ON DELETE SET NULL
);

CREATE INDEX idx_messages_author ON messages(author_id);
CREATE INDEX idx_messages_channel ON messages(channel_id);
CREATE INDEX idx_messages_created_at ON messages(created_at DESC);

-- ============================================
-- 6. message_attachments (OneToMany 조인 테이블)
-- ============================================
CREATE TABLE IF NOT EXISTS message_attachments (
    message_id UUID NOT NULL,
    attachment_id UUID NOT NULL,

    PRIMARY KEY (message_id, attachment_id),

    CONSTRAINT fk_message_attachments_message FOREIGN KEY (message_id)
        REFERENCES messages(id) ON DELETE CASCADE,
    CONSTRAINT fk_message_attachments_attachment FOREIGN KEY (attachment_id)
        REFERENCES binary_contents(id) ON DELETE CASCADE
);

CREATE INDEX idx_message_attachments_message ON message_attachments(message_id);

-- ============================================
-- 7. user_statuses 테이블
-- ============================================
CREATE TABLE IF NOT EXISTS user_statuses (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID UNIQUE,
    last_active_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_user_statuses_user FOREIGN KEY (user_id)
        REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_user_statuses_user ON user_statuses(user_id);
CREATE INDEX idx_user_statuses_last_active ON user_statuses(last_active_at DESC);

-- ============================================
-- 8. read_statuses 테이블
-- ============================================
CREATE TABLE IF NOT EXISTS read_statuses (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL,
    channel_id UUID NOT NULL,
    last_read_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_read_statuses_user FOREIGN KEY (user_id)
        REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_read_statuses_channel FOREIGN KEY (channel_id)
        REFERENCES channels(id) ON DELETE CASCADE,

    CONSTRAINT uk_read_statuses_user_channel UNIQUE (user_id, channel_id)
);

CREATE INDEX idx_read_statuses_user ON read_statuses(user_id);
CREATE INDEX idx_read_statuses_channel ON read_statuses(channel_id);

-- ============================================
-- 외래키 추가 (users.profile_id는 binary_contents 생성 후 추가)
-- ============================================
-- users.profile_id 외래키는 이미 위에서 정의됨

-- ============================================
-- 완료 메시지
-- ============================================
-- Schema created successfully
