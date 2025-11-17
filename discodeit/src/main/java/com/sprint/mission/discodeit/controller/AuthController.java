package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthService authService;

  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  @Operation(summary = "로그인 API", description = "username과 password를 이용한 로그인")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "로그인 성공"),
      @ApiResponse(responseCode = "400", description = "비밀번호가 일치하지 않음",

          // breaking 떠서 추가하라고 하길래 추가했는데 꼭 해야 되나요?
          // @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음") <- 원래 이 형태였어요
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(example = "{\"error\": \"비밀번호 불일치\"}")
          )),
      @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
  })
  @RequestMapping(method = RequestMethod.POST, value = "/login", consumes = "application/json")
  public ResponseEntity<UserDto> login(@RequestBody LoginRequest loginRequest) {
    User user = authService.login(loginRequest);

    UserDto userDto = new UserDto(
        user.getId(),
        user.getCreatedAt(),
        user.getUpdatedAt(),
        user.getUsername(),
        user.getEmail(),
        user.getProfileId(),
        true // 로그인 성공하면 online true
    );
    return ResponseEntity.ok(userDto);
  }
}
