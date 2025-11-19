package com.sprint.mission.discodeit.controller.docs;

import com.sprint.mission.discodeit.dto.auth.request.UserLoginReq;
import com.sprint.mission.discodeit.dto.user.response.UserDetailInfoRes;
import com.sprint.mission.discodeit.exception.ErrorInfoRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Auth", description = "인증 API")
public interface AuthControllerDocs {

  @Operation(summary = "로그인")
  @ApiResponses(
      value = {
          @io.swagger.v3.oas.annotations.responses.ApiResponse(
              responseCode = ResponseCode.OK,
              description = "로그인 성공",
              content = @Content(
                  mediaType = "*/*",
                  schema = @Schema(implementation = UserDetailInfoRes.class),
                  examples = @ExampleObject(
                      name = "로그인 성공 했을 때 반환되는 유저 정보 예시",
                      value = """
                          {
                            "userId": "734ec33f-c6a6-4077-8ef8-70a5c5bd7700",
                             "nickname": "테스터1",
                             "email": "tester1@naver.com",
                             "profileImg": {
                                 "binaryContentId": "9db76f78-7dbb-4956-8669-d25d42bb19cc",
                                 "data": "/9j/4AAQSkZJRgABAQAAAQABAAD/...중략.../2Q==",
                                 "fileName": "D3343D40-5CF8-438E-92D0-4ED668688E43.jpg",
                                 "fileType": "image/jpeg",
                                 "createdAt": "2025-11-15 12:33:34"
                             },
                             "isOnline": true,
                             "createAt": "2025-11-15 12:33:34",
                             "updateAt": "2025-11-15 12:33:34"
                          }
                          """
                  )
              )
          ),
          @io.swagger.v3.oas.annotations.responses.ApiResponse(
              responseCode = ResponseCode.NOT_FOUND,
              description = "사용자를 찾을 수 없음",
              content = @Content(
                  mediaType = "*/*",
                  schema = @Schema(implementation = ErrorInfoRes.class),
                  examples = @ExampleObject(
                      name = "해당 닉네임 유저 없음 예시",
                      value = """
                          {
                               "code": "AUTH_001",
                               "message": "해당 닉네임의 유저가 존재하지 않습니다.",
                               "httpStatus": 404
                          }
                          """
                  )
              )
          ),
          @io.swagger.v3.oas.annotations.responses.ApiResponse(
              responseCode = ResponseCode.UNAUTHORIZED,
              description = "비밀번호가 일치하지 않음",
              content = @Content(
                  mediaType = "*/*",
                  schema = @Schema(implementation = ErrorInfoRes.class),
                  examples = @ExampleObject(
                      name = "비밀번호 틀림 예시",
                      value = """
                          {
                              "code": "AUTH_002",
                              "message": "비밀번호가 올바르지 않습니다.",
                              "httpStatus": 401
                          }
                          """
                  )
              )
          )
      }
  )
  public ResponseEntity<UserDetailInfoRes> login(@Valid @RequestBody UserLoginReq req);
}
