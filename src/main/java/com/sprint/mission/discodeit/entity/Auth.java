package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class Auth extends BaseEntity {
    private UUID userID;

//    public Auth(UUID userID) {
//        this.userID = userID;
//    }
}
