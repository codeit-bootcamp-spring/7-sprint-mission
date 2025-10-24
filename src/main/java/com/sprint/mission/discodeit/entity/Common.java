package com.sprint.mission.discodeit.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter @ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Common implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public final UUID id;
    public final Instant createAt;
    public Instant updateAt;

    public Common() {
        this.id = UUID.randomUUID();
        this.createAt = Instant.now();
        this.updateAt = Instant.now();
    }

    public void touch(){
        this.updateAt = Instant.now();
    }
}
