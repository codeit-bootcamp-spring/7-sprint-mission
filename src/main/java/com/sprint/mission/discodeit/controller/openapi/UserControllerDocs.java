package com.sprint.mission.discodeit.controller.openapi;


import com.sprint.mission.discodeit.dto.user.request.CreateUserRequestDto;
import com.sprint.mission.discodeit.dto.user.request.UpdateUserRequestDto;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.dto.userstatus.request.UpdateUserStatusRequestDto;
import com.sprint.mission.discodeit.dto.userstatus.response.UserStatusResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Tag(name = "사용자 관리(User Controller)", description = "사용자 생성/수정/삭제/온라인 업데이트를 관리하는 API 입니다. ")
public interface UserControllerDocs {

    @Operation(
            summary = "회원가입",
            description = """
                    새로운 사용자를 등록합니다.
                    
                    ## 요청 데이터(JSON)
                    - userCreateRequest(`@RequestPart` 이름)
                        - username : 아이디
                        - password : 비밀번호
                        - email : 이메일
                        - 모든 정보는 필수 값입니다.
                        - 아이디와 이메일은 중복될 수 없습니다.
                    
                    ## 요청 데이터(Multipart File)
                    - profile(`@RequestPart` 이름) : 사용자 프로필 이미지 (선택 가능)
                    
                    ## 응답 데이터
                    - 성공 시 저장된 회원 정보가 반환됩니다.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "회원 가입 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = User.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "id": "a973c4b1-4f64-46e6-be77-26765e577fee",
                                                "createdAt": "2025-11-12T01:05:28.006980100Z",
                                                "updatedAt": "2025-11-12T01:05:28.006980100Z",
                                                "realName": "user3",
                                                "nickName": "user3",
                                                "email": "user3@naver.com",
                                                "phoneNum": "010-0000-0000",
                                                "username": "user3",
                                                "password": "user12345",
                                                "profileId": "74912c24-8a95-4aa6-862a-e98e00855b6d"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "중복된 정보로 회원 가입 요청(아이디, 이메일)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = com.sprint.mission.discodeit.global.dto.ApiResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "status": "CONFLICT",
                                                "message": "이미 존재하는 아이디입니다.",
                                                "data": null
                                            }
                                            """
                            )

                    )
            )
    })
    ResponseEntity<UserResponseDto> createUser(CreateUserRequestDto requestDto, MultipartFile profile);

    @Operation(
            summary = "사용자 정보 수정",
            description = """
                    기존 사용자 정보를 수정합니다.
                    
                    ## 요청 경로(Path Variable)
                    - userId : 사용자 고유 식별자(UUID)
                    
                    ## 요청 본문(JSON)
                    - userUpdateRequest(`@RequestPart` 이름)
                        - newUsername : 아이디
                        - newPassword : 비밀번호
                        - newEmail : 이메일
                        - 변경하지 않을 필드는 생략 가능
                        - 아이디와 이메일은 중복될 수 없습니다.
                    
                    ## 요청 본문(Multipart File)
                    - profile(`@RequestPart` 이름) : 사용자 프로필 이미지 (선택 가능)
                    
                    ## 응답 데이터
                    - 성공 시 수정된 회원 정보가 반환됩니다.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "사용자 정보 수정 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = User.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "id": "a973c4b1-4f64-46e6-be77-26765e577fee",
                                                "createdAt": "2025-11-12T01:05:28.006980100Z",
                                                "updatedAt": "2025-11-12T01:19:55.678833300Z",
                                                "realName": "user5",
                                                "nickName": "user5",
                                                "email": "user5@naver.com",
                                                "phoneNum": "010-0000-0000",
                                                "username": "user5",
                                                "password": "user123457",
                                                "profileId": "74912c24-8a95-4aa6-862a-e98e00855b6d"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "중복된 정보로 회원 가입 요청(아이디, 이메일)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = com.sprint.mission.discodeit.global.dto.ApiResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "status": "CONFLICT",
                                                "message": "이미 존재하는 아이디입니다.",
                                                "data": null
                                            }
                                            """
                            )

                    )
            )
    })
    ResponseEntity<UserResponseDto> updateUser(UUID userId, UpdateUserRequestDto requestDto, MultipartFile profile);

    @Operation(
            summary = "사용자 정보 삭제",
            description = """
                    기존 사용자 정보를 삭제합니다.
                    
                    ## 요청 경로(Path Variable)
                    - userId : 사용자 고유 식별자(UUID)
                    
                    ## 응답 데이터
                    - 응답 본문은 없습니다.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "사용자 정보 삭제 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Void.class)
                    )

            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "사용자 삭제 요청시 지정한 사용자를 찾을 수 없음",
                    content = @Content(
                            mediaType = "application/json",
                            schema =  @Schema(implementation = com.sprint.mission.discodeit.global.dto.ApiResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "status": "NOT_FOUND",
                                                "message": "해당 사용자를 찾을 수 없습니다.",
                                                "data": null
                                            }
                                            """
                            )
                    )
            )
    })
    ResponseEntity<Void> deleteUser(UUID userId);

    @Operation(
            summary = "사용자 조회",
            description = "모든 사용자 정보를 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "사용자 정보 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = UserResponseDto.class)
                            ),
                            examples = @ExampleObject(
                                    value = """
                                            [
                                                {
                                                    "id": "a973c4b1-4f64-46e6-be77-26765e577fee",
                                                    "createdAt": "2025-11-12T01:05:28.006980100Z",
                                                    "updatedAt": "2025-11-12T01:19:55.678833300Z",
                                                    "username": "user5",
                                                    "email": "user5@naver.com",
                                                    "profileId": "74912c24-8a95-4aa6-862a-e98e00855b6d",
                                                    "online": false
                                                },
                                                {
                                                    "id": "8f624fc1-7613-4b25-96ef-a94c6de7e590",
                                                    "createdAt": "2025-11-12T01:08:47.143231600Z",
                                                    "updatedAt": "2025-11-12T01:08:47.143231600Z",
                                                    "username": "user4",
                                                    "email": "user4@naver.com",
                                                    "profileId": null,
                                                    "online": false
                                                }
                                            ]
                                            """
                            )
                    )
            )
    })
    ResponseEntity<List<UserResponseDto>> searchUsers();

    @Operation(
            summary = "사용자 온라인 업데이트",
            description = """
                    기존 사용자의 온라인 상태(최근 접속 시간)를 갱신 합니다.
                    
                    ## 요청 경로(Path Variable)
                    - userId : 사용자 고유 식별자(UUID)
                    
                    ## 요청 본문(JSON)
                    - newLastActiveAt : 최근 온라인 시각
                    
                    ## 응답 데이터
                    - 성공 시 사용자 정보가 반환됩니다.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "사용자 온라인 업데이트 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserStatus.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "userId": "bea5f619-f39c-4604-8105-70d0f8e8a2a4",
                                                "id": "8c3cac58-5bc6-4d18-ad75-ff72dd6baecb",
                                                "createdAt": "2025-11-11T08:41:15.377166100Z",
                                                "updatedAt": "2025-11-11T08:45:12.728357500Z",
                                                "lastActiveAt": "2025-11-11T02:17:35Z",
                                                "online": false
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "존재하지 않는 사용자 온라인 업데이트 요청",
                    content = @Content(
                            mediaType = "applicaton/json",
                            schema = @Schema(implementation = com.sprint.mission.discodeit.global.dto.ApiResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "status": "NOT_FOUND",
                                                "message": "user status가 존재하지 않습니다.",
                                                "data": null
                                            }
                                            """
                            )
                    )
            )
    })
    ResponseEntity<UserStatusResponseDto> onlineUser(UUID userId, UpdateUserStatusRequestDto request);
}
