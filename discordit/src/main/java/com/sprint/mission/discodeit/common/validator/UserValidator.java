package com.sprint.mission.discodeit.common.validator;

import com.sprint.mission.discodeit.common.config.properties.UserValidationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class UserValidator {
    private final UserValidationProperties props;

    public void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("이메일은 필수입니다.");
        }
        if (!email.matches(props.emailRegex())) {
            throw new IllegalArgumentException("올바른 이메일 형식이 아닙니다.");
        }
    }

    public void validateId(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("아이디는 필수입니다.");
        }
        if (id.length() < props.id().minLength() || id.length() > props.id().maxLength()) {
            throw new IllegalArgumentException(String.format("아이디는 %d에서 %d자 사이로 입력해주세요.", props.id().minLength(), props.id().maxLength()));
        }
        if (!id.matches("^[a-zA-Z0-9_]+$")) {
            throw new IllegalArgumentException("아이디는 영문자, 숫자, 언더바(_)만 사용할 수 있습니다.");
        }
    }

    public void validatePassword(String passwd) {
        if (passwd == null || passwd.isBlank()) {
            throw new IllegalArgumentException("비밀번호는 필수입니다.");
        }
        if (passwd.length() < props.password().minLength() || passwd.length() > props.password().maxLength()) {
            throw new IllegalArgumentException(String.format("비밀번호는 %d에서 %d자 사이로 입력해주세요.", props.password().minLength(), props.password().maxLength()));
        }
    }

    public void validateDisplayName(String displayName) {
        if (displayName == null || displayName.isBlank()) {
            throw new IllegalArgumentException("닉네임은 필수입니다.");
        }
        if (displayName.length() < props.displayName().minLength() || displayName.length() > props.displayName().maxLength()) {
            throw new IllegalArgumentException(String.format("닉네임은 %d에서 %d자 사이로 입력해주세요.", props.displayName().minLength(), props.displayName().maxLength()));
        }
    }
}