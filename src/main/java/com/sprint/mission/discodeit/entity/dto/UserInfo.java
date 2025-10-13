package com.sprint.mission.discodeit.entity.dto;

import com.sprint.mission.discodeit.entity.User;

import java.util.UUID;

public class UserInfo {

    private UUID id;
    private String email;
    private String userName;
    private String state;
    private Long createAt;  // 가입시기
    private String phoneNum;

    public UserInfo(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.userName = user.getUserName();
        this.state = user.getState().getDescState();
        this.createAt = user.getCreatedAt();
        this.phoneNum = user.getPhoneNum();
    }

    public UUID getId() { return id; }
    public String getEmail() { return email; }
    public String getUserName() { return userName; }
    public String getState() { return state; }
    public Long getCreateAt() { return createAt; }
    public String getPhoneNum() { return phoneNum; }

    @Override
    public String toString() {
        return "유저 정보\n" +
                "이름: " + userName + '\n' +
                "이메일: " + email + '\n' +
                "상태: " + state + '\n' +
                "가입 시기: " + createAt + '\n' +
                "전화번호: " + phoneNum + '\n';
    }
}
