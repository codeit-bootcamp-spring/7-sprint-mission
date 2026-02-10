package com.sprint.mission.discodeit.common.config.properties;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "admin")
public record AdminProperties(
        @NotBlank
        String username,
        @NotBlank
        String password,
        @Email
        String email
) {

}
