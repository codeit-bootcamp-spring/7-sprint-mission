package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.dto.user.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.common.Common;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
public class User extends Common implements Serializable {
    private static final long serialVersionUID = 1L;

    private String UserEmail;
    private String password;
    private  String userName;
    private  String userNickname;
    private  String profilePicture;





    public User(String userId,String password,String userName,String userNickname)
    {
           this.UserEmail = userId;
           this.userName = userName;
           this.password = password;
           this.userNickname = userNickname;
    }



    @Override
    public String toString() {
        return "User{" +
                "아이디 = '" + UserEmail + '\'' +
                ", 비번 = '" + password + '\'' +
                ", 이름 = '" + userName + '\'' +
                ", 닉네임 = '" + userNickname + '\'' +
                ", 닉네임 = '" + this.getId() + '\'' +
                '}';
    }

    //업데이트하면 하난하나 적용해서 트루로체크해
    //끝가지가면 트루 아니면 false된다
    public void update(UserUpdateRequest userUpdateRequest) {
        boolean anyValueUpdated = false;
        if (userUpdateRequest.username() != null && !userUpdateRequest.username().equals(this.userName)) {
            this.userName = userUpdateRequest.username();
            anyValueUpdated = true;
        }
        if (userUpdateRequest.email() != null && !userUpdateRequest.email().equals(this.UserEmail)) {
            this.UserEmail = userUpdateRequest.email();
            anyValueUpdated = true;
        }
        if (userUpdateRequest.Password() != null && !userUpdateRequest.Password().equals(this.password)) {
            this.password = userUpdateRequest.Password();
            anyValueUpdated = true;
        }
        if (userUpdateRequest.userNickname() != null && !userUpdateRequest.userNickname().equals(this.userNickname)) {
            this.userNickname = userUpdateRequest.userNickname();
            anyValueUpdated = true;
        }
        if (userUpdateRequest.profileImageUrl() != null && !userUpdateRequest.profileImageUrl().equals(this.profilePicture)) {
            this.profilePicture = userUpdateRequest.profileImageUrl();
        }

        if (anyValueUpdated) {
            this.setUpdatedAt(Instant.ofEpochSecond(Instant.now().getEpochSecond()));
        }
    }





}
