package com.sprint.mission.discodeit.entity.base;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    //@CreationTimestamp도 사용가능
    //->디비 서버 기준 시간을 사용하고 싶을 때
    @CreatedDate
    //@CreateDate
    @Column(name="created_at" ,nullable = false, updatable = false)
    //updatable은 jpa레벨에서 동작함
    //디비에는 간단하게 업데이트를 막는 기능은 없음
    private Instant createdAt;
}
