-- ----------------------------------------------------
-- DATABASE INITIALIZATION FOR discodeit
-- ----------------------------------------------------

-- USER TABLE ---------------------------------------------------
CREATE TABLE users (
                       id UUID PRIMARY KEY,
                       name VARCHAR(50) NOT NULL,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       created_at TIMESTAMP NOT NULL,
                       updated_at TIMESTAMP NOT NULL
);

-- USER_STATUS TABLE --------------------------------------------
CREATE TABLE user_status (
                             id UUID PRIMARY KEY,
                             user_id UUID UNIQUE, -- 1:1 관계이므로 UNIQUE
                             last_seen TIMESTAMP,
                             created_at TIMESTAMP NOT NULL,
                             updated_at TIMESTAMP NOT NULL,
                             CONSTRAINT fk_userstatus_user
                                 FOREIGN KEY (user_id)
                                     REFERENCES users(id)
                                     ON DELETE CASCADE
);

-- CHANNEL TABLE ------------------------------------------------
CREATE TABLE channels (
                          id UUID PRIMARY KEY,
                          name VARCHAR(100) NOT NULL,
                          created_at TIMESTAMP NOT NULL,
                          updated_at TIMESTAMP NOT NULL
);

-- MESSAGE TABLE ------------------------------------------------
CREATE TABLE messages (
                          id UUID PRIMARY KEY,
                          content TEXT NOT NULL,
                          author_id UUID NOT NULL,
                          channel_id UUID NOT NULL,
                          created_at TIMESTAMP NOT NULL,
                          updated_at TIMESTAMP NOT NULL,

                          CONSTRAINT fk_message_user
                              FOREIGN KEY (author_id)
                                  REFERENCES users(id)
                                  ON DELETE CASCADE,

                          CONSTRAINT fk_message_channel
                              FOREIGN KEY (channel_id)
                                  REFERENCES channels(id)
                                  ON DELETE CASCADE
);

-- BINARY_CONTENT TABLE -----------------------------------------
CREATE TABLE binary_contents (
                                 id UUID PRIMARY KEY,
                                 file_name VARCHAR(255) NOT NULL,
                                 size BIGINT NOT NULL,
                                 content_type VARCHAR(255),
                                 message_id UUID,
                                 user_id UUID,
                                 created_at TIMESTAMP NOT NULL,
                                 updated_at TIMESTAMP NOT NULL,

                                 CONSTRAINT fk_binary_message
                                     FOREIGN KEY (message_id)
                                         REFERENCES messages(id)
                                         ON DELETE CASCADE,

                                 CONSTRAINT fk_binary_user
                                     FOREIGN KEY (user_id)
                                         REFERENCES users(id)
                                         ON DELETE SET NULL
);

-- READ_STATUS TABLE --------------------------------------------
CREATE TABLE read_status (
                             id UUID PRIMARY KEY,
                             user_id UUID NOT NULL,
                             channel_id UUID NOT NULL,
                             last_read_at TIMESTAMP,
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

-- ----------------------------------------------------
-- INDEXES
-- ----------------------------------------------------

CREATE INDEX idx_message_channel ON messages(channel_id);
CREATE INDEX idx_message_author ON messages(author_id);

CREATE INDEX idx_binarycontent_message ON binary_contents(message_id);
CREATE INDEX idx_binarycontent_user ON binary_contents(user_id);

CREATE INDEX idx_readstatus_user ON read_status(user_id);
CREATE INDEX idx_readstatus_channel ON read_status(channel_id);
