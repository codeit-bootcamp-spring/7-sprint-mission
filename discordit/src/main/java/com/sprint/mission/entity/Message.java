package com.sprint.mission.entity;

import java.io.Serializable;
import java.util.UUID;

public class Message<T extends Receivable> extends BaseEntity {

    // 직렬화 및 역직렬화를 수행할 때 이 클래스의 버전을 의미
    private static final long serialVersionID = 1L;

    private User sender;
    private Receivable receiver;
    private String message;

    public Message(User sender, T receiver, String message) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
    }

    public User getSender() {
        return sender;
    }

    public Receivable getReceiver() {
        return receiver;
    }

    public String getMessage() {
        return message;
    }

    public static <T extends Receivable> Message<T> rehydrate(UUID uuid, Long createdAt, Long updatedAt,
                                                              User sender, T receiver, String message) {
        Message<T> msg = new Message<>(sender, receiver, message);
        msg.uuid = uuid;
        msg.createdAt = createdAt;
        msg.updatedAt = updatedAt;

        return msg;
    }
}