package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.service.jcf.JCFUserService;
import lombok.Data;

import java.util.UUID;

@Data
public class Message {
    //Common field
    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;

    //Channel field
    private Channel channel;
    private User speaker;
    private String content;

    //기본 생성자
    public Message(){
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = this.createdAt;
        this.speaker = JCFUserService.loginUser;
    }

    public Message(Channel channel, String content){
        this();
        this.channel = channel;
        this.content = content;
    }
}
