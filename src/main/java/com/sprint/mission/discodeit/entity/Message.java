package com.sprint.mission.discodeit.entity;

public class Message extends BaseEntity {
    private final User author;
    private String content;

    private final User receiver;
    private final Channel channel;

    private final MessageType type;

    public enum MessageType {
        CHANNEL("채널메시지"), DIRECT("개인메시지");
        private final String desc;
        MessageType(String description) {
            this.desc = description;
        }
        public String getDesc() {
            return desc;
        }
    }   // CHANNEL, DIRECT

    public Message(User author, User receiver, String content) {
        super();
        this.author = author;
        this.receiver = receiver;
        this.content = content;
        this.type = MessageType.DIRECT;
        this.channel = null;
    }   // DM
    public Message(User author, Channel channel, String content) {
        super();
        this.author = author;
        this.content = content;
        this.channel = channel;
        this.type = MessageType.CHANNEL;
        this.receiver = null;
    }   // CM

    public User getAuthor() {return author;}
    public User getReceiver() {return receiver;}
    public String getContent() {return content;}
    public Channel getChannel() {return channel;}
    public MessageType getType() {return type;}

    public void updateContent(String content) {
        this.content = content;
        updateTimestamp();
    }


    @Override
    public String toString() {
        return "Message{" +
                "author=" + author +
                ", content='" + content + '\'' +
                ", receiver=" + receiver +
                ", channel=" + channel +
                ", type=" + type +
                ", createdAt=" + createdAt +
                ", id=" + id +
                ", updatedAt=" + updatedAt +
                '}';
    }
}



