package com.sprint.mission.discodeit.entity;


import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class User extends BaseEntity {
    private String username; // 유저 이름 ( 별명 x)
    private String password;
    private String email;
    private UserState userState; // 0: 오프라인 , 1: 온라인

    public User(String username, String password, String email) {
        this.username = VerifiedUtils.verifyName(username);
        this.password = VerifiedUtils.verifyPassword(password);
        this.email = VerifiedUtils.verifyEmail(email);
        this.userState = UserState.ONLINE;
    }

    public void setUserState(UserState userState) {
        UserState state = VerifiedUtils.verifyNull(userState);
        if (this.userState != state) {
            this.userState = state;
            reUpdatedAt();
        }
    }

    public void setUsername(String username) {
        String v = VerifiedUtils.verifyName(username);
        if (!v.equals(this.username)) {
            this.username = v;
            reUpdatedAt();
        }
    }

    public void setPassword(String password) {
        String v = VerifiedUtils.verifyPassword(password);
        if (!v.equals(this.password)) {
            this.password = v;
            reUpdatedAt();
        }
    }


    public void setEmail(String email) {
        String v = VerifiedUtils.verifyEmail(email);
        if (!v.equals(this.email)) {
            this.email = v;
            reUpdatedAt();
        }
    }

    public void update(String username, String password, String email) {
        if (username != null) setUsername(username);
        if (password != null) setPassword(password);
        if (email != null) setEmail(email);
    }

    public boolean passwordMatch(String password) {
        return this.password.equals(password);
    }
}
