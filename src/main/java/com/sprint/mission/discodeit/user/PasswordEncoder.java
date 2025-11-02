package com.sprint.mission.discodeit.user;

import org.springframework.context.annotation.Configuration;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Configuration
public class PasswordEncoder {
    private static final String FIXED_SALT = "_DISCODE_IT_STUDY_SALT";
    public String encode(String rawPassword) {
        if (rawPassword == null) {
            return null;
        }
        String saltedPassword = rawPassword + FIXED_SALT;
        return Base64.getEncoder().encodeToString(saltedPassword.getBytes(StandardCharsets.UTF_8));
    }

    public boolean matches(String rawPassword, String encodedPassword) {
        if (encodedPassword == null) {
            return false;
        }
        String newEncodedPassword = encode(rawPassword);
        return newEncodedPassword.equals(encodedPassword);
    }
}
