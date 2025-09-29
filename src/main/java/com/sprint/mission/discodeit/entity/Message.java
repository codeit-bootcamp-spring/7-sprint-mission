package com.sprint.mission.discodeit.entity;

public class Message extends BaseEntity {
    public String content;
    private String name;

    public Message() {}
    public Message(String message, String name) {
        this.content = message;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String sendMessage(String msg) {
        content = msg;
//        Channel channel = new Channel(name);
//        if (msg != null) {
//            channel.setAlarm(true);
//        }
        return content;
    }
}
