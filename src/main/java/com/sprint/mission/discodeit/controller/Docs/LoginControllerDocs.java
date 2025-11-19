package com.sprint.mission.discodeit.controller.Docs;

import com.sprint.mission.discodeit.dto.user.request.LoginRequest;
import com.sprint.mission.discodeit.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "로그인 관리 (LoginController)", description = "사용자 로그인 및 인증 처리")
public interface LoginControllerDocs {

    @Operation(
            summary = "사용자 로그인",
            description = """
                    사용자가 이메일과 비밀번호를 통해 로그인합니다.

                    **요청 데이터**
                    - email (사용자 이메일)
                    - password (비밀번호)

                    **응답**
                    - 로그인 성공 시 사용자 정보와 함께 인증 성공 상태를 반환합니다.
                    - 비밀번호 또는 이메일이 일치하지 않으면 401 오류를 반환합니다.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "로그인 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = User.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "id": "7f6e9a31-5b23-42c1-8d88-2a8c5d19e56a",
                                              "createdAt": "2025-11-12T11:30:00.000Z",
                                              "updatedAt": "2025-11-12T11:30:00.000Z",
                                              "username": "신제원",
                                              "email": "sinjawon@naver.multi",
                                              "profileId": "c3e4a91b-9a6f-44b2-95e7-4a0bb8f1c124",
                                              "status": "ACTIVE"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 (필드 누락/형식 오류)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            [
                                              "email 은(는) 필수입니다.",
                                              "password 는(은) 필수입니다."
                                            ]
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증 실패 (이메일 또는 비밀번호 불일치)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "error": "INVALID_CREDENTIALS",
                                              "message": "이메일 또는 비밀번호가 올바르지 않습니다."
                                            }
                                            """
                            )
                    )
            )
    })
    ResponseEntity<User> login(LoginRequest req);
}