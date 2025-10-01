package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.exception.InvalidInputException;

import java.util.regex.Pattern;

public class User extends BaseEntity {

    private final String email;     // 이메일 -> 아이디로 사용
    private String password;        // 비밀번호
    private String userName;        // 닉네임
    private String phoneNum;        // 전화번호
    private State state;            // 상태

    // regular expression
    private static final Pattern REGEX_EMAIL_PATTERN
            = Pattern.compile("^[a-zA-Z0-9+-_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$");
    private static final Pattern REGEX_PHONE_PATTERN
            = Pattern.compile("^\\d{3}-\\d{4}-\\d{4}$");
    private static final Pattern REGEX_PASSWORD_PATTERN
            = Pattern.compile("^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}$");


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
        validateEmail(email);
        validatePassword(password);
        validateUserName(userName);
        this.email = email;             // 받을때 @ 있는지 확인
        this.password = password;       // 8자리 이상
        this.userName = userName;       // 특수문자 불가
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
        this.userName = userName;
        updateTimestamp();
    }
    public void updatePassword(String password) {
        this.password = password;
        updateTimestamp();
    }
    public void updatePhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
        updateTimestamp();
    }
    public void updateState(State state) {
        this.state = state;
        updateTimestamp();
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", userName='" + userName + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", state=" + state +
                ", id=" + id +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    // 유효성 검사
    private void validateEmail(String email) {
        if (email == null || !REGEX_EMAIL_PATTERN.matcher(email).matches()) {
            throw new InvalidInputException("이메일 형식이 올바르지 않음");
        }
    }

    private void validatePassword(String password) {
        if (password == null || !REGEX_PASSWORD_PATTERN.matcher(password).matches()) {
            throw new InvalidInputException("비밀번호 형식이 올바르지 않음");
        }
    }

    private void validateUserName(String userName) {
        if (userName == null) {
            throw new InvalidInputException("닉네임 형식이 올바르지 않음");
        }
    }


    private void validatePhoneNum(String phoneNum) {
        if (phoneNum == null || !REGEX_PHONE_PATTERN.matcher(phoneNum).matches()) {
            throw new InvalidInputException("전화번호 형식이 올바르지 않음");
        }
    }

}

//https://adjh54.tistory.com/104