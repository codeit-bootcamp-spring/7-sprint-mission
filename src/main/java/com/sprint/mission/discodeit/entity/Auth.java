package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

import java.util.UUID;

@Getter
public class Auth extends BaseEntity {
    private UUID userID;

    public Auth(UUID userID) {
        this.userID = userID;
    }
}
