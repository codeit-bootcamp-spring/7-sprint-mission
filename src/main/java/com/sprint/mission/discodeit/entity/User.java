package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.exception.InvalidInputException;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;
import java.util.regex.Pattern;

@Getter
@ToString
public class User extends BaseEntity {

    private String userName;        // 닉네임
    private String email;     // 이메일 -> 아이디로 사용
    private String password;        // 비밀번호

    // 프로필 이미지 추가
    private UUID profileId;

    // regular expression
    private static final Pattern REGEX_EMAIL_PATTERN
            = Pattern.compile("^[a-zA-Z0-9+-_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$");
    private static final Pattern REGEX_PASSWORD_PATTERN
            = Pattern.compile("^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,15}$");

    @Builder
    public User(String email, String password, String userName, String phoneNum) {
        super();

        validateEmail(email);
        validatePassword(password);
        validateUserName(userName);

        this.userName = userName;       // 특수문자 불가
        this.email = email;             // 받을때 @ 있는지 확인
        this.password = password;       // 8자리 이상
        this.profileId = null;
    }

    // Update
    public void updateUserName(String userName) {
        validateUserName(userName);
        this.userName = userName;
        updateTimestamp();
    }

    public void updateEmail(String email) {
        validateEmail(email);
        this.email = email;
        updateTimestamp();
    }

    public void updatePassword(String password) {
        validatePassword(password);
        this.password = password;
        updateTimestamp();
    }

    public void updateProfileId(UUID profileId) {
        this.profileId = profileId;
        updateTimestamp();
    }

    // 논리 삭제
    public void softDelete() {
        this.userName = "Deleted User";
        this.email = this.id.toString() + "@deleted.user";
        this.password = this.id.toString();
        updateTimestamp();  // 탈퇴일자
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
        if (userName == null || userName.isBlank()) {
            throw new InvalidInputException("닉네임 형식이 올바르지 않음");
        }
    }
}