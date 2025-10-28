package com.sprint.mission.discodeit.entityElement;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;

import java.security.cert.X509Certificate;
import java.time.Instant;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;

public enum ReadStatusElement {

    USER_ID(ReadStatus::getUserId, (x, y) ->
            x.setUserId((UUID) y)),
    CHANNEL_ID(ReadStatus::getChannelId, (x, y) ->
            x.setChannelId((UUID) y)),
    READ_LAST_TIME(ReadStatus::getReadLastTime, (x, y) ->
           x.setReadLastTime((Instant) y));
    public final BiConsumer<ReadStatus, Object> setter;
    public final Function<ReadStatus, Object> getter;
    ReadStatusElement(Function<ReadStatus, Object> getter, BiConsumer<ReadStatus, Object> setter) {
        this.getter = getter;
        this.setter = setter;
    }

}
