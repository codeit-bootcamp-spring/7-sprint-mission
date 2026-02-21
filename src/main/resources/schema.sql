----------------- binary_contents -------------------
CREATE TABLE IF NOT EXISTS binary_contents
(
    id           UUID PRIMARY KEY,
    created_at   TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at   TIMESTAMP WITH TIME ZONE,
    file_name    VARCHAR(255)             NOT NULL,
    size         BIGINT                   NOT NULL,
    content_type VARCHAR(100)             NOT NULL,
    status       VARCHAR(20)              NOT NULL
);

-- ALTER TABLE binary_contents
--      ADD COLUMN updated_at timestamp with time zone;
-- ALTER TABLE binary_contents
--      ADD COLUMN status varchar(20) NOT NULL;

------------------ users ----------------------
CREATE TABLE IF NOT EXISTS users
(
    id         UUID PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE,
    username   VARCHAR(50) UNIQUE       NOT NULL,
    email      VARCHAR(100) UNIQUE      NOT NULL,
    password   VARCHAR(60)              NOT NULL,
    profile_id UUID                     REFERENCES binary_contents (id) ON DELETE SET NULL,
    role       varchar(20)              NOT NULL
);

-- ALTER TABLE users ADD role varchar(20) NOT NULL Default 'USER';

-------------------- channels -------------------
CREATE TABLE IF NOT EXISTS channels
(
    id          UUID PRIMARY KEY,
    created_at  TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at  TIMESTAMP WITH TIME ZONE,
    name        VARCHAR(100),
    description VARCHAR(500),
    type        VARCHAR(10)              NOT NULL CHECK (type IN ('PUBLIC', 'PRIVATE'))
);

-------------------- messages ---------------------
CREATE TABLE IF NOT EXISTS messages
(
    id         UUID PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE,
    content    TEXT,
    channel_id UUID                     NOT NULL REFERENCES channels (id) ON DELETE CASCADE,
    author_id  UUID                     REFERENCES users (id) ON DELETE SET NULL
);

-------------------- read_statuses ---------------------
CREATE TABLE IF NOT EXISTS read_statuses
(
    id           UUID PRIMARY KEY,
    created_at   TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at   TIMESTAMP WITH TIME ZONE,
    user_id      UUID                     NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    channel_id   UUID                     NOT NULL REFERENCES channels (id) ON DELETE CASCADE,
    last_read_at TIMESTAMP WITH TIME ZONE NOT NULL,
    notification_enabled boolean NOT NULL,
    UNIQUE (user_id, channel_id)
);

-- ALTER TABLE read_statuses
--      ADD COLUMN notification_enabled boolean NOT NULL;
-------------------- message_attachments ------------------
CREATE TABLE IF NOT EXISTS message_attachments
(
    message_id    UUID NOT NULL REFERENCES messages (id) ON DELETE CASCADE,
    attachment_id UUID NOT NULL REFERENCES binary_contents (id) ON DELETE CASCADE,
    PRIMARY KEY (message_id, attachment_id)
);

--------------------persistent_logins------------------
create table persistent_logins
(
    username  varchar(64) not null,
    series    varchar(64) primary key,
    token     varchar(64) not null,
    last_used timestamp   not null
);