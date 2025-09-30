package com.sprint.mission.discodeit.entity;

import java.util.ArrayList;
import java.util.List;

public class User extends BaseEntity {

    private final String email;       // 이메일 -> 아이디로 사용
    private String password;        // 비밀번호
    private String userName;        // 닉네임
    private String phoneNum;        // 전화번호
    private State state;    // 상태

    public enum State {
        ONLINE("온라인"), AFK("자리비움"),
        DND("방해금지"), OFFLINE("오프라인");

        private final String descState;

        State(String description) {
            this.descState = description;
        }

        public String getDescState() {
            return descState;
        }
    }



    public User(String email, String password, String userName) {
        super();
        this.email = email;     // 받을때 @ 있는지 확인
        this.password = password;   // 8자리 이상
        this.userName = userName;   // 특수문자 불가
        this.state = State.ONLINE;      // 기본 상태
    }

    public User(String email, String password, String userName, String phoneNum) {
        this(email, password, userName);
        this.phoneNum = phoneNum;
    }

    // Getter
    public String getPassword() {
        return password;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public State getState() {
        return state;
    }

    // Update
    public void updateUserName(String userName) {
        this.userName = userName;       // 특수문자금지
        updateTimestamp();
    }
    public void updatePassword(String password) {
        this.password = password;       // 변경 시 본인확인? 8자리
        updateTimestamp();
    }
    public void updatePhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;       // 01012345678 11자리
        updateTimestamp();
    }
    public void updateState(State state) {
        this.state = state;
        updateTimestamp();
    }

    @Override
    public String toString() {
        return "User{" +
                " 이름: '" + userName + '\'' +
                ", 상태: " + state +
                ", 생성일자: " + createdAt +
                ", 갱신일자: " + updatedAt +
                " }";
    }
}