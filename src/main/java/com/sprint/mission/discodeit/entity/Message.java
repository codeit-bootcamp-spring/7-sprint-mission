package com.sprint.mission.discodeit.entity;

public class Message extends BaseEntity {
    private final User sender;
    private final User receiver;
    private String content;


    public Message(User sender, User receiver, String content) {
        super();
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
    }


    public User getSender() {
        return sender;
    }

    public User getReceiver() {
        return receiver;
    }
    public String getContent() {
        return content;
    }

    public void updateContent(String content) {
        this.content = content + " (수정됨)";
        updateTimestamp();
    }

    @Override
    public String toString() {
        return sender.getUserName() + ' ' + createdAt + '\n' +
                "->\t" + content + '\n';
    }
}



