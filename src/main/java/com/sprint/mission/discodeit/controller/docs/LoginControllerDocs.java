package com.sprint.mission.discodeit.controller.docs;

import com.sprint.mission.discodeit.service.dto.login.LoginForm;
import com.sprint.mission.discodeit.service.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Login")
public interface LoginControllerDocs {

    @Operation(summary = "login")
    UserResponse login(@Valid @RequestBody LoginForm loginForm);
}
