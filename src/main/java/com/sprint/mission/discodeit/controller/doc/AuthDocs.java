package com.sprint.mission.discodeit.controller.doc;

import com.sprint.mission.discodeit.dto.login.request.LoginRequestDto;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.global.dto.CustomApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Auth", description = "인증 API")
public interface AuthDocs {

  @Operation(summary = "로그인")
  @ApiResponses({
      @ApiResponse(
          responseCode = "200",
          description = "로그인 성공",
          content = @Content(
              mediaType = "*/*",
              schema = @Schema(implementation = UserResponseDto.class)
          )
      ),
      @ApiResponse(
          responseCode = "401",
          description = "비밀번호가 일치하지 않음",
          content = @Content(
              mediaType = "*/*",
              schema = @Schema(implementation = CustomApiResponse.class)
          )
      ),
      @ApiResponse(
          responseCode = "404",
          description = "사용자를 찾을 수 없음",
          content = @Content(
              mediaType = "*/*",
              schema = @Schema(implementation = CustomApiResponse.class)
          )
      )
  })
  ResponseEntity<UserResponseDto> login(@Parameter(description = "로그인 정보") @Valid @RequestBody
  LoginRequestDto loginRequestDto);
}
