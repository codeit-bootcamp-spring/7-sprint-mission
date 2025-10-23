package com.sprint.mission.discodeit.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Getter @ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Common implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public final UUID id;
    public final Long createAt;
    public Long updateAt;

    public Common() {
        this.id = UUID.randomUUID();
        this.createAt = System.currentTimeMillis();
        this.updateAt = System.currentTimeMillis();
    }

    public void touch(){
        this.updateAt = System.currentTimeMillis();
    }
}
