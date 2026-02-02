package com.sprint.mission.discodeit.api;

import com.sprint.mission.discodeit.dto.auth.AuthLoginRequestDto;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.ErrorResponse;

@Tag(name = "Auth API", description = "인증 관련 API")
public interface AuthApi {
    @Operation(summary = "로그인 요청")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "찾을수없습니다",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))
            )
    })
    ResponseEntity<UserResponseDto> login(AuthLoginRequestDto authLoginRequestDto);

    ResponseEntity<Void> getCsrfToken(CsrfToken csrfToken);
}
