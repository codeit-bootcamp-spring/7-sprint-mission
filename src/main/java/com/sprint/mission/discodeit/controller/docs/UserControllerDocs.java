package com.sprint.mission.discodeit.controller.docs;

import com.sprint.mission.discodeit.dto.request.CreateUserRequestDto;
import com.sprint.mission.discodeit.dto.response.UserResponseDto;
import com.sprint.mission.discodeit.dto.update.UpdateUserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "User", description = "User API")
public interface UserControllerDocs {

  @Operation(summary = "User 등록")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201",
          description = "User가 성공적으로 생성됨",
          content = @Content(
              schema = @Schema(implementation = User.class)
          )
      ),
      @ApiResponse(
          responseCode = "400",
          description = "같은 email 또는 username을 사용하는 User가 이미 존재함",
          content = @Content(
              examples = @ExampleObject(
                  value = "User With email {email} already exists"
              )
          )
      )
  })
  ResponseEntity<User> createUser(
      @Parameter(description = "User 정보 생성")
      @Valid @RequestPart("user") CreateUserRequestDto UserCreateRequestDto,
      @Parameter(description = "User 프로필 이미지")
      @RequestPart(value = "profile", required = false) MultipartFile profile) throws IOException;

  @Operation(summary = "전체 User 목록 조회")
  @ApiResponse(
      responseCode = "200",
      description = "User 목록 조회 성공",
      content = @Content(
          schema = @Schema(implementation = UserResponseDto.class)
      )
  )
  ResponseEntity<List<UserResponseDto>> findAll();


  @Operation(summary = "User 정보 수정")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "User 정보가 성공적으로 수정됨",
          content = @Content(
              schema = @Schema(implementation = User.class)
          )
      ),
      @ApiResponse(
          responseCode = "400",
          description = "같은 email 또는 username를 사용하는 User가 이미 존재함",
          content = @Content(
              examples = @ExampleObject(
                  value = "User With email {email} already exists"
              )
          )
      ),
      @ApiResponse(
          responseCode = "404",
          description = "User를 찾을 수 없음",
          content = @Content(
              examples = @ExampleObject(
                  value = "User with id {id} not found"
              )
          )
      )
  })
  ResponseEntity<User> updateUser(
      @Parameter(description = "수정 할 User ID")
      @Valid @PathVariable UUID userId,
      @Parameter(description = "수정 할 User 정보")
      @RequestPart("userUpdateRequest") UpdateUserDto userUpdateRequest,
      @Parameter(description = "수정 할 User 프로필 이미지")
      @RequestPart(value = "profile", required = false) MultipartFile profile) throws IOException;


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
              examples = @ExampleObject(
                  value = "User with id {id} not found"
              )
          )
      )
  })
  ResponseEntity<Void> deleteUser(
      @Parameter(description = "삭제할 User ID")
      @PathVariable UUID userId);


  @Operation(summary = "User 온라인 상태 업데이트")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "User 온라인 상태가 성공적으로 업데이트됨",
          content = @Content(
              schema = @Schema(implementation = UserStatus.class)
          )
      ),
      @ApiResponse(
          responseCode = "404",
          description = "해당 User의 UserStatus를 찾을 수 없음",
          content = @Content(
              examples = @ExampleObject(
                  value = "UserStatus for user with id {userId} not found"
              )
          )
      )
  })
  ResponseEntity<UserStatus> updateUserStatusByUserId(
      @Parameter(description = "상태를 변경할 User ID")
      @PathVariable UUID userId);


}

