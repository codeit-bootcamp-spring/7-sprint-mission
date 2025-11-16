package com.sprint.mission.discodeit.swaggerDocs;

import com.sprint.mission.discodeit.entity.dto.Dto_UserCreate;
import com.sprint.mission.discodeit.entity.dto.Dto_UserStatusUpdate;
import com.sprint.mission.discodeit.entity.dto.Dto_UserUpdate;
import com.sprint.mission.discodeit.entity.dto.Res_User;
import com.sprint.mission.discodeit.entity.dto.Res_UserStatus;
import com.sprint.mission.discodeit.entity.dto.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Tag(name="User", description="User API")
public interface UserDoc {

    /**
     * GET /api/users - 전체 User 목록 조회
     */
    @Operation(summary = "전체 User 목록 조회")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "User 목록 조회 성공",
            content = @Content(
                schema = @Schema(implementation = UserDto.class, type = "array")
            )
        )
    })
    ResponseEntity<List<UserDto>> findAll();

    /**
     * POST /api/users - User 등록
     * Multipart 요청을 위한 문서화는 Object 타입으로 처리하고, @RequestBody 대신 @RequestPart에 대한 설명을 추가합니다.
     */
    @Operation(
        summary = "User 등록 (프로필 이미지 포함)",
        description = "**Content-Type: multipart/form-data** 요청입니다."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "User가 성공적으로 생성됨",
            content = @Content(
                schema = @Schema(implementation = Res_User.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "같은 email 또는 username를 사용하는 User가 이미 존재함",
            content = @Content(
                examples = @ExampleObject(value = "User with email {email} already exists")
            )
        )
    })
    ResponseEntity<Res_User> create(
        @Parameter(description = "User 생성 정보 (JSON)") @RequestPart(value = "userCreateRequest") Dto_UserCreate dtoUser,
        @Parameter(description = "프로필 이미지 파일 (선택 사항)") @RequestPart(value = "profile", required = false) MultipartFile file);


    /**
     * DELETE /api/users/{userId} - User 삭제
     */
    @Operation(summary = "User 삭제")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "User가 성공적으로 삭제됨"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "User를 찾을 수 없음",
            content = @Content(
                examples = @ExampleObject(value = "User with id {userId} not found")
            )
        )
    })
    ResponseEntity<Objects> delete(
        @Parameter(description = "삭제할 User ID") @PathVariable("userId") UUID userId);


    /**
     * PATCH /api/users/{userId} - User 정보 수정
     * Multipart 요청을 위한 문서화입니다.
     */
    @Operation(
        summary = "User 정보 수정 (프로필 이미지 포함)",
        description = "**Content-Type: multipart/form-data** 요청입니다."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "User 정보가 성공적으로 수정됨",
            content = @Content(
                schema = @Schema(implementation = Res_User.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "User를 찾을 수 없음",
            content = @Content(
                examples = @ExampleObject(value = "User with id {userId} not found")
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "같은 email 또는 username를 사용하는 User가 이미 존재함",
            content = @Content(
                examples = @ExampleObject(value = "user with email {newEmail} already exists")
            )
        )
    })
    ResponseEntity<Res_User> update(
        @Parameter(description = "수정할 User ID") @PathVariable("userId") UUID userId,
        @Parameter(description = "User 업데이트 정보 (JSON)") @RequestPart(value = "userUpdateRequest") Dto_UserUpdate dtoUser,
        @Parameter(description = "새로운 프로필 이미지 파일 (선택 사항)") @RequestPart(value = "profile", required = false) MultipartFile file);


    /**
     * PATCH /api/users/{userId}/userStatus - User 온라인 상태 업데이트
     */
    @Operation(summary = "User 온라인 상태 업데이트")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "User 온라인 상태가 성공적으로 업데이트됨",
            content = @Content(
                schema = @Schema(implementation = Res_UserStatus.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "해당 User의 UserStatus를 찾을 수 없음",
            content = @Content(
                examples = @ExampleObject(value = "UserStatus with userId {userId} not found")
            )
        )
    })
    ResponseEntity<Res_UserStatus> updateUserStatus(
        @Parameter(description = "상태를 변경할 User ID") @PathVariable("userId") UUID userId,
        @org.springframework.web.bind.annotation.RequestBody Dto_UserStatusUpdate userStatusUpdate);
}