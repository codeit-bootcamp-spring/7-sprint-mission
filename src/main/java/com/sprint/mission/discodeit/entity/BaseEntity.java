package com.sprint.mission.discodeit.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Getter
@MappedSuperclass // 상속을 통해 자식 클래스에게 컬럼 정보를 제공(테이블과는 관련 없음)
@EntityListeners(AuditingEntityListener.class)
// 직접 사용되지 않고 반드시 상속을 통해 구현되어야 한다는 것을 강조하기 위해 abstract로 선언
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Override
    public String toString() {
        return  ", id=" + id +
                ", createdAt=" + createdAt;
    }

    //id를 통해서만 동일한 객체인지 비교
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BaseEntity that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
