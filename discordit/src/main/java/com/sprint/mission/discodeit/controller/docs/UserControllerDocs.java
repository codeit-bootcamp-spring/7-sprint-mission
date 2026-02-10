package com.sprint.mission.discodeit.controller.docs;

import com.sprint.mission.discodeit.dto.entity.user.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.entity.user.request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.entity.user.response.UserResponse;
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
    ResponseEntity<List<UserResponse>> getAll();

    @Operation(
            summary = "모든 사용자 조회 (상세)",
            description = "시스템의 모든 사용자를 상세 정보로 조회합니다"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    ResponseEntity<List<UserResponse>> getDetailedAll();

    @Operation(
            summary = "사용자 회원가입",
            description = "새로운 사용자를 생성합니다"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "회원가입 성공")
    })
    ResponseEntity<UserResponse> signIn(@Valid @RequestBody UserCreateRequest request);

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
    ResponseEntity<UserResponse> update(@PathVariable UUID userId, @Valid @RequestPart(name = "userUpdateRequest") UserUpdateRequest userUpdateRequest, @RequestPart(name = "profile", required = false) MultipartFile profile);
}