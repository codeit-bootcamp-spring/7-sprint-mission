package com.sprint.mission.discodeit.controller.docs;

import com.sprint.mission.discodeit.dto.user.request.UserInfoReq;
import com.sprint.mission.discodeit.dto.user.response.UserDetailInfoRes;
import com.sprint.mission.discodeit.dto.user.response.UserSimpleInfoRes;
import com.sprint.mission.discodeit.dto.userStatus.response.UserStatusSimpleViewRes;
import com.sprint.mission.discodeit.exception.ErrorInfoRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "User", description = "User API")
public interface UserControllerDocs {

  @Operation(summary = "전체 User 목록 조회")
  @ApiResponses(
      value = {
          @io.swagger.v3.oas.annotations.responses.ApiResponse(
              responseCode = ResponseCode.OK,
              description = "User 목록 조회 성공",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = UserSimpleInfoRes.class),
                  examples = @ExampleObject(
                      name = "User 목록 조회 성공 예시",
                      value = """
                          [
                               {
                                   "userId": "734ec33f-c6a6-4077-8ef8-70a5c5bd7700",
                                   "nickname": "테스터1",
                                   "email": "tester1@naver.com",
                                   "profileImg": {
                                       "binaryContentId": "9db76f78-7dbb-4956-8669-d25d42bb19cc",
                                       "data": "/9j/...중략.../2Q==",
                                       "fileName": "D3343D40-5CF8-438E-92D0-4ED668688E43.jpg",
                                       "fileType": "image/jpeg",
                                       "createdAt": "2025-11-15 12:33:34"
                                   },
                                   "isOnline": false
                               },
                               {
                                   "userId": "a44804a8-178c-4946-bf5a-3db0087d8832",
                                   "nickname": "테스터2",
                                   "email": "tester2@naver.com",
                                   "profileImg": {
                                       "binaryContentId": "14447de7-6590-445d-b0a9-e75d032938e1",
                                       "data": "UklGRhRpA...중략...AAAA==",
                                       "fileName": "cat.jpg",
                                       "fileType": "image/jpeg",
                                       "createdAt": "2025-11-15 13:35:54"
                                   },
                                   "isOnline": true
                               }
                           ]
                          """
                  )
              )
          )
      }
  )
  ResponseEntity<List<UserSimpleInfoRes>> getAllUsers();

  @Operation(summary = "User 상세정보 조회")
  @ApiResponses(
      value = {
          @io.swagger.v3.oas.annotations.responses.ApiResponse(
              responseCode = ResponseCode.OK,
              description = "User 상세정보 조회 성공",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = UserSimpleInfoRes.class),
                  examples = @ExampleObject(
                      name = "User 상세정보 조회 성공 예시",
                      value = """
                          {
                                "userId": "a44804a8-178c-4946-bf5a-3db0087d8832",
                                "nickname": "테스터2",
                                "email": "tester2@naver.com",
                                "profileImg": {
                                    "binaryContentId": "14447de7-6590-445d-b0a9-e75d032938e1",
                                    "data": "UklGRhRpAAB...중략...AAAA==",
                                    "fileName": "cat.jpg",
                                    "fileType": "image/jpeg",
                                    "createdAt": "2025-11-15 13:35:54"
                                },
                                "isOnline": false,
                                "createAt": "2025-11-15 13:35:54",
                                "updateAt": "2025-11-15 13:35:54"
                            }
                          """
                  )
              )
          ),
          @io.swagger.v3.oas.annotations.responses.ApiResponse(
              responseCode = ResponseCode.NOT_FOUND,
              description = "사용자를 찾을 수 없음",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorInfoRes.class),
                  examples = @ExampleObject(
                      name = "해당 UUID 를 가지고 있는 유저 찾을 수 없음 예시",
                      value = """
                          {
                              "code": "USER_001",
                              "message": "해당 UUID를 가진 유저가 존재하지 않습니다.",
                              "httpStatus": 404
                          }
                          """
                  )
              )
          ),
      }
  )
  ResponseEntity<UserDetailInfoRes> getUserByUserId(@PathVariable UUID userId);

  @Operation(summary = "User 등록")
  @ApiResponses(
      value = {
          @io.swagger.v3.oas.annotations.responses.ApiResponse(
              responseCode = ResponseCode.CREATED,
              description = "User가 성공적으로 생성됨",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = UserDetailInfoRes.class),
                  examples = @ExampleObject(
                      name = "User가 성공적으로 생성 예시",
                      value = """
                          {
                                "userId": "a44804a8-178c-4946-bf5a-3db0087d8832",
                                "nickname": "테스터2",
                                "email": "tester2@naver.com",
                                "profileImg": {
                                    "binaryContentId": "14447de7-6590-445d-b0a9-e75d032938e1",
                                    "data": "UklGRhRpAAB...중략...AAAA==",
                                    "fileName": "cat.jpg",
                                    "fileType": "image/jpeg",
                                    "createdAt": "2025-11-15 13:35:54"
                                },
                                "isOnline": false,
                                "createAt": "2025-11-15 13:35:54",
                                "updateAt": "2025-11-15 13:35:54"
                            }
                          """
                  )
              )
          ),
          @io.swagger.v3.oas.annotations.responses.ApiResponse(
              responseCode = ResponseCode.CONFLICT,
              description = "중복된 이메일 입력",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorInfoRes.class),
                  examples = @ExampleObject(
                      name = "중복된 이메일로 계정을 생성할 수 없음 예시",
                      value = """
                          {
                              "code": "USER_002",
                              "message": "이미 존재하는 이메일로 값을 지정할 수 없습니다.",
                              "httpStatus": 409
                          }
                          """
                  )
              )
          ),
          @io.swagger.v3.oas.annotations.responses.ApiResponse(
              responseCode = ResponseCode.CONFLICT,
              description = "중복된 닉네임 입력",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorInfoRes.class),
                  examples = @ExampleObject(
                      name = "중복된 닉네임으로 계정을 생성할 수 없음 예시",
                      value = """
                          {
                              "code": "USER_003",
                              "message": "이미 존재하는 닉네임으로 값을 지정할 수 없습니다.",
                              "httpStatus": 409
                          }
                          """
                  )
              )
          ),
          @io.swagger.v3.oas.annotations.responses.ApiResponse(
              responseCode = ResponseCode.BAD_REQUEST,
              description = "이메일 형식에 맞지 않게 입력",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorInfoRes.class),
                  examples = @ExampleObject(
                      name = "이메일 형식에 맞지 않게 입력 예시",
                      value = """
                          {
                              "code": "AUTH_003",
                              "message": "회원가입 이메일 형식이 올바르지 않습니다.",
                              "httpStatus": 400
                          }
                          """
                  )
              )
          ),
          @io.swagger.v3.oas.annotations.responses.ApiResponse(
              responseCode = ResponseCode.BAD_REQUEST,
              description = "2글자 미만의 닉네임 입력",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorInfoRes.class),
                  examples = @ExampleObject(
                      name = "2글자 미만의 닉네임 입력 예시",
                      value = """
                          {
                              "code": "AUTH_004",
                              "message": "회원가입 닉네임은 2글자 이상이어야 합니다.",
                              "httpStatus": 400
                          }
                          """
                  )
              )
          ),
          @io.swagger.v3.oas.annotations.responses.ApiResponse(
              responseCode = ResponseCode.BAD_REQUEST,
              description = "6자 이하의 비밀번호 입력",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorInfoRes.class),
                  examples = @ExampleObject(
                      name = "6자 이하의 비밀번호로 계정을 생성할 수 없음 예시",
                      value = """
                          {
                              "code": "AUTH_005",
                              "message": "회원가입 비밀번호는 최소 6글자 이상이어야 합니다.",
                              "httpStatus": 400
                          }
                          """
                  )
              )
          )
      }
  )
  ResponseEntity<UserDetailInfoRes> createUser(
      @Valid @RequestPart("userInfoReq") UserInfoReq userInfoReq,
      @RequestPart(value = "profile", required = false) MultipartFile profileFile);

  @Operation(summary = "User 정보 수정")
  @ApiResponses(
      value = {
          @io.swagger.v3.oas.annotations.responses.ApiResponse(
              responseCode = ResponseCode.OK,
              description = "User가 성공적으로 수정됨",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = UserDetailInfoRes.class),
                  examples = @ExampleObject(
                      name = "User가 성공적으로 수정 예시",
                      value = """
                          {
                                "userId": "a44804a8-178c-4946-bf5a-3db0087d8832",
                                "nickname": "테스터2",
                                "email": "tester2@naver.com",
                                "profileImg": {
                                    "binaryContentId": "14447de7-6590-445d-b0a9-e75d032938e1",
                                    "data": "UklGRhRpAAB...중략...AAAA==",
                                    "fileName": "cat.jpg",
                                    "fileType": "image/jpeg",
                                    "createdAt": "2025-11-15 13:35:54"
                                },
                                "isOnline": false,
                                "createAt": "2025-11-15 13:35:54",
                                "updateAt": "2025-11-15 13:35:54"
                            }
                          """
                  )
              )
          ),
          @io.swagger.v3.oas.annotations.responses.ApiResponse(
              responseCode = ResponseCode.NOT_FOUND,
              description = "해당 UUID 를 가진 사용자가 없음",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorInfoRes.class),
                  examples = @ExampleObject(
                      name = "해당 UUID 를 가진 사용자가 없음 예시",
                      value = """
                          {
                              "code": "USER_001",
                              "message": "해당 UUID를 가진 유저가 존재하지 않습니다.",
                              "httpStatus": 404
                          }
                          """
                  )
              )
          ),
          @io.swagger.v3.oas.annotations.responses.ApiResponse(
              responseCode = ResponseCode.CONFLICT,
              description = "중복된 이메일 입력",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorInfoRes.class),
                  examples = @ExampleObject(
                      name = "중복된 이메일로 계정을 생성할 수 없음 예시",
                      value = """
                          {
                              "code": "USER_002",
                              "message": "이미 존재하는 이메일로 값을 지정할 수 없습니다.",
                              "httpStatus": 409
                          }
                          """
                  )
              )
          ),
          @io.swagger.v3.oas.annotations.responses.ApiResponse(
              responseCode = ResponseCode.CONFLICT,
              description = "중복된 이메일 입력",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorInfoRes.class),
                  examples = @ExampleObject(
                      name = "중복된 이메일로 계정을 생성할 수 없음 예시",
                      value = """
                          {
                              "code": "USER_002",
                              "message": "이미 존재하는 이메일로 값을 지정할 수 없습니다.",
                              "httpStatus": 409
                          }
                          """
                  )
              )
          ),
          @io.swagger.v3.oas.annotations.responses.ApiResponse(
              responseCode = ResponseCode.CONFLICT,
              description = "중복된 닉네임 입력",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorInfoRes.class),
                  examples = @ExampleObject(
                      name = "중복된 닉네임으로 계정을 생성할 수 없음 예시",
                      value = """
                          {
                              "code": "USER_003",
                              "message": "이미 존재하는 닉네임으로 값을 지정할 수 없습니다.",
                              "httpStatus": 409
                          }
                          """
                  )
              )
          ),
          @io.swagger.v3.oas.annotations.responses.ApiResponse(
              responseCode = ResponseCode.BAD_REQUEST,
              description = "이메일 형식에 맞지 않게 입력",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorInfoRes.class),
                  examples = @ExampleObject(
                      name = "이메일 형식에 맞지 않게 입력 예시",
                      value = """
                          {
                              "code": "AUTH_003",
                              "message": "회원가입 이메일 형식이 올바르지 않습니다.",
                              "httpStatus": 400
                          }
                          """
                  )
              )
          ),
          @io.swagger.v3.oas.annotations.responses.ApiResponse(
              responseCode = ResponseCode.BAD_REQUEST,
              description = "2글자 미만의 닉네임 입력",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorInfoRes.class),
                  examples = @ExampleObject(
                      name = "2글자 미만의 닉네임 입력 예시",
                      value = """
                          {
                              "code": "AUTH_004",
                              "message": "회원가입 닉네임은 2글자 이상이어야 합니다.",
                              "httpStatus": 400
                          }
                          """
                  )
              )
          ),
          @io.swagger.v3.oas.annotations.responses.ApiResponse(
              responseCode = ResponseCode.BAD_REQUEST,
              description = "6자 이하의 비밀번호 입력",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorInfoRes.class),
                  examples = @ExampleObject(
                      name = "6자 이하의 비밀번호로 계정을 생성할 수 없음 예시",
                      value = """
                          {
                              "code": "AUTH_005",
                              "message": "회원가입 비밀번호는 최소 6글자 이상이어야 합니다.",
                              "httpStatus": 400
                          }
                          """
                  )
              )
          )
      }
  )
  ResponseEntity<UserDetailInfoRes> updateUser(
      @PathVariable UUID userId,
      @Valid @RequestPart("userInfoReq") UserInfoReq userInfoReq,
      @RequestPart(value = "profile", required = false) MultipartFile profileFile);

  @Operation(summary = "User 삭제")
  @ApiResponses(
      value = {
          @io.swagger.v3.oas.annotations.responses.ApiResponse(
              responseCode = ResponseCode.NO_CONTENT,
              description = "User가 성공적으로 삭제됨"
          ),
          @io.swagger.v3.oas.annotations.responses.ApiResponse(
              responseCode = ResponseCode.NOT_FOUND,
              description = "해당 UUID 를 가진 사용자가 없음",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorInfoRes.class),
                  examples = @ExampleObject(
                      name = "해당 UUID 를 가진 사용자가 없음 예시",
                      value = """
                          {
                              "code": "USER_001",
                              "message": "해당 UUID를 가진 유저가 존재하지 않습니다.",
                              "httpStatus": 404
                          }
                          """
                  )
              )
          ),
      }
  )
  ResponseEntity<Void> deleteUser(@PathVariable UUID userId);


  @Operation(summary = "User 온라인 상태 업데이트")
  @ApiResponses(
      value = {
          @io.swagger.v3.oas.annotations.responses.ApiResponse(
              responseCode = ResponseCode.OK,
              description = "User 온라인 상태가 성공적으로 업데이트됨",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = UserStatusSimpleViewRes.class),
                  examples = @ExampleObject(
                      name = "User 온라인 상태가 성공적으로 업데이트됨 예시",
                      value = """
                          {
                              "isOnline": false
                          }
                          """
                  )
              )
          ),
          @io.swagger.v3.oas.annotations.responses.ApiResponse(
              responseCode = ResponseCode.NOT_FOUND,
              description = "해당 UUID 를 가진 사용자가 없음",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorInfoRes.class),
                  examples = @ExampleObject(
                      name = "해당 UUID 를 가진 사용자가 없음 예시",
                      value = """
                          {
                              "code": "USER_001",
                              "message": "해당 UUID를 가진 유저가 존재하지 않습니다.",
                              "httpStatus": 404
                          }
                          """
                  )
              )
          ),
      }
  )
  ResponseEntity<UserStatusSimpleViewRes> updateUserStatus(@PathVariable UUID userId);
}
