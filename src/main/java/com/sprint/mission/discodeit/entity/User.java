package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.dto.user.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.common.Common;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter

public class User extends Common implements Serializable {
    private static final long serialVersionUID = 1L;

    private  String username;
    private String email;
    private String password;
    private  String userNickname;
    private UUID profileId;





    public User(String email, String password, String username, UUID profileId)
    {
           this.email = email;
           this.password = password;
           this.username = username;
           this.profileId = profileId;
    }

    public void setProfileId(UUID profileId) {
        this.profileId = profileId;
    }

    @Override
    public String toString() {
        return "User{" +
                "아이디 = '" + email + '\'' +
                ", 비번 = '" + password + '\'' +
                ", 이름 = '" + username + '\'' +
                ", 닉네임 = '" + userNickname + '\'' +
                ", 닉네임 = '" + this.getId() + '\'' +
                '}';
    }


    //업데이트하면 하난하나 적용해서 트루로체크해
    //끝가지가면 트루 아니면 false된다
    public void update(UserUpdateRequest userUpdateRequest) {
        boolean anyValueUpdated = false;
        if (userUpdateRequest.username() != null && !userUpdateRequest.username().equals(this.username)) {
            this.username = userUpdateRequest.username();
            anyValueUpdated = true;
        }
        if (userUpdateRequest.email() != null && !userUpdateRequest.email().equals(this.email)) {
            this.email = userUpdateRequest.email();
            anyValueUpdated = true;
        }
        if (userUpdateRequest.password() != null && !userUpdateRequest.password().equals(this.password)) {
            this.password = userUpdateRequest.password();
            anyValueUpdated = true;
        }
        if (userUpdateRequest.userNickname() != null && !userUpdateRequest.userNickname().equals(this.userNickname)) {
            this.userNickname = userUpdateRequest.userNickname();
            anyValueUpdated = true;
        }
        if (userUpdateRequest.profileImageId() != null && !userUpdateRequest.profileImageId().equals(this.profileId)) {
            this.profileId = userUpdateRequest.profileImageId();
        }

        if (anyValueUpdated) {
            this.setUpdatedAt(Instant.ofEpochSecond(Instant.now().getEpochSecond()));
        }
    }


}
