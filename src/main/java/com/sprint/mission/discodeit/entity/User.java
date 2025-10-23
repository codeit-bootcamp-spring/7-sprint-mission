package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class User {
    //Common field
    private final UUID id;              //각 유저 UUID
    private final Long createdAt;       //계정 생성
    private Long updatedAt;             //계정 정보 변경

    //User field
    private final String email;         //이메일
    private String nickname;            //닉네임
    private String password;            //비밀번호

    //Constructor
    public User(String nickname, String email, String password){
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = this.createdAt;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }
}
