package com.sprint.mission.discodeit.controller.docs;

import com.sprint.mission.discodeit.service.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.service.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.service.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.service.dto.response.UserResponse;
import com.sprint.mission.discodeit.service.dto.response.UserStatusResponse;
import io.swagger.v3.oas.annotations.Operation;
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

@Tag(name = "User")
public interface UserControllerDocs {


    @Operation(summary = "전체 User 조회")
    @ApiResponse(responseCode = "200", description = "User 목록 조회 성공",
            content = @Content(
                    examples = @ExampleObject(
                            value = """
                                    [
                                    {
                                    "id": "200d357d-02fe-4989-92f8-1f93944bb736",
                                    "createAt": "2025-11-13T00:43:47.256649Z",
                                    "updatedAt": "2025-11-13T00:43:47.256649Z",
                                    "email": "test1234@gmail.com",
                                    "username": "testman",
                                    "profileId": null,
                                    "online": false
                                    }
                                    ]
                                    """)))
    List<UserResponse> getAllUsers();

    @Operation(summary = "User등록")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User가 성공적으로 생성됨",
                    content = @Content(
                            schema = @Schema(implementation = UserResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                            "id": "200d357d-02fe-4989-92f8-1f93944bb736",
                                            "createAt": "2025-11-13T00:43:47.256649Z",
                                            "updatedAt": "2025-11-13T00:43:47.256649Z",
                                            "email": "test1234@gmail.com",
                                            "username": "testman",
                                            "profileId": null,
                                            "online": false
                                            }
                                            """))),
            @ApiResponse(responseCode = "400", description = "같은 email을 사용하는 User가 이미 존재함",
                    content = @Content(examples = @ExampleObject(value = "이미 존재하는 아이디입니다.")

                    ))})
    ResponseEntity<UserResponse> createUser(UserCreateRequest userCreateRequest,
                                            MultipartFile profileImage);


    @Operation(summary = "User 정보 수정")
    @ApiResponse(responseCode = "200", description = "User 정보 수정 성공",
            content = @Content(
                    schema = @Schema(implementation = UserResponse.class),
                    examples = @ExampleObject(
                            value = """
                                            {
                                            "id": "200d357d-02fe-4989-92f8-1f93944bb736",
                                            "createAt": "2025-11-13T00:43:47.256649Z",
                                            "updatedAt": "2025-11-13T00:43:47.256649Z",
                                            "email": "test1234@gmail.com",
                                            "username": "testman",
                                            "profileId": null,
                                            "online": false
                                            }
                                            """)))
    UserResponse updateUser(
            UUID userId,
            UserUpdateRequest userUpdateRequest,
            MultipartFile profile);

    @Operation(summary = "User삭제")
    @ApiResponse(responseCode = "204", description = "User 삭제 성공")
    String deleteUser(UUID userId);


    @Operation(summary = "User 온라인 상태 업데이트")
    @ApiResponse(responseCode = "200", description = "User 온라인 상태 업데이트 성공",
            content = @Content(
                    schema = @Schema(implementation = UserResponse.class),
                    examples = @ExampleObject(
                            value = """
                                            {
                                            "id": "200d357d-02fe-4989-92f8-1f93944bb736",
                                            "createAt": "2025-11-13T00:43:47.256649Z",
                                            "updatedAt": "2025-11-13T00:43:47.256649Z",
                                            "email": "test1234@gmail.com",
                                            "username": "testman",
                                            "profileId": null,
                                            "online": false
                                            }
                                            """)))
    UserStatusResponse markOnline(UUID userId, UserStatusUpdateRequest request);
}
