package com.sprint.mission.discodeit.entity.validator;

import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.global.exception.user.InvalidEmailFormatException;
import com.sprint.mission.discodeit.global.exception.user.InvalidPasswordFormatException;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class UserValidator {
    private static final String EMAIL_PATTERN = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$";

    public static void validateEmail(String email){
        ValidationUtils.validateNotNull(email, "이메일");

        if(!email.matches(EMAIL_PATTERN)){
            throw new InvalidEmailFormatException(
                    ErrorCode.INVALID_EMAIL_FORMAT,
                    Map.of("email", email)
            );
        }
    }

    public static void validatePassword(String password){
        ValidationUtils.validateNotNull(password, "비밀번호");

        if(password.length() < 8 || password.isBlank()){
            throw new InvalidPasswordFormatException(
                    ErrorCode.INVALID_PASSWORD_FORMAT,
                    Map.of("length", password.length(), "isBlank", password.isBlank())
            );
        }
    }
}
