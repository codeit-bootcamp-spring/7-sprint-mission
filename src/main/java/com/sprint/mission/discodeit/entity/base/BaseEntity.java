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
@EntityListeners(AuditingEntityListener.class) // 날짜 찍어줌. 시간 기록 비서 @CreatedDate
@MappedSuperclass
public abstract class BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "created_at", nullable = false) // , updatable = false ??? 이것땜시 에러! 왜 넣었을까?!!!!!
    @CreatedDate
    private Instant createdAt;
}
