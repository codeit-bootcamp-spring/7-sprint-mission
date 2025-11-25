package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Tag(name = "User", description = "User API")
public interface UserApi {

    /* -----------------------------------
       User 생성
       ----------------------------------- */
    @Operation(summary = "User 등록")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201", description = "User가 성공적으로 생성됨",
                    content = @Content(schema = @Schema(implementation = UserDto.class))
            ),
            @ApiResponse(
                    responseCode = "400", description = "같은 email 또는 username을 사용하는 User가 이미 존재함",
                    content = @Content(examples = @ExampleObject(value = "User with email {email} already exists"))
            ),
    })
    ResponseEntity<UserDto> create(
            @Parameter(description = "User 생성 정보") UserCreateRequest userCreateRequest,
            @Parameter(
                    description = "User 프로필 이미지",
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
            ) MultipartFile profile
    );

    /* -----------------------------------
       User 정보 수정
       ----------------------------------- */
    @Operation(summary = "User 정보 수정")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "User 정보 수정 성공",
                    content = @Content(schema = @Schema(implementation = UserDto.class))
            ),
            @ApiResponse(
                    responseCode = "404", description = "User를 찾을 수 없음",
                    content = @Content(examples = @ExampleObject("User with id {userId} not found"))
            ),
            @ApiResponse(
                    responseCode = "400", description = "같은 email 또는 username을 사용하는 User가 이미 존재함",
                    content = @Content(examples = @ExampleObject("user with email {newEmail} already exists"))
            )
    })
    ResponseEntity<UserDto> update(
            @Parameter(description = "수정할 User ID") UUID userId,
            @Parameter(description = "수정할 User 정보") UserUpdateRequest userUpdateRequest,
            @Parameter(description = "수정할 User 프로필 이미지") MultipartFile profile
    );

    /* -----------------------------------
       User 삭제
       ----------------------------------- */
    @Operation(summary = "User 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User가 성공적으로 삭제됨"),
            @ApiResponse(
                    responseCode = "404", description = "User를 찾을 수 없음",
                    content = @Content(examples = @ExampleObject("User with id {id} not found"))
            )
    })
    ResponseEntity<Void> delete(
            @Parameter(description = "삭제할 User ID") UUID userId
    );

    /* -----------------------------------
       User 전체 조회
       ----------------------------------- */
    @Operation(summary = "전체 User 목록 조회")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "User 목록 조회 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserDto.class)))
            )
    })
    ResponseEntity<List<UserDto>> findAll();


    /* -----------------------------------------------
       User 온라인 상태 업데이트 (JPA 버전 / Request 제거됨)
       ----------------------------------------------- */
    @Operation(summary = "User 온라인 상태 갱신 (마지막 접속 시간 업데이트)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User 온라인 상태 업데이트 성공"),
            @ApiResponse(
                    responseCode = "404", description = "User를 찾을 수 없음",
                    content = @Content(examples = @ExampleObject("User with id {userId} not found"))
            )
    })
    ResponseEntity<Void> updateUserStatusByUserId(
            @Parameter(description = "업데이트할 User ID") UUID userId
    );
}
