package com.sprint.mission.discodeit.controller.openapi;

import com.sprint.mission.discodeit.dto.auth.request.LoginRequestDto;
import com.sprint.mission.discodeit.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "로그인 관리(AuthController)", description = "로그인을 관리하는 API입니다.")
public interface AuthControllerDocs {

    @Operation(
            summary = "사용자 로그인",
            description = """
                    사용자의 아이디와 비밀번호를 검증하여 로그인합니다.
                    
                    ## 요청 데이터(JSON)
                    - username : 아이디
                    - password : 비밀번호
                    - 모든 정보는 필수 값입니다.
                    
                    ## 응답 데이터
                    - 성공 시 로그인된 사용자의 정보를 반환합니다.
                    """

    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "로그인 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = User.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "id": "4f5f8a05-fdbc-41ad-b736-e6a2695e4e6d",
                                                "createdAt": "2025-11-11T09:06:37.841130500Z",
                                                "updatedAt": "2025-11-11T09:06:37.841130500Z",
                                                "realName": "user1",
                                                "nickName": "user1",
                                                "email": "user1@naver.com",
                                                "phoneNum": "010-0000-0000",
                                                "username": "user1",
                                                "password": "user12345",
                                                "profileId": "b5ffb16b-9c2b-4c58-a324-7b9f59a76626"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "잘못된 아이디나 비밀번호로 로그인 요청",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = com.sprint.mission.discodeit.global.dto.ApiResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "status": "UNAUTHORIZED",
                                                "message": "아이디 또는 비밀번호가 일치하지 않습니다.",
                                                "data": null
                                            }
                                            """
                            )
                    )
            )
    })
    ResponseEntity<User> login(LoginRequestDto loginRequestDto);
}
