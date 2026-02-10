package com.sprint.mission.discodeit.common.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "validation.user")
public record UserValidationProperties(
       IdProperties id,
       PasswordProperties password,
       DisplayNameProperties displayName,
       String emailRegex

) {
    public record IdProperties(
            int minLength,
            int maxLength
    ){}

    public record PasswordProperties(
            int minLength,
            int maxLength
    ){}

    public record DisplayNameProperties(
            int minLength,
            int maxLength
    ){}
}
