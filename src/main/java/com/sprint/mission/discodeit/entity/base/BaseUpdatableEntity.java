package com.sprint.mission.discodeit.entity.base;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.Instant;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 🔥 추가 필수
public abstract class BaseUpdatableEntity extends BaseEntity {

    @Column(name = "updated_at")
    @LastModifiedBy
    private Instant updatedAt;

    @Override
    public String toString() {
        return "✅ BaseUpdatableEntity{" +
             super.toString() +
            "updatedAt=" + updatedAt +
            '}';
    }
}
