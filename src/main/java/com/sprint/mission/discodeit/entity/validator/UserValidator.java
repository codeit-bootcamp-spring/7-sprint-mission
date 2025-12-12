package com.sprint.mission.discodeit.entity.validator;

import com.sprint.mission.discodeit.global.exception.custom.CustomException;
import com.sprint.mission.discodeit.global.exception.custom.ErrorCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserValidator {
    private static final String EMAIL_PATTERN = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$";

    public static void validateEmail(String email){
        ValidationUtils.validateNotNull(email, "이메일");

        if(!email.matches(EMAIL_PATTERN)){
            log.warn("유효하지 않은 이메일 형식: {}", email);
            throw new CustomException(ErrorCode.INVALID_EMAIL_FORMAT);
        }
    }

    public static void validatePassword(String password){
        ValidationUtils.validateNotNull(password, "비밀번호");

        if(password.length() < 8 || password.isBlank()){
            log.warn("유효하지 않은 비밀번호 형식: (길이={}, 공백여부={})", password.length(), password.isBlank());
            throw new CustomException(ErrorCode.INVALID_PASSWORD_FORMAT);        }
    }
}
