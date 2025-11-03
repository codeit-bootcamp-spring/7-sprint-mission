package com.sprint.mission.discodeit.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter @ToString
public class Common implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @EqualsAndHashCode.Include
    private final UUID id;
    private final Instant createAt;

    public Common() {
        this.id = UUID.randomUUID();
        this.createAt = Instant.now();
    }

}
