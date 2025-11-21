-- USER 생성은 관리자 계정에서만 가능

-- user 생성 
CREATE USER discodeit_user WITH PASSWORD 'discodeit1234';

-- db 생성 
CREATE DATABASE discodeit OWNER discodeit_user;

-- 권한 부여  = grant / 권한 뺏기 = revoke
GRANT ALL PRIVILEGES ON DATABASE discodeit TO discodeit_user;


-- public 스키마 권한 부여 
GRANT ALL PRIVILEGES ON SCHEMA public TO discodeit_user;

/////////////////////////////////////////////////////////////////////////////////


CREATE TABLE binary_contents(
	id UUID,
	created_at timestamptz NOT NULL,
	file_anme varchar(255) NOT NULL,
	size bigint NOT NULL,
	content_type varchar(100) NOT NULL,
	bytes bytea NOT NULL,
	CONSTRAINT contents_pk PRIMARY KEY (id)
);

DROP TABLE binary_contents;


CREATE TABLE users(
	id UUID,
	created_at timestamptz NOT NULL,
	updated_at timestamptz,
	username varchar(255) NOT NULL,
	email varchar(255) NOT NULL,
	PASSWORD varchar(255) NOT NULL,
	profile_id UUID,
	CONSTRAINT users_pk PRIMARY KEY (id),
	CONSTRAINT users_username_uk UNIQUE (username),
	CONSTRAINT users_email_uk UNIQUE (email),
	CONSTRAINT users_profile_id_fk FOREIGN KEY (profile_id) REFERENCES public.binary_contents(id) ON DELETE SET NULL
);


DROP TABLE users;


CREATE TABLE user_statuses(
	id UUID,
	create_at timestamp NOT NULL,
	update_at timestamp,
	user_id UUID NOT NULL,
	last_active_at timestamp NOT NULL,
	CONSTRAINT user_statuses_pk PRIMARY KEY (id),
	CONSTRAINT user_statuses_fk FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE,
	CONSTRAINT user_statuses_uk UNIQUE (user_id)
);


DROP TABLE user_statuses;



CREATE TABLE channels(
	id UUID,
	create_at timestamp NOT NULL,
	update_at timestamp,
	name varchar(10),
	description varchar(500),
	type varchar(10) NOT NULL CHECK (TYPE IN ('PUBLIC', 'PRIVATE')),
	CONSTRAINT channel_pk PRIMARY KEY (id)
);

DROP TABLE channels;


CREATE TABLE read_statuses(
	id UUID,
	created_at timestamp NOT NULL,
	updated_at timestamp,
	user_id uuid,
	channel_id uuid,
	last_read_at timestamp NOT NULL,
	CONSTRAINT read_statuses_pk PRIMARY KEY (id),
	CONSTRAINT read_statuses_user_id_fk FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE,
	CONSTRAINT read_statuses_channel_id_fk FOREIGN KEY (channel_id) REFERENCES public.channels(id) ON DELETE CASCADE,
	CONSTRAINT read_statuses_user_id_uk UNIQUE (user_id),
	CONSTRAINT read_statuses_channel_id_uk UNIQUE (channel_id)
);

DROP TABLE read_statuses;



CREATE TABLE messages(
	id UUID  NOT NULL,
	created_at timestamp NOT NULL,
	updated_at timestamp,
	content text,
	channel_id uuid NOT NULL,
	author_id uuid,
	CONSTRAINT messages_pk PRIMARY KEY (id),
	CONSTRAINT messages_channel_id_fk FOREIGN KEY (channel_id) REFERENCES public.channels(id) ON DELETE CASCADE,
	CONSTRAINT messages_author_id_fk FOREIGN KEY (author_id) REFERENCES public.users(id) ON DELETE SET null
);

DROP TABLE messages;





CREATE TABLE message_attachments(
	message_id uuid,
	attachment_id uuid,
	CONSTRAINT message_attachments_message_id_fk FOREIGN KEY (message_id) REFERENCES public.messages(id) ON DELETE CASCADE,
	CONSTRAINT message_attachments_attachment_id_fk FOREIGN KEY (attachment_id) REFERENCES public.binary_contents(id) ON DELETE CASCADE
);

DROP TABLE message_attachments;

