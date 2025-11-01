package com.sprint.mission.discodeit.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString
@Builder
public class User extends BaseEntity {
    private static final long serialVersionUID = 1L;

    //Field
    private String email;         //이메일
    private String nickname;            //닉네임
    private String password;            //비밀번호
    private UUID profileId;             //프로필 이미지 UUID

    //Constructor
    public User(String email, String nickname, String password) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
    }

    //update
    public User update(String email, String nickname, String password) {
        super.update();
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        return this;
    }

    //profile update
    public void updateProfile(UUID profileId) {
        super.update();
        this.profileId = profileId;
    }
}