package com.sprint.mission.discodeit.swaggerDocs;

import com.sprint.mission.discodeit.dto.AuthServiceDto;
import com.sprint.mission.discodeit.dto.Res_UserLogin;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

//@Tag : API 그룹화, 일반적으로 Controller 클래스 단위로 정의
@Tag(name="Auth", description="인증 API")
public interface AuthDoc {
    //@Operation : 엔드포인트에 대한 설명
    @Operation(summary = "로그인")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "로그인 성공",
            content = @Content(
                schema = @Schema(implementation = Res_UserLogin.class)
            )
        ),

        @ApiResponse(
            responseCode = "400",
            description = "비밀번호가 일치하지 않음",
            content = @Content(
                examples = @ExampleObject(value = "Wrong password")
            )
        ),

        @ApiResponse(
            responseCode = "404",
            description = "사용자를 찾을 수 없음",
            content = @Content(
                examples = @ExampleObject(value = "User with username {username} not found")
            )
        )
    })
    ResponseEntity<Res_UserLogin> login(@RequestBody AuthServiceDto authServiceDto);
}
