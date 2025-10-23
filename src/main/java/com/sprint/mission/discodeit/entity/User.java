package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class User extends BaseEntity {
    //Field
    private final String email;         //이메일
    private String nickname;            //닉네임
    private String password;            //비밀번호

    //Constructor
    public User(String nickname, String email, String password){
        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }
}
