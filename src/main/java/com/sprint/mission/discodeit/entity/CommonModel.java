package com.sprint.mission.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

/*
Serializable 직렬화
#**보안 고려사항**
        - 직렬화된 데이터는 암호화되지 않습니다
- 민감한 정보는 transient로 표시하거나 암호화해야 합니다
ex) private transient String password; // 직렬화에서 제외됨

# 예외 처리
직렬화/역직렬화 과정에서 발생할 수 있는 주요 예외:
- `IOException`: 입출력 작업 중 발생하는 예외
- `ClassNotFoundException`: 역직렬화 시 클래스를 찾을 수 없을 때 발생
- `InvalidClassException`: serialVersionUID가 일치하지 않을 때 발생
*/

public class CommonModel implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final UUID id;
//    private Long createdAt;
//    private Long updatedAt; // 유닉스 타임스탬프
    private final Instant createdAt;
    private Instant updatedAt; // 유닉스 타임스탬프

    public CommonModel() {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now(); // System.currentTimeMillis();
        this.updatedAt = Instant.now(); // System.currentTimeMillis();
    }

    public UUID getId() {
        return id;
    }

//    public void setId(UUID id) {
//        this.id = id;
//    }

    public Instant getCreatedAt() {
        return createdAt;
    }

//    public void setCreatedAt(Long createdAt) {
//        this.createdAt = createdAt;
//    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = Instant.now(); // System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return  "\n id = " + id +
                ", \n createdAt = " + createdAt +
                ", \n updatedAt = " + updatedAt;
    }
}
