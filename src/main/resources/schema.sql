CREATE TABLE binary_contents
(
    id           UUID PRIMARY KEY,
    created_at timestamp with time zone NOT NULL,
    file_name    VARCHAR(255) NOT NULL,
    size         BIGINT       NOT NULL,
    content_type VARCHAR(100) NOT NULL,
--     bytes        BYTEA        NOT NULL
)

CREATE TABLE users
(
    id         uuid PRIMARY KEY,
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone,
    username   varchar(50)  NOT NULL UNIQUE,
    email      varchar(100) NOT NULL UNIQUE,
    password   varchar(60)  NOT NULL,
    profile_id uuid         REFERENCES binary_contents (id) ON DELETE SET NULL
)

CREATE TABLE user_statuses
(
    id             uuid PRIMARY KEY,
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone,
    user_id        uuid REFERENCES users (id) ON DELETE CASCADE NOT NULL,
    last_active_at timestamptz                                  NOT NULL
)

CREATE TABLE read_statuses
(
    id           uuid PRIMARY KEY,
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone,
    user_id      uuid REFERENCES users (id) ON DELETE CASCADE    NOT NULL,
    channel_id   uuid REFERENCES channels (id) ON DELETE CASCADE NOT NULL,
    last_read_at timestamptz                                     NOT NULL,

    CONSTRAINT uk_user_channel UNIQUE (user_id, channel_id)
)

CREATE TABLE channels
(
    id          uuid PRIMARY KEY,
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone,
    name        varchar(100),
    description varchar(500),
    TYPE        varchar(10) NOT NULL CHECK (type IN ('PUBLIC', 'PRIVATE'))
)

CREATE TABLE messages
(
    id         uuid PRIMARY KEY,
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone,
    CONTENT    text,
    channel_id uuid REFERENCES channels (id) ON DELETE CASCADE NOT NULL,
    author_id  uuid                                            REFERENCES users (id) ON DELETE SET NULL
)

CREATE TABLE message_attachments
(
    message_id    uuid REFERENCES messages (id) ON DELETE CASCADE NOT NULL,
    attachment_id uuid REFERENCES binary_contents (id) ON DELETE CASCADE NOT NULL,
    PRIMARY KEY (message_id, attachment_id)
)







