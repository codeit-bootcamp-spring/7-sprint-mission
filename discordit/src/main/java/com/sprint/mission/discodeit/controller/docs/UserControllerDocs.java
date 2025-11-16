package com.sprint.mission.discodeit.controller.docs;

import com.sprint.mission.discodeit.dto.user.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.user.response.SimpleUserResponse;
import com.sprint.mission.discodeit.dto.user.response.UserResponseV2;
import com.sprint.mission.discodeit.dto.userStatus.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.userStatus.response.UserStatusResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Tag(name = "사용자 관리", description = "사용자 생성, 조회, 수정, 삭제 및 상태 관리 API입니다.")
public interface UserControllerDocs {
    @Operation(
            summary = "모든 사용자 조회 (간단)",
            description = "시스템의 모든 사용자를 간단한 정보로 조회합니다"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    ResponseEntity<List<SimpleUserResponse>> getAll();

    @Operation(
            summary = "모든 사용자 조회 (상세)",
            description = "시스템의 모든 사용자를 상세 정보로 조회합니다"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    ResponseEntity<List<UserResponseV2>> getDetailedAll();

    @Operation(
            summary = "사용자 회원가입",
            description = "새로운 사용자를 생성합니다"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "회원가입 성공")
    })
    ResponseEntity<UserResponseV2> signIn(@Valid @RequestBody UserCreateRequest request);

    @Operation(
            summary = "사용자 삭제",
            description = "사용자를 삭제합니다"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "삭제 성공")
    })
    ResponseEntity<Void> delete(@RequestParam UUID userId);

    @Operation(
            summary = "사용자 정보 수정",
            description = "사용자 정보를 수정합니다. 프로필 이미지는 선택사항입니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공")
    })
    ResponseEntity<UserResponseV2> update(@PathVariable UUID userId, @Valid @RequestPart UserUpdateRequest userUpdateRequest, @RequestPart(required = false) MultipartFile profile);

    @Operation(
            summary = "사용자 상태 수정",
            description = "사용자의 활동 상태를 수정합니다"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "수정 성공")
    })
    ResponseEntity<Instant> updateUserStatus(@PathVariable UUID userId, @RequestBody UserStatusUpdateRequest request);

    @Operation(
            summary = "모든 사용자 상태 조회",
            description = "시스템의 모든 사용자 상태를 조회합니다"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    ResponseEntity<List<UserStatusResponse>> getAllUserStatus();

    @Operation(
            summary = "특정 사용자 조회",
            description = "ID로 특정 사용자를 조회합니다"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    ResponseEntity<UserResponseV2> get(@RequestParam UUID id);
}