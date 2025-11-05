package com.sprint.mission.discodeit.entity;

import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    private User(String email, String nickname, String password, UUID profileId) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.profileId = profileId;
    }

    //Factory Method
    public static User createWithProfile(String email, String nickname, String password, UUID profileId) {
        return new User(email, nickname, password, profileId);
    }

    public static User createWithoutProfile(String email, String nickname, String password) {
        return new User(email, nickname, password, null);
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