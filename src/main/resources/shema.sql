-- ==========================================
-- 1. binary_contents
-- ==========================================
CREATE TABLE binary_contents (
 id            UUID PRIMARY KEY,
 created_at    TIMESTAMPTZ NOT NULL,
 file_name     VARCHAR(255) NOT NULL,
 size          BIGINT NOT NULL,
 content_type  VARCHAR(100) NOT NULL,
 bytes         BYTEA NOT NULL
);

-- ==========================================
-- 2. users
-- ==========================================
CREATE TABLE users (
   id           UUID PRIMARY KEY,
   created_at   TIMESTAMPTZ NOT NULL,
   updated_at   TIMESTAMPTZ NOT NULL,
   email        VARCHAR(100) UNIQUE NOT NULL,
   username     VARCHAR(50) UNIQUE NOT NULL,
   password     VARCHAR(60) NOT NULL,
   profile_id   UUID UNIQUE,
   CONSTRAINT fk_profile
       FOREIGN KEY (profile_id)
           REFERENCES binary_contents (id)
           ON DELETE SET NULL
);

-- ==========================================
-- 3. user_statuses
-- ==========================================
CREATE TABLE user_statuses (
   id             UUID PRIMARY KEY,
   created_at     TIMESTAMPTZ NOT NULL,
   updated_at     TIMESTAMPTZ NOT NULL,
   last_active_at TIMESTAMPTZ NOT NULL,
   user_id        UUID UNIQUE NOT NULL,
   CONSTRAINT fk_user_status_user
       FOREIGN KEY (user_id)
           REFERENCES users (id)
           ON DELETE CASCADE
);

-- ==========================================
-- 4. channels
-- ==========================================
CREATE TABLE channels (
  id            UUID PRIMARY KEY,
  created_at    TIMESTAMPTZ NOT NULL,
  updated_at    TIMESTAMPTZ NOT NULL,
  name          VARCHAR(100),
  description   VARCHAR(500),
  type          VARCHAR(10) NOT NULL,
  CONSTRAINT chk_channel_type CHECK (type IN ('PUBLIC', 'PRIVATE'))
);

-- ==========================================
-- 5. channel_members(readstates 였던거 member role 추가 및 이름 변경)
-- ==========================================
CREATE TABLE channel_members (
     id            UUID PRIMARY KEY,
     created_at    TIMESTAMPTZ NOT NULL,
     updated_at    TIMESTAMPTZ NOT NULL,
     user_id       UUID NOT NULL,
     channel_id    UUID NOT NULL,
     last_read_at  TIMESTAMPTZ NOT NULL,
     role          VARCHAR(20) NOT NULL,
     CONSTRAINT fk_read_user
         FOREIGN KEY (user_id)
             REFERENCES users (id)
             ON DELETE CASCADE,
     CONSTRAINT fk_read_channel
         FOREIGN KEY (channel_id)
             REFERENCES channels (id)
             ON DELETE CASCADE,
     CONSTRAINT ux_read_user_channel UNIQUE (user_id, channel_id),
     CONSTRAINT chk_channel_member_role CHECK (role IN ('MANAGER', 'MEMBER'))
);

-- ==========================================
-- 6. messages
-- ==========================================
CREATE TABLE messages (
  id           UUID PRIMARY KEY,
  created_at   TIMESTAMPTZ NOT NULL,
  updated_at   TIMESTAMPTZ NOT NULL,
  content      TEXT,
  channel_id   UUID NOT NULL,
  author_id    UUID NOT NULL,
  CONSTRAINT fk_msg_channel
      FOREIGN KEY (channel_id)
          REFERENCES channels (id)
          ON DELETE CASCADE,
  CONSTRAINT fk_msg_author
      FOREIGN KEY (author_id)
          REFERENCES users (id)
          ON DELETE CASCADE
);

-- ==========================================
-- 7. message_attachments
-- ==========================================
CREATE TABLE message_attachments (
     message_id     UUID NOT NULL,
     attachment_id  UUID NOT NULL,
     CONSTRAINT fk_msg_attachment_msg
         FOREIGN KEY (message_id)
             REFERENCES messages (id)
             ON DELETE CASCADE,
     CONSTRAINT fk_msg_attachment_file
         FOREIGN KEY (attachment_id)
             REFERENCES binary_contents (id)
             ON DELETE CASCADE
);

-- ==========================================
-- binaryContent 테이블 변경
-- ==========================================
ALTER TABLE binary_contents DROP COLUMN bytes;


-- ==========================================
-- 모든 테이블 삭제
-- ==========================================
DELETE FROM message_attachments;
DELETE FROM messages;
DELETE FROM channel_members;
DELETE FROM channels;
DELETE FROM user_statuses;
DELETE FROM users;
DELETE FROM binary_contents;



-- ==========================================
-- 테이블 조회
-- ==========================================
SELECT * FROM message_attachments;
SELECT * FROM messages;
SELECT * FROM channel_members;
SELECT * FROM channels;
SELECT * FROM user_statuses;
SELECT * FROM users;
SELECT * FROM binary_contents;


