CREATE TABLE users
(
    id         UUID
        CONSTRAINT users_id_pk PRIMARY KEY,
    created_at TIMESTAMP
        CONSTRAINT users_createdAt_nn NOT NULL,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP,
    name       VARCHAR(50)
        CONSTRAINT users_name_nn NOT NULL
        CONSTRAINT users_name_uk UNIQUE,
    email      VARCHAR(100)
        CONSTRAINT users_email_nn NOT NULL
        CONSTRAINT users_email_uk UNIQUE,
    password   VARCHAR(60)
        CONSTRAINT users_password_nn_uniq NOT NULL,
    profile_id UUID,
    CONSTRAINT users_profile_id_fk
        FOREIGN KEY (profile_id)
            REFERENCES binary_content (id)
            ON DELETE SET NULL
);
--이넘용 1가지 방법
--CREATE TYPE channel_type AS ENUM ('PUBLIC', 'PRIVATE');

CREATE TABLE channels
(
    id          UUID
        CONSTRAINT channels_id_pk PRIMARY KEY,
    created_at  TIMESTAMP
        CONSTRAINT channels_createdAt_nn NOT NULL,
    updated_at  TIMESTAMP,
    name        VARCHAR(100),
    description VARCHAR(500),
    type        VARCHAR(10),
    CONSTRAINT channels_type_ck
        CHECK (type IN ('PUBLIC', 'PRIVATE'))
);

CREATE TABLE messages
(
    id         UUID
        CONSTRAINT messages_id_pk PRIMARY KEY,
    created_at TIMESTAMP
        CONSTRAINT messages_createdAt_nn NOT NULL,
    updated_at TIMESTAMP,
    content    TEXT,
    channel_id UUID
        CONSTRAINT messages_channel_id_nn NOT NULL,
    author_id  UUID,
    CONSTRAINT messages_channel_id_fk
        FOREIGN KEY (channel_id)
            REFERENCES channels (id)
            ON DELETE CASCADE,
    CONSTRAINT messages_author_id_fk
        FOREIGN KEY (author_id)
            REFERENCES users (id)
            ON DELETE SET NULL
);


CREATE TABLE binary_content
(
    id           UUID PRIMARY KEY,
    created_at   TIMESTAMP
        CONSTRAINT binary_content_createdAt_nn NOT NULL,
    file_name    VARCHAR(255)
        CONSTRAINT users_password_nn NOT NULL,
    size         BIGINT
        constraint binary_content_size_nn NOT NULL,
    content_type VARCHAR(100)
        CONSTRAINT binary_content_contentType_nn NOT NULL,
    bytes        BYTEA
        constraint binary_content_bytes_nn NOT NULL
);

CREATE TABLE user_status
(
    id            UUID
        CONSTRAINT users_id_pk PRIMARY KEY,
    created_at    TIMESTAMP
        CONSTRAINT user_status_createdAt_nn NOT NULL,
    updated_at    TIMESTAMP
        CONSTRAINT user_status_updatedAt_nn NOT NULL,
    last_activity TIMESTAMP
        CONSTRAINT user_status_lastActivity_nn NOT NULL,
    user_id       UUID UNIQUE,
    CONSTRAINT user_status_user_id_fk
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE CASCADE

);



CREATE TABLE read_status
(
    id           UUID
        CONSTRAINT read_status_id_pk PRIMARY KEY,
    created_at   TIMESTAMP
        CONSTRAINT read_status_createdAt_nn NOT NULL,
    updated_at   TIMESTAMP,
    last_read_at TIMESTAMP
        CONSTRAINT read_status_last_read_at_nn NOT NULL,
    user_id      UUID
        CONSTRAINT read_status_user_id_nn NOT NULL,
    channel_id   UUID
        CONSTRAINT read_status_channel_id_nn NOT NULL,
    CONSTRAINT read_status_uk UNIQUE (user_id, channel_id),
    CONSTRAINT read_status_user_id_fk
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE CASCADE,
    CONSTRAINT read_status_channel_id_fk
        FOREIGN KEY (channel_id)
            REFERENCES channels (id)
            ON DELETE CASCADE


);


CREATE TABLE message_attachments
(
    message_id    UUID
        CONSTRAINT message_attachments_message_id_nn NOT NULL,
    attachment_id UUID
        CONSTRAINT message_attachments_attachment_id_nn NOT NULL,

    CONSTRAINT message_attachments_message_id_fk
        FOREIGN KEY (message_id)
            REFERENCES messages (id) ON DELETE CASCADE,
    CONSTRAINT message_attachments_attachment_id_fk
        FOREIGN KEY (attachment_id)
            REFERENCES binary_content (id) ON DELETE CASCADE
);
