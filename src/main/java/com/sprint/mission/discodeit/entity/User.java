package com.sprint.mission.discodeit.entity;


import lombok.Data;

import java.util.UUID;

@Data
public class User {
    //Common field
    private final UUID id;
    private final Long createdAt;
    private Long updatedAt;

    //User field
    private String nickname;
    private String email;
    private String password;

    //기본 생성자
    public User(){
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = this.createdAt;
    }

    public User(String nickname, String email, String password){
        this();
        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }
}
