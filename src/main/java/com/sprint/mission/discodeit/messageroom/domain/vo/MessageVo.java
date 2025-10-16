package com.sprint.mission.discodeit.messageroom.domain.vo;

import lombok.Getter;

import java.util.UUID;

@Getter
public class MessageVo {
    private final UUID senderId;
    private final String content;


    public MessageVo(UUID senderId, String content) {
        this.content = content;
        this.senderId = senderId;
    }

    //불변 객체의 내용을 바꾸고 새로운 객체를 반환하고 싶을 때는
    //with를 앞에 불인다
    //예) public Message withContent(){~}
}
