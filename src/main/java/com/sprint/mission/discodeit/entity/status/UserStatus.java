package com.sprint.mission.discodeit.entity.status;

import com.sprint.mission.discodeit.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserStatus extends BaseEntity {

    private UUID userId;


    public void updateLastAccess() {
        updateTimestamp();
    }

    // 5분이 안지났으면 온라인
    public boolean isOnline() {
        Instant expirationTime = updatedAt.plusSeconds(300);
        // 만료시간(업데이트시간 + 5분)이 현재시간보다 뒤에 있는가? ture : false
        return expirationTime.isAfter(Instant.now());
    }

}

