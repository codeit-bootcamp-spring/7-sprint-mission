package com.sprint.mission.discodeit.entity.dto;

import com.sprint.mission.discodeit.entity.User;

import java.util.UUID;

import static com.sprint.mission.discodeit.entity.dto.ChangeTime.changeTime;

public class UserInfo {

    private final UUID id;
    private final String email;
    private final String userName;
    private final String state;
    private final String createAt;  // 가입시기
    private final String updateAt;
    private final String phoneNum;

    public UserInfo(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.userName = user.getUserName();
        this.state = user.getState().getDescState();
        this.createAt = changeTime(user.getCreatedAt());
        this.phoneNum = user.getPhoneNum();
        this.updateAt = changeTime(user.getUpdatedAt());
    }

    public UUID getId() { return id; }
    public String getEmail() { return email; }
    public String getUserName() { return userName; }
    public String getState() { return state; }
    public String getCreateAt() { return createAt; }
    public String getUpdateAt() { return updateAt; }

    public String getPhoneNum() { return phoneNum; }

    @Override
    public String toString() {
        return "유저 정보\n" +
                "이름: " + userName + '\n' +
                "상태: " + state + '\n' +
                "가입 시기: " + createAt + '\n' +
                "전화번호: " + phoneNum + '\n';
    }
}
