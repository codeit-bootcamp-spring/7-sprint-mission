package com.sprint.mission.discodeit.entity.base;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
/*
    JPA에서 엔티티 클래스들 간에 공통된 매핑 정보를 재사용하기 위한 어노테이션입니다.
    이걸로 지정된 클래스는 테이블에 매핑되지 않음.
 */
@MappedSuperclass
 /*
    JPA에서 제공하는 7가지 event를 자동으로 관리
        @PrePersist : Persist(insert)메서드가 호출되기 전에 실행되는 메서드
        @PreUpdate : merge메서드가 호출되기 전에 실행되는 메서드
        @PreRemove : Delete메서드가 호출되기 전에 실행되는 메서드
        @PostPersist : Persist(insert)메서드가 호출된 이후에 실행되는 메서드
        @PostUpdate : merge메서드가 호출된 후에 실행되는 메서드
        @PostRemove : Delete메서드가 호출된 후에 실행되는 메서드
        @PostLoad : Select조회가 일어난 직후에 실행되는 메서드
  */
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
}
