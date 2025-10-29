package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString
public class User extends BaseEntity {
    private static final long serialVersionUID = 1L;

    //Field
    private final String email;         //이메일
    private String nickname;            //닉네임
    private String password;            //비밀번호
    private UUID profileId;             //프로필 이미지 UUID

    //Constructor
    public User(String email, String nickname, String password){
        this.email = email;
        this.nickname = nickname;
        this.password = password;
    }

    //update Nickname, password
    public User update(String nickname, String password) {
        super.update();
        this.nickname = nickname;
        this.password = password;
        return this;
    }

    //프로필 이미지 업데이트
    public void updateProfileImg(UUID profileId){
        this.profileId = profileId;
    }
}
