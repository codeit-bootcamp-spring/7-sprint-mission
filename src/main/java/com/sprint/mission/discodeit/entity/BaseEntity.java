package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public abstract class BaseEntity implements Serializable {
    //Field
    private final UUID id;              //각 객체 UUID
    private final Instant createdAt;       //객체 생성 일시
    private Instant updatedAt;             //객체 변경 일시

    //Constructor
    BaseEntity() {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    //객체 변경시 변경 일시 업데이트
    public void update(){
        this.updatedAt = Instant.now();
    }
}
