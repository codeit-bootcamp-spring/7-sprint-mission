package com.sprint.mission.discodeit.entity;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;

import java.util.UUID;

@Getter
public class User extends BaseEntity {
    private static final long serialVersionUID = 1L;

    //Field
    @Pattern(regexp = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")
    private String email;         //이메일
    @Pattern(regexp = "^[\\w가-힣]{2,}$")
    private String nickname;            //닉네임
    @Pattern(regexp = "^.{4,}$")
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