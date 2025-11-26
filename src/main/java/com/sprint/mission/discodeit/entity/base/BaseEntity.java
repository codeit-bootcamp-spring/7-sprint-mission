package com.sprint.mission.discodeit.entity.base;

//import jakarta.persistence.*;
//import lombok.*;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.JpaRepository;

@Getter @ToString
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class) // 날짜 찍어줌. 시간 기록 비서 @CreatedDate
public abstract class BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @CreatedDate
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
}
