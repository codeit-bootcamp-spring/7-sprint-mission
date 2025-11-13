package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.authService.LoginRequestDto;
import com.sprint.mission.discodeit.dto.response.LoginResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Tag(name = "로그인 API(Login Controller)",description = "로그인을 위한 API 입니다.")
public interface LoginControllerDocs {
    @Operation(summary = "로그인(Login)", description = "로그인을 해도 되는 유저인지 검증하는 메서드입니다.")
    @ApiResponse(responseCode = "200", description = "로그인 성공",
    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponseDto.class),
    examples = @ExampleObject(value = """
          {
          "id":UUID,
          "createdAt":Instant,
          "updatedAt":Instant,
          "username": "Ronaldo",
          "email":"Ronaldo@Goat.com",
          "password": "1111",
          "profileId":UUId
          }
          """)
    )
    )
    @ApiResponse(responseCode = "400", description = "유효하지 않은 id랑 비밀번호를 치면 발생합니다")
    @RequestMapping(value ="/login", method = RequestMethod.POST)
    ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto dto);
}
