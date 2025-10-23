package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message {
    //Common field
    private final UUID id;              //각 메세지 UUID
    private final Long createdAt;       //메세지 생성일
    private Long updatedAt;             //메세지 수정일

    //Message field
    //[Need Fix] : 아래 둘 final 로 변경
    private UUID speakerId;             //화자 UUID
    private UUID channelId;             //채널 UUID
    private String content;             //메세지 내용

    //Constructor
    public Message(UUID channelId, String content) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = this.createdAt;
        //[Need Fix] : loginUser를 speaker 로 넣기
        //this.speaker = JCFUserService.loginUser;
        this.channelId = channelId;
        this.content = content;
    }
}
