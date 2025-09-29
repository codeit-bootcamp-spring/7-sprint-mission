package com.sprint.mission.entity;

public class Message<T extends Receivable> extends BaseEntity {
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
                [%s] -> [%s] :
                %s
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