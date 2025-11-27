package com.sprint.mission.discodeit.entity.base;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@Getter
@MappedSuperclass // 이 클래스는 테이블과 관련이 없고, 컬럼 정보만 자식에게 제공하기 위해서 만든 클래스
@EntityListeners(AuditingEntityListener.class)
// 직접 사용되지 앟고 반드시 상속을 통해 구현되어야 한다는 것을 강조하기 위해 abstract
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID")
    protected UUID id;        // 고유아이디

    @CreatedDate
    @Column(nullable = false, updatable = false)
    protected Instant createdAt; // 생성일자
}