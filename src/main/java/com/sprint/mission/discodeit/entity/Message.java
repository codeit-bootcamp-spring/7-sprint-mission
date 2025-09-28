package com.sprint.mission.discodeit.entity;

public class Message {

    private String content;
    private User sender;
    private boolean isMarkDown;

    public Message(String content, User sender, boolean isMarkDown) {
        super();
        this.content = content;
        this.sender = sender;
        this.isMarkDown = isMarkDown;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public boolean isMarkDown() {
        return isMarkDown;
    }

    public void setMarkDown(boolean markDown) {
        isMarkDown = markDown;
    }
}
