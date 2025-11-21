-- channel 테이블 추가
CREATE TABLE channels (
    id          uuid,
	created_at  timestamptz NOT NULL,
	updated_at  timestamptz,
	name        varchar(100),
	description varchar(500),
	type        varchar(10) NOT NULL,

	PRIMARY KEY (id),
	CHECK (type IN ('PUBLIC', 'PRIVATE'))
);

-- binary_contents 테이블 추가
CREATE TABLE binary_contents (
	id           uuid,
	created_at   timestamptz NOT NULL,
	file_name    varchar(255) NOT NULL,
	size         bigint NOT NULL,
	content_type varchar(100) NOT NULL,
	bytes        bytea NOT NULL,

	PRIMARY KEY (id)
);

-- user 테이블 추가
CREATE TABLE users (
	id          uuid,
	created_at  timestamptz NOT NULL,
	updated_at  timestamptz,
	username    varchar(50) NOT NULL UNIQUE,
	email       varchar(100) NOT NULL UNIQUE,
	password    varchar(60) NOT NULL,
	profile_id  uuid  UNIQUE,

	PRIMARY KEY (id),
	FOREIGN KEY (profile_id) REFERENCES binary_contents (id) ON DELETE SET NULL
);


-- user_statuses 테이블 추가
CREATE TABLE user_statuses (
	id             uuid,
	created_at     timestamptz NOT NULL,
	updated_at     timestamptz,
	user_id        uuid        NOT NULL UNIQUE,
	last_active_at timestamptz NOT NULL,

	PRIMARY KEY (id),
	FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

-- read_statuses 테이블 생성
CREATE TABLE read_statuses (
	id           uuid,
	created_at   timestamptz NOT NULL,
	updated_at   timestamptz,
	user_id      uuid NOT NULL UNIQUE,
	channel_id   uuid NOT NULL UNIQUE,
	last_read_at timestamptz NOT NULL,

	PRIMARY KEY (id),
	FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
	FOREIGN KEY (channel_id) REFERENCES channels (id) ON DELETE CASCADE
);

-- messages 테이블 생성
CREATE TABLE messages (
	id         uuid,
	created_at timestamptz NOT NULL,
	updated_at timestamptz,
	content    text,
	channel_id uuid NOT NULL,
	author_id  uuid,

	PRIMARY KEY (id),
	FOREIGN KEY (channel_id) REFERENCES channels (id) ON DELETE CASCADE,
	FOREIGN KEY (author_id) REFERENCES users (id) ON DELETE SET NULL
);

-- message_attachments 테이블 생성
CREATE TABLE message_attachments (
	message_id    uuid NOT NULL,
	attachment_id uuid NOT NULL,

	FOREIGN KEY (message_id) REFERENCES messages (id) ON DELETE CASCADE,
	FOREIGN KEY (attachment_id) REFERENCES binary_contents (id) ON DELETE CASCADE
);