CREATE TABLE `users` (
	`id`	PK	NOT NULL,
	`created_at`	VARCHAR(255)	NOT NULL,
	`updated_at`	VARCHAR(255)	NULL,
	`username`	UK	NOT NULL,
	`email`	UK	NOT NULL,
	`password`	VARCHAR(255)	NOT NULL,
	`profile_id`	VARCHAR(255)	NULL
);

CREATE TABLE `user_statuses` (
	`id`	PK	NOT NULL,
	`created_at`	VARCHAR(255)	NOT NULL,
	`updated_at`	VARCHAR(255)	NULL,
	`last_active_at`	VARCHAR(255)	NOT NULL,
	`id2`	FK	NOT NULL
);

CREATE TABLE `binary_contents` (
	`id`	VARCHAR(255)	NOT NULL,
	`created_at`	VARCHAR(255)	NOT NULL,
	`file_name`	VARCHAR(255)	NOT NULL,
	`size`	VARCHAR(255)	NOT NULL,
	`Field`	VARCHAR(255)	NULL
);

CREATE TABLE `channels` (
	`id`	PK	NOT NULL,
	`created_at`	VARCHAR(255)	NOT NULL,
	`updated_at`	VARCHAR(255)	NULL,
	`name`	VARCHAR(255)	NOT NULL,
	`description`	VARCHAR(255)	NULL,
	`type`	VARCHAR(255)	NOT NULL,
	`Field`	VARCHAR(255)	NULL	COMMENT 'ENUM(PUBLIC, PRIVATE)'
);

CREATE TABLE `messages` (
	`id`	PK	NOT NULL,
	`author_id`	PK	NOT NULL,
	`channel_id`	PK	NOT NULL,
	`created_at`	VARCHAR(255)	NOT NULL,
	`updated_at`	VARCHAR(255)	NULL,
	`content`	VARCHAR(255)	NOT NULL
);

CREATE TABLE `read_statuses` (
	`id`	VARCHAR(255)	NOT NULL,
	`user_id`	PK	NOT NULL,
	`channel_id`	PK	NOT NULL,
	`created_at`	VARCHAR(255)	NOT NULL,
	`updated_at`	VARCHAR(255)	NULL,
	`last_read_at`	VARCHAR(255)	NOT NULL
);

CREATE TABLE `message_attachments` (
	`message_id`	PK	NOT NULL,
	`attachment_id`	VARCHAR(255)	NOT NULL
);

ALTER TABLE `users` ADD CONSTRAINT `PK_USERS` PRIMARY KEY (
	`id`
);

ALTER TABLE `user_statuses` ADD CONSTRAINT `PK_USER_STATUSES` PRIMARY KEY (
	`id`
);

ALTER TABLE `binary_contents` ADD CONSTRAINT `PK_BINARY_CONTENTS` PRIMARY KEY (
	`id`
);

ALTER TABLE `channels` ADD CONSTRAINT `PK_CHANNELS` PRIMARY KEY (
	`id`
);

ALTER TABLE `messages` ADD CONSTRAINT `PK_MESSAGES` PRIMARY KEY (
	`id`
);

ALTER TABLE `read_statuses` ADD CONSTRAINT `PK_READ_STATUSES` PRIMARY KEY (
	`id`
);

