package com.sprint.mission.discodeit.controller.docs;

import com.sprint.mission.discodeit.dto.entity.auth.request.UserLoginRequest;
import com.sprint.mission.discodeit.dto.entity.auth.response.UserLoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "인증 관리", description = "사용자 인증 관련 API입니다.")
public interface AuthControllerDocs {
    @Operation(
            summary = "사용자 로그인",
            description = "사용자 로그인을 처리합니다"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공")
    })
    UserLoginResponse login(@Valid @RequestBody UserLoginRequest request);
}