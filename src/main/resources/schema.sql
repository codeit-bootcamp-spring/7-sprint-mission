-- channel 테이블 추가
CREATE TABLE channels (
    id          uuid,
	created_at  timestamp with time zone NOT NULL,
	updated_at  timestamp with time zone,
	name        varchar(100),
	description varchar(500),
	type        varchar(10) NOT NULL,

	PRIMARY KEY (id),
	CHECK (type IN ('PUBLIC', 'PRIVATE'))
);

-- binary_contents 테이블 추가
CREATE TABLE binary_contents (
	id           uuid,
	created_at   timestamp with time zone NOT NULL,
	updated_at   timestamp with time zone,
	file_name    varchar(255) NOT NULL,
	size         bigint NOT NULL,
	content_type varchar(100) NOT NULL,
	status       varchar(20) NOT NULL,

	PRIMARY KEY (id)
);

-- user 테이블 추가
CREATE TABLE users (
	id          uuid,
	created_at  timestamp with time zone NOT NULL,
	updated_at  timestamp with time zone,
	username    varchar(50) NOT NULL UNIQUE,
	email       varchar(100) NOT NULL UNIQUE,
	password    varchar(60) NOT NULL,
	profile_id  uuid  UNIQUE,
	role varchar(20) NOT NULL,

	PRIMARY KEY (id),
	FOREIGN KEY (profile_id) REFERENCES binary_contents (id) ON DELETE SET NULL
);


-- user_statuses 테이블 추가
CREATE TABLE user_statuses (
	id             uuid,
	created_at     timestamp with time zone NOT NULL,
	updated_at     timestamp with time zone,
	user_id        uuid        NOT NULL UNIQUE,
	last_active_at timestamp with time zone NOT NULL,

	PRIMARY KEY (id),
	FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

-- read_statuses 테이블 생성
CREATE TABLE read_statuses (
	id           uuid,
	created_at   timestamp with time zone NOT NULL,
	updated_at   timestamp with time zone,
	user_id      uuid NOT NULL,
	channel_id   uuid NOT NULL,
	last_read_at timestamp with time zone NOT NULL,

	PRIMARY KEY (id),
	FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
	FOREIGN KEY (channel_id) REFERENCES channels (id) ON DELETE CASCADE,
	UNIQUE (user_id, channel_id)
);

-- messages 테이블 생성
CREATE TABLE messages (
	id         uuid,
	created_at timestamp with time zone NOT NULL,
	updated_at timestamp with time zone,
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

-- Remember-Me 토큰 저장 테이블
CREATE TABLE IF NOT EXISTS persistent_logins (
    -- 시리즈 식별자 (Primary Key)
    -- UUID 형식, 로그인 세션마다 고유
    series VARCHAR(64) PRIMARY KEY,

    -- 사용자명
    -- User 테이블의 username과 연결
    username VARCHAR(64) NOT NULL,

    -- 인증 토큰
    -- 매번 변경되는 토큰, SecureRandom 생성
    token VARCHAR(64) NOT NULL,

    -- 마지막 사용 시간
    -- 토큰이 마지막으로 사용된 시각
    -- 오래된 토큰 정리에 사용
    last_used TIMESTAMP NOT NULL
);

-- 성능 최적화: username으로 조회 가능
-- 사용자의 모든 Remember-Me 토큰 조회 시 사용
CREATE INDEX IF NOT EXISTS idx_persistent_logins_username
    ON persistent_logins(username);