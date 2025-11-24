package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

@Getter
@ToString
public abstract class BaseUpdatableEntity extends BaseEntity{

    @LastModifiedDate
    private Instant updatedAt;
}
