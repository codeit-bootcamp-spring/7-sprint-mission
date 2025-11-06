package com.sprint.mission.discodeit.validator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserValidator {

    @Value("${validation.user.id.min-length}")
    private static int ID_MIN_LENGTH;
    @Value("${validation.user.id.max-length}")
    private static int ID_MAX_LENGTH;
    @Value("${validation.user.password.min-length}")
    private static int PASSWD_MIN_LENGTH;
    @Value("${validation.user.password.max-length}")
    private static int PASSWD_MAX_LENGTH;
    @Value("${validation.user.display-name.min-length}")
    private static int DISPLAY_NAME_MIN_LENGTH;
    @Value("${validation.user.display-name.max-length}")
    private static int DISPLAY_NAME_MAX_LENGTH;
    @Value("${validation.user.email-regex}")
    private static String EMAIL_REGEX;

    public static void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("이메일은 필수입니다.");
        }
        if (!email.matches(EMAIL_REGEX)) {
            throw new IllegalArgumentException("올바른 이메일 형식이 아닙니다.");
        }
    }

    public static void validateId(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("아이디는 필수입니다.");
        }
        if (id.length() < ID_MIN_LENGTH || id.length() > ID_MAX_LENGTH) {
            throw new IllegalArgumentException(String.format("아이디는 %d에서 %d자 사이로 입력해주세요.", ID_MIN_LENGTH, ID_MAX_LENGTH));
        }
    }

    public static void validatePassword(String passwd) {
        if (passwd == null || passwd.isBlank()) {
            throw new IllegalArgumentException("비밀번호는 필수입니다.");
        }
        if (passwd.length() < PASSWD_MIN_LENGTH || passwd.length() > PASSWD_MAX_LENGTH) {
            throw new IllegalArgumentException(String.format("비밀번호는 %d에서 %d자 사이로 입력해주세요.", PASSWD_MIN_LENGTH, PASSWD_MAX_LENGTH));
        }
    }

    public static void validateDisplayName(String displayName) {
        if (displayName == null || displayName.isBlank()) {
            throw new IllegalArgumentException("닉네임은 필수입니다.");
        }
        if (displayName.length() < DISPLAY_NAME_MIN_LENGTH || displayName.length() > DISPLAY_NAME_MAX_LENGTH) {
            throw new IllegalArgumentException(String.format("닉네임은 %d에서 %d자 사이로 입력해주세요.", DISPLAY_NAME_MIN_LENGTH, DISPLAY_NAME_MAX_LENGTH));
        }
    }

    private UserValidator() {
        // 유틸리티 클래스이므로 인스턴스화 방지
    }
}