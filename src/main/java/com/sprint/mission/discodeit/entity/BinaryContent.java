package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public abstract class BinaryContent implements Serializable {
    //Field
    private final UUID id;                  //각 객체 UUID
    private final Instant createdAt;        //객체 생성 일시
    private final byte[] data;              //이미지의 데이터

    //Constructor
    BinaryContent(byte[] data){
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.data = data;
    }
}
