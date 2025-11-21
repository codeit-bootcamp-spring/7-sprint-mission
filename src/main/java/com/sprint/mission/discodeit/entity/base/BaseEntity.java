package com.sprint.mission.discodeit.entity.base;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @Id
    protected final UUID id;        // 고유아이디

    @CreatedDate
    @Column(nullable = false, updatable = false)
    protected Instant createdAt; // 생성일자

    protected BaseEntity() {
        this.id = UUID.randomUUID(); // 고유한 UUID를 생성하여 할당
    }

}