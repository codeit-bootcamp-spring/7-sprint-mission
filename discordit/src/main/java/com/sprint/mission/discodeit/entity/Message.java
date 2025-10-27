package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.util.UUID;

@Getter
public class Message<T extends Receivable> extends BaseEntity {

    // 직렬화 및 역직렬화를 수행할 때 이 클래스의 버전을 의미
    private static final long serialVersionID = 1L;

    private final User sender;
    private final Receivable receiver;
    private final String message;

    public Message(User sender, T receiver, String message) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
    }

    // 더미 데이터 생성 or 파일 데이터 복원용 (createdAt, updateAt 조작 위해 엔터티 내 배치.)
    public static <T extends Receivable> Message<T> fromDto(UUID uuid, Long createdAt, Long updatedAt,
                                                            User sender, T receiver, String message) {
        Message<T> msg = new Message<>(sender, receiver, message);
        msg.uuid = uuid;
        msg.createdAt = createdAt;
        msg.updatedAt = updatedAt;

        return msg;
    }
}