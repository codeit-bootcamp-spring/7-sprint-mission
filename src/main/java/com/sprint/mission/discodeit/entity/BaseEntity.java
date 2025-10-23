package com.sprint.mission.discodeit.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Getter
@EqualsAndHashCode(callSuper = false)
// abstract 키워드를 사용해 이 클래스 자체로는 객체를 만들 수 없도록 합니다.
public abstract class BaseEntity implements Serializable {

    // 자식 클래스에서도 접근할 수 있도록 protected로 선언합니다.
    protected final UUID id;        // 고유아이디
    protected final Long createdAt; // 생성일자
    protected Long updatedAt;       // 갱신일자


    // 생성자: 이 클래스를 상속받는 자식 클래스가 super()를 통해 호출합니다.
    protected BaseEntity() {
        this.id = UUID.randomUUID(); // 고유한 UUID를 생성하여 할당
        long now = System.currentTimeMillis(); // 현재 시간을 유닉스 타임스탬프로 가져옴
        this.createdAt = now;
        this.updatedAt = now;
    }

    // 수정시간 업데이트
    protected void updateTimestamp() {
        this.updatedAt = System.currentTimeMillis();
    }
}