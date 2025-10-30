package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Message extends BaseEntity {
    private final User author;
    private String content;

    private final User receiver;
    private final Channel channel;

    private final MessageType type;

    // 유저나 채널처럼 입력이 필요하지 않은 정보가 있을 땐 빌더 패턴이 좋아보이지만
    // 전체 입력을 받아야하는 메시지는 안쓰는게 더 좋을지도?
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

    public void updateContent(String content) {
        this.content = content;
        updateTimestamp();
    }
}



