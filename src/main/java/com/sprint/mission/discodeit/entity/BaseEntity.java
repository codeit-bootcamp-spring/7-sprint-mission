package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.UUID;

public abstract class BaseEntity implements Serializable {
    //Field
    private final UUID id;              //각 객체 UUID
    private final Long createdAt;       //객체 생성 일시
    private Long updatedAt;             //객체 변경 일시

    //Constructor
    BaseEntity() {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = this.createdAt;
    }

    //Getter
    public UUID getId() {
        return id;
    }

    //객체 변경시 변경 일시 업데이트
    public void update(){
        this.updatedAt = System.currentTimeMillis();
    }
}
