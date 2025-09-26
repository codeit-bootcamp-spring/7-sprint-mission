package com.sprint.mission.entity;

import java.time.Instant;
import java.util.UUID;

public class User extends BaseEntity {

    private String userId;
    private String Passwd;

    private String displayName;
    private String bio;
    private Status onlineStatus;

    private enum Status{
        ONLINE("온라인"),
        OFFLINE("오프라인"),
        AWAY("자리비움"),
        DO_NOT_DISTURB("방해금지");

        Status(String description){
        }

    }


}
