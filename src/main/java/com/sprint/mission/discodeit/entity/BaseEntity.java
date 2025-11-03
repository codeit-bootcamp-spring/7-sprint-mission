package com.sprint.mission.discodeit.entity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
/*
1. id : 객체를 식별하기 위한 id로 UUID 타입으로 선언
2. createAt, updateAt: 각각 객체의 생성, 수정 시간을 유닉스 타임스탬프로
나타내기 위한 필드로 long 타입으로 선언한다.
 */

@Getter
@EqualsAndHashCode(of = "id")
@ToString
public abstract class BaseEntity implements Serializable {
    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;
    private static final long serialVersionUID = 1L;

    protected BaseEntity() {
        id = UUID.randomUUID();
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    protected void reUpdatedAt() {
        updatedAt = Instant.now();
    }
}
