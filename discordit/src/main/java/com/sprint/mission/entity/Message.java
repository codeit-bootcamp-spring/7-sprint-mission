package com.sprint.mission.entity;

import java.io.Serializable;

public class Message<T extends Receivable> extends BaseEntity implements Serializable {

    // 직렬화 및 역직렬화를 수행할 때 이 클래스의 버전을 의미
    public static final long serialVersionID = 1L;

    private User sender;
    private Receivable receiver;
    private String message;

    public Message(User sender, T receiver, String message) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
    }

    public void display() {
        System.out.printf("""
                [%s] -> [%s] : %s
                """, sender.getDisplayName(), receiver.getDisplayName(), message);
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
}