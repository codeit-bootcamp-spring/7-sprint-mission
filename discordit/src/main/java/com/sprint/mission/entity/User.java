package com.sprint.mission.entity;

import java.time.Instant;
import java.util.UUID;

public class User {
    private UUID uuid;
    private Long createdAt;
    private Long updatedAt;

    private String userId;
    private String Passwd;

    private String displayName;
    private String bio;
    private Status onlineStatus;


    public User() {
        this.uuid = UUID.randomUUID();
        this.createdAt = getUnixTimestamp();
    }

    private static long getUnixTimestamp() {
        return Instant.now().getEpochSecond();
    }



    private enum Status{
        ONLINE("온라인"),
        OFFLINE("오프라인"),
        AWAY("자리비움"),
        DO_NOT_DISTURB("방해금지");

        private final String description;
        Status(String description){
            this.description = description;
        }

        public String getDescription(){
            return this.description;
        }
    }


}
