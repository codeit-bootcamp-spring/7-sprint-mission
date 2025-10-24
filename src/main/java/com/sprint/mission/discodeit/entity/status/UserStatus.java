package com.sprint.mission.discodeit.entity.status;

import com.sprint.mission.discodeit.entity.common.Common;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class UserStatus extends Common {
   private final   UUID userId;




    public boolean isOnline() {
        long minutes = Duration.between(super.getUpdatedAt(), Instant.now()).toMinutes();
        return minutes <= 5;
    }

}
