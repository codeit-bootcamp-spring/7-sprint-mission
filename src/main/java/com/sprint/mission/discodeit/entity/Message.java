package com.sprint.mission.discodeit.entity;

import java.util.function.BiConsumer;
import java.util.function.Function;

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
        CONTENT(Message::getContent,(x,y) -> x.setContent( (String) y)),
        IS_MARKDOWN(Message::isMarkDown,(x,y)->x.setMarkDown( (boolean) y));

        public BiConsumer<Message, Object> setter;
        public Function<Message,Object> getter;

        messageElement(   Function<Message,Object>  getter,BiConsumer<Message, Object> setter)
        {   this.getter = getter;
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
