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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter @ToString
@EntityListeners(AuditingEntityListener.class) //1. @CreatedDate @LastModifiedDate 에 필요
@MappedSuperclass // 매핑정보만 상속받는 Superclass라는 의미
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 🔥 추가 필수
public abstract class BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "created_at") // , updatable = false ???
    @CreatedDate
    private Instant createdAt;
}
