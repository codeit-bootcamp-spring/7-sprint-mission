package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.util.UUID;

@Getter
public class Auth extends BaseModel{
    private UUID userID;

    public Auth(UUID userID) {
        this.userID = userID;
    }
}
