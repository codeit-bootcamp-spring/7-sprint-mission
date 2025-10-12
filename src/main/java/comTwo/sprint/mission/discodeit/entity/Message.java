package com2.sprint.mission.discodeit.entity;

/**
 * 디스코드 메시지를 나타내는 엔티티
 * User와 Channel에 종속된다.
 */
public class Message extends BaseEntity {
    private String content;  // 메시지 내용
    private User sender;     // 메시지를 작성한 유저
    private Channel channel; // 메시지가 속한 채널

    public Message(String content, User sender, Channel channel) {
        super();
        this.content = content;
        this.sender = sender;
        this.channel = channel;
    }

    // Getter
    public String getContent() { return content; }
    public User getSender() { return sender; }
    public Channel getChannel() { return channel; }

    // 메시지 수정
    public void update(String content) {
        this.content = content;
        touch();
    }
}