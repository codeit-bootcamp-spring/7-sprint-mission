package com.sprint.mission.entity;

import java.io.Serializable;

public class Message extends Common implements Serializable {
    private User sender;
    private User receiver;
    private long time;
    private String content;

    public Message() {
    }


    public  Message(User send,User receiver,String content ){
        this.sender = send;
        this.receiver = receiver;
        this.time = System.currentTimeMillis();
        this.content = content;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Message{" +
                "발신자  =" + sender.getUserName() +
                ", 수신자 =" + receiver.getUserName() +
                ", 생성시간 =" + time +
                ",  내용 ='" + content + '\'' +
                '}';
    }
}
