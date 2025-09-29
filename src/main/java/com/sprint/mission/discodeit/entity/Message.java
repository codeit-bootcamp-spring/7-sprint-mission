package com.sprint.mission.discodeit.entity;

import java.util.function.BiConsumer;

public class Message extends Entity{

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

    public enum messageElement
    {
        CONTENT((x,y) -> x.setContent( (String) y)),
        IS_MARKDOWN((x,y)->x.setMarkDown( (boolean) y));

        public BiConsumer<Message, Object> setter;

        messageElement(BiConsumer<Message, Object> setter)
        {
            this.setter = setter;
        }
    }

    @Override
    public String toString() {
        return "Message{" +
                "content='" + content + '\'' +
                ", sender=" + sender.getNickname() +
                ", isMarkDown=" + isMarkDown +
                '}';
    }
}
