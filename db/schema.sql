create table binary_contents
(
    binary_content_id uuid primary key,
    file_name         varchar(100)                        not null,
    file_type         varchar(50)                         not null,
    file_size         bigint                                not null,
    created_at        timestamp default current_timestamp not null
);


create table users
(
    user_id        UUID primary key,
    username       varchar(100) unique                 not null,
    password       varchar(100)                        not null,
    email          varchar(100) unique                 not null,
    role           varchar(20)                         not null default 'USER'
        check (role in ('USER', 'CHANNEL_MANAGER', 'ADMIN')),
    last_active_at timestamp,
    profile_id     uuid,
    created_at     timestamp default current_timestamp not null,
    updated_at     timestamp,

    constraint fk_users_binary_contents
        foreign key (profile_id)
            references binary_contents (binary_content_id)
            on delete set null
);

create table user_statuses
(
    user_status_id uuid primary key,
    user_id        uuid unique,
    last_active_at timestamp with time zone,
    created_at     timestamp default current_timestamp not null,
    updated_at     timestamp,

    constraint fk_user_statuses_users
        foreign key (user_id)
            references users (user_id)
            on delete cascade
);


create table channels
(
    channel_id  uuid primary key,
    name        varchar(100),
    description text,
    type        varchar(100) not null default 'PUBLIC',
    created_at  timestamp             default current_timestamp not null,
    updated_at  timestamp,

    constraint chk_channels_type check (type in ('PRIVATE', 'PUBLIC'))
);

create table messages
(
    message_id uuid primary key,
    user_id    uuid                                not null,
    channel_id uuid                                not null,
    content    text                                not null,
    created_at timestamp default current_timestamp not null,
    updated_at timestamp,

    constraint fk_messages_users
        foreign key (user_id)
            references users (user_id)
            on delete cascade,

    constraint fk_messages_channels
        foreign key (channel_id)
            references channels (channel_id)
            on delete cascade
);


create table message_attachments
(
    message_attachment_id uuid primary key,
    message_id            uuid                                not null,
    binary_content_id     uuid                                not null,
    created_at            timestamp default current_timestamp not null,
    updated_at            timestamp,

    constraint fk_message_attachments_messages
        foreign key (message_id)
            references messages (message_id)
            on delete cascade,

    constraint fk_message_attachments_binary_content
        foreign key (binary_content_id)
            references binary_contents (binary_content_id)
            on delete cascade
);

create table read_statuses
(
    read_status_id uuid primary key,
    user_id        uuid                                not null,
    channel_id     uuid                                not null,
    last_read_at   timestamp,
    created_at     timestamp default current_timestamp not null,
    updated_at     timestamp,

    constraint uq_read_statuses_user_channel
        unique (user_id, channel_id),

    constraint fk_read_statuses_users
        foreign key (user_id)
            references users (user_id)
            on delete cascade,

    constraint fk_read_statuses_channels
        foreign key (channel_id)
            references channels (channel_id)
            on delete cascade

);
