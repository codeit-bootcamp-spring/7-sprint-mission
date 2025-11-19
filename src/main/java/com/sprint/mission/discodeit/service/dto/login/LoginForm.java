package com.sprint.mission.discodeit.service.dto.login;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginForm {
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
}
