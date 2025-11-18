package com.sprint.mission.discodeit.entity;

/*
Serializable 직렬화
#**보안 고려사항**
- 직렬화된 데이터는 암호화되지 않습니다
- 민감한 정보는 transient로 표시하거나 암호화해야 합니다
ex) private transient String newPassword; // 직렬화에서 제외됨
*/

import lombok.Getter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter @ToString
public class BaseModel implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private UUID id;
    private final Instant createdAt;
    private Instant updatedAt;

    public BaseModel() {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void setUpdatedAtNow() {
        this.updatedAt = Instant.now();
    }

    public void setNewLastActiveAt(Instant newLastActiveAt) {
      this.updatedAt = newLastActiveAt;
    }
}
