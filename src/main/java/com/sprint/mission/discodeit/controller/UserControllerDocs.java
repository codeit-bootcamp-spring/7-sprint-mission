package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.user.UserUpdateRequestDto;
import com.sprint.mission.discodeit.dto.request.userstatus.UserStatusUpdateByUserIdRequestDto;
import com.sprint.mission.discodeit.dto.response.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.response.userstatus.UserStatusResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Tag(name = "유저 관리(User Controller", description = "유저 회원가입, 유저 조회, 유저 갱신 등을 관리하는 API 입니다.")
public interface UserControllerDocs {
    /*
    @Operation(
            summary = "회원 가입",
            description = """
                    새로운 사용자를 등록합니다.
                    
                    ## 요청 데이터
                    - 이름, 이메일, 비밀번호 프로필 이미지 정보가 필요합니다.
                    - 프로필 이미지를 제외한 나머지 정보는 필수 입력입니다.
                    
                    ## 응답
                    - 응답 시 저장된 회원 정보가 반환됩니다.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "회원 가입 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDto.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                             "id": "6d5a2cae-49b3-447e-850d-ae26cbb21740",
                                             "username": "이형일",
                                             "email": "codeit@codeit.com",
                                             "profileId": null,
                                             "online": true,
                                             "createdAt": "2025-11-11T08:48:19.108927Z",
                                             "updatedAt": "2025-11-11T08:48:19.108927Z"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청입니다.",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "timestamp": "2025-11-11T09:09:53.547247Z",
                                              "status": 400,
                                              "error": "Bad Request",
                                              "message": "password length must be between 7 and 50 characters"
                                            }
                                            """
                            )
                    )
            )
    })
    UserResponseDto create(UserCreateRequestDto userCreateRequestDto);

     */


    @Operation(
            summary = "회원 가입 (multipart)",
            description = """
                    새로운 사용자를 등록합니다. (프로필 이미지 포함)

                    - userCreateRequestDto : JSON 형태의 회원 정보
                    - profile : 프로필 이미지 파일 (선택)
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "회원 가입 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDto.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                             "id": "6d5a2cae-49b3-447e-850d-ae26cbb21740",
                                             "username": "이형일",
                                             "email": "codeit@codeit.com",
                                             "profileId": null,
                                             "online": true,
                                             "createdAt": "2025-11-11T08:48:19.108927Z",
                                             "updatedAt": "2025-11-11T08:48:19.108927Z"
                                            }
                                            """
                            )

                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청입니다.",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "timestamp": "2025-11-11T09:09:53.547247Z",
                                              "status": 400,
                                              "error": "Bad Request",
                                              "message": "password length must be between 7 and 50 characters"
                                            }
                                            """
                            )
                    )
            )
    })
    UserResponseDto createMultipart(
            @RequestPart("userCreateRequest") UserCreateRequestDto userCreateRequestDto,
            @RequestPart(value = "profile", required = false) MultipartFile profile);
//    UserResponseDto update(UserUpdateRequestDto userUpdateRequestDto, UUID userId);
    UserResponseDto updateMultipart(UUID userId, UserUpdateRequestDto userUpdateRequestDto, MultipartFile profile);
    void delete(UUID userId);
    List<UserResponseDto> getAll();
//    ResponseEntity<List<UserResponseDto>> findAll();
    UserStatusResponseDto updateUserStatusByUserId(UUID userId,UserStatusUpdateByUserIdRequestDto userStatusUpdateByUserIdRequestDto);
}
