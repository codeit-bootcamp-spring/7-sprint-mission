-- 1. 의존성 없는 테이블 먼저
CREATE TABLE binary_contents
(
    id           UUID PRIMARY KEY,
    created_at   TIMESTAMP WITH TIME ZONE NOT NULL,
    file_name    VARCHAR(255)             NOT NULL,
    size         BIGINT                   NOT NULL,
    content_type VARCHAR(100)             NOT NULL
);

-- 2. binary_contents 참조
CREATE TABLE users
(
    id         UUID PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE,
    username   VARCHAR(50)              NOT NULL UNIQUE,
    email      VARCHAR(100)             NOT NULL UNIQUE,
    password   VARCHAR(60)              NOT NULL,
    profile_id UUID                     REFERENCES binary_contents (id) ON DELETE SET NULL
);

-- 3. users 참조
CREATE TABLE user_statuses
(
    id             UUID PRIMARY KEY,
    created_at     TIMESTAMP WITH TIME ZONE                     NOT NULL,
    updated_at     TIMESTAMP WITH TIME ZONE,
    user_id        UUID REFERENCES users (id) ON DELETE CASCADE NOT NULL,
    last_active_at TIMESTAMP WITH TIME ZONE                     NOT NULL
);

-- 4. 의존성 없음
CREATE TABLE channels
(
    id          UUID PRIMARY KEY,
    created_at  TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at  TIMESTAMP WITH TIME ZONE,
    name        VARCHAR(100),
    description VARCHAR(500),
    type        VARCHAR(10)              NOT NULL CHECK (type IN ('PUBLIC', 'PRIVATE'))
);

-- 5. users + channels 참조
CREATE TABLE read_statuses
(
    id           UUID PRIMARY KEY,
    created_at   TIMESTAMP WITH TIME ZONE                        NOT NULL,
    updated_at   TIMESTAMP WITH TIME ZONE,
    user_id      UUID REFERENCES users (id) ON DELETE CASCADE    NOT NULL,
    channel_id   UUID REFERENCES channels (id) ON DELETE CASCADE NOT NULL,
    last_read_at TIMESTAMP WITH TIME ZONE                        NOT NULL,
    CONSTRAINT uk_user_channel UNIQUE (user_id, channel_id)
);

-- 6. channels + users 참조
CREATE TABLE messages
(
    id         UUID PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE                        NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE,
    content    TEXT,
    channel_id UUID REFERENCES channels (id) ON DELETE CASCADE NOT NULL,
    author_id  UUID                                            REFERENCES users (id) ON DELETE SET NULL
);

-- 7. messages + binary_contents 참조
CREATE TABLE message_attachments
(
    message_id    UUID REFERENCES messages (id) ON DELETE CASCADE        NOT NULL,
    attachment_id UUID REFERENCES binary_contents (id) ON DELETE CASCADE NOT NULL,
    PRIMARY KEY (message_id, attachment_id)
);