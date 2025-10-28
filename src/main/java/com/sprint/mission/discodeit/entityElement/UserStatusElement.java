package com.sprint.mission.discodeit.entityElement;

import com.sprint.mission.discodeit.entity.UserStatus;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;


public enum UserStatusElement {
    USER_ID(UserStatus::getUserId, (x, y) -> x.setUserId((UUID) y)),
    LAST_ONLINE_TIME(UserStatus::getLastOnlineTime, (x, y) ->x.setLastOnlineTime((Instant) y));

    public BiConsumer<UserStatus, Object> setter;
    public Function<UserStatus,Object> getter;

    UserStatusElement(Function<UserStatus,Object> getter, BiConsumer<UserStatus, Object> setter) {
        this.getter = getter;
        this.setter = setter;
    }


}
