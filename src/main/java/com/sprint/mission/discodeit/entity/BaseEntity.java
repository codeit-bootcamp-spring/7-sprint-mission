package com.sprint.mission.discodeit.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    //@CreationTimestamp도 사용가능
    //->디비 서버 기준 시간을 사용하고 싶을 때
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;
}
