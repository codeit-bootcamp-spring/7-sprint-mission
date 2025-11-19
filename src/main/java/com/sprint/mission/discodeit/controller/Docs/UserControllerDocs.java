package com.sprint.mission.discodeit.controller.Docs;


import com.sprint.mission.discodeit.dto.user.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.user.response.UserDto;
import com.sprint.mission.discodeit.dto.userStatus.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.status.UserStatus;
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

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Tag(name = "사용자 관리 (UserController)",description ="사용자 생성,사용자업데이트,사용자조회,사용자삭제,유저아이디로유저상태 업데이트")
public interface UserControllerDocs {

    @Operation(
            summary = "회원가입",
            description = """
                    새로운 사용자를 등록합니다.
                    
                    **요청 데이터**
                    - 이름, 이메일, 비밀번호,프로필사진(필수x)
                   
                    
                    
                    **응답**
                    - 성공 시 저장된 회원 정보가 반환됩니다.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "회원가입 성공",
                    //스웨거 컨텐트
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = User.class),
                            examples = @ExampleObject(
                                    value = """
                                         {
                                            "username": "멀티못해",
                                            "email": "nomulti@naver.multi",
                                            "password": "1231234",
                                            "profileId": null,
                                            "id": "6c79fd36-db32-46f2-bf46-f5bf66310691",
                                            "createdAt": "2025-11-12T07:44:54.752022800Z",
                                            "updatedAt": null
                                         }
                                         """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 (이메일 중복 , 유효성 검사 실패 등)",
                    //스웨거 컨텐트
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = User.class),
                            examples = @ExampleObject(
                                    value = """
                                            [
                                              "이름이 이미 존재합니다.",
                                              "이메일이 이미 존재합니다."
                                            ]
                                         """
                            )
                    )
            )
    })
    ResponseEntity<User> create(
            UserCreateRequest request,
             MultipartFile profile

    );
    @Operation(
            summary = "유저 업데이트",
            description = """
                    사용자 정보 수정.
                    
                    **수정 요청 데이터**
                    - 이름, 이메일, 비밀번호,이미지(필수x)
                  
                    **응답**
                    - 성공 시 저장된 회원 정보가 반환됩니다.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "유저 업데이트 성공",
                    //스웨거 컨텐트
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = User.class),
                            examples = @ExampleObject(
                                    value = """
                                         {
                                            "username": "수정값",
                                            "email": "수정값@naver.multi",
                                            "password": "1231234",
                                            "profileId": null,
                                            "id": "6c79fd36-db32-46f2-bf46-f5bf66310691",
                                            "createdAt": "2025-11-12T07:44:54.752022800Z",
                                            "updatedAt": null
                                         }
                                         """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 (관련유저없음, 잘못된수정값)",
                    //스웨거 컨텐트
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = User.class),
                            examples = @ExampleObject(
                                    value = """
                                            [ 
                                              "맞는 유저 ID가 없습니다 f832e547-4b4f-4b31-a690-a4c6f441429b",
                                              "이름이 이미 존재합니다.",
                                              "이메일이 이미 존재합니다."
                                            ]
                                         """
                            )
                    )
            )
    })
    ResponseEntity<User> update (UUID userId,
                                 UserUpdateRequest request,
                                 MultipartFile profile) throws IOException;


    @Operation(
            summary = "모든유저 찾기",
            description = """
                    사용자 정보 수정.
                    
                    **수정 요청 데이터**
        
                    **응답**
                    - 성공 시 유저정보+유저상태.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "모든유저찾기 성공",
                    //스웨거 컨텐트
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UserDto.class)),
                            examples = @ExampleObject(
                                    value = """
                                            [
                                            {
                                              "id": "62792ad2-4c6f-4bd8-a05a-b86224417134",
                                              "createdAt": "2025-11-12T08:47:51.451453600Z",
                                              "updatedAt": null,
                                              "username": "string2",
                                              "email": "string2",
                                              "profileId": null,
                                              "online": true
                                            },
                                            {
                                              "id": "320f602a-542d-4f14-8f40-d0674eb5f65c",
                                              "createdAt": "2025-11-12T08:47:55.539346200Z",
                                              "updatedAt": null,
                                              "username": "strin32",
                                              "email": "strin3",
                                              "profileId": null,
                                              "online": true
                                            },
                                            {
                                              "id": "81e104dc-47c6-4252-8ae9-decfaa6138a7",
                                              "createdAt": "2025-11-12T08:47:47.911774100Z",
                                              "updatedAt": null,
                                              "username": "string",
                                              "email": "string",
                                              "profileId": null,
                                              "online": true
                                            }
                                                        ]
                                         """
                            )
                    )
            )
    })
    ResponseEntity<List<UserDto>> findAll();



    @Operation(
            summary = "유저상태 업데이트",
            description = """
                    사용자 정보 수정.
                    
                    **수정 요청 데이터**
                    - 유저UUID ,Instant
                   
                    **응답**
                    - 성공 시 저장된 회원 정보가 반환됩니다.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "유저상태 업데이트 성공",
                    //스웨거 컨텐트
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserStatus.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "id": "9ad6a5ef-1bd3-408c-bc0d-debdb7f9a5e5",
                                              "createdAt": "2025-11-12T09:13:30.002493100Z",
                                              "updatedAt": "2025-11-12T09:14:51.586209300Z",
                                              "userId": "6f01be3c-e8f9-4597-a5b1-02e348db7d36",
                                              "lastActiveAt": "2025-11-12T09:13:43.140Z",
                                              "online": true
                                            }
                                         """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 (UUID가없음)",
                    //스웨거 컨텐트
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserStatus.class),
                            examples = @ExampleObject(
                                    value = """
                                           유저uuid못찾아용   (유저 UUId..)
                                         """
                            )
                    )
            )
    })
    ResponseEntity<UserStatus> updateUserStatusByUserId( UUID userId,
                                                        UserStatusUpdateRequest request);



    @Operation(
            summary = "유저 삭제",
            description = """
                    사용자 정보 수정.
                    
                    **수정 요청 데이터**
                    - 유저UUID
                  
                    **응답**
                    - 성공 시 끝
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "유저 삭제 성공",
                    //스웨거 컨텐트
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseEntity.class),
                            examples = @ExampleObject(
                                    value = """
                                         아무것도 없으요
                                         """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 (UUID가없음)",
                    //스웨거 컨텐트
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ResponseEntity.class),
                            examples = @ExampleObject(
                                    value = """
                                           유저uuid못찾아용   (유저 UUId..)
                                         """
                            )
                    )
            )
    })
    ResponseEntity<Void> delete(UUID userId);

}
