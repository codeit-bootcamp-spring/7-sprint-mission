package com.sprint.mission.discodeit.entity.base;

import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@MappedSuperclass
public abstract class BaseUpdateEntity extends BaseEntity {

    @LastModifiedDate
    private Instant updatedAt;


}
