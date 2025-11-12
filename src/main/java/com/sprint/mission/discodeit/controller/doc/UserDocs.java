package com.sprint.mission.discodeit.controller.doc;

import com.sprint.mission.discodeit.dto.user.request.CreateUserDto;
import com.sprint.mission.discodeit.dto.user.request.UpdateUserDto;
import com.sprint.mission.discodeit.dto.user.response.CreateUserResponseDto;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.dto.userStatus.request.UpdateUserStatusDto;
import com.sprint.mission.discodeit.dto.userStatus.response.UserStatusResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "User", description = "User API")
public interface UserDocs {

  @Operation(summary = "User 등록")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201",
          description = "User가 성공적으로 생성됨",
          content = @Content(mediaType = "*/*", schema = @Schema(implementation = CreateUserResponseDto.class))),
      @ApiResponse(responseCode = "400",
          description = "같은 email 또는 username를 사용하는 User가 이미 존재함",
          content = @Content(mediaType = "*/*", examples = @ExampleObject(value = "User with email {email} already exists"))
      ),
  })
  ResponseEntity<CreateUserResponseDto> createUser(
      @Parameter(description = "User 생성 정보", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
      @Valid @RequestPart(value = "userCreateRequest") CreateUserDto userDto,
      @Parameter(description = "User 프로필 이미지", content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
      @RequestPart(value = "profile", required = false) MultipartFile profile) throws IOException;

  @Operation(summary = "전체 User 목록 조회")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          description = "User 목록 조회 성공",
          content = @Content(
              mediaType = "*/*",
              array = @ArraySchema(schema = @Schema(implementation = UserResponseDto.class))))
  })
  ResponseEntity<List<UserResponseDto>> getAllUser();

  @Operation(summary = "User 삭제")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204",
          description = "User가 성공적으로 삭제됨"
      ),
      @ApiResponse(
          responseCode = "404",
          description = "User를 찾을 수 없음",
          content = @Content(examples = @ExampleObject(value = "User with id {id} not found"))
      )
  })
  ResponseEntity<Void> deleteUser(
      @Parameter(description = "삭제할 User ID") @PathVariable UUID userId);

  @Operation(summary = "User 정보 수정")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "User 정보가 성공적으로 수정됨",
          content = @Content(mediaType = "*/*", schema = @Schema(implementation = CreateUserResponseDto.class))
      ),
      @ApiResponse(
          responseCode = "404", description = "User를 찾을 수 없음",
          content = @Content(mediaType = "*/*", examples = @ExampleObject("User with id {userId} not found"))
      ),
      @ApiResponse(
          responseCode = "400", description = "같은 email 또는 username를 사용하는 User가 이미 존재함",
          content = @Content(mediaType = "*/*", examples = @ExampleObject("user with email {newEmail} already exists"))
      )
  })
  ResponseEntity<CreateUserResponseDto> updateUser(
      @Parameter(description = "수정할 User ID") @PathVariable UUID userId,
      @Parameter(description = "수정할 User 정보") @RequestPart UpdateUserDto updateUserDto,
      @Parameter(description = "수정할 User 프로필 이미지") @RequestPart(value = "profile", required = false) MultipartFile profile)
      throws IOException;

  @Operation(summary = "User 온라인 상태 업데이트")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "User 온라인 상태가 성공적으로 업데이트됨",
          content = @Content(mediaType = "*/*", schema = @Schema(implementation = UserStatusResponseDto.class))
      ),
      @ApiResponse(
          responseCode = "404", description = "해당 User의 UserStatus를 찾을 수 없음",
          content = @Content(mediaType = "*/*", examples = @ExampleObject("UserStatus with userId {userId} not found"))
      ),
  })
  ResponseEntity<UserStatusResponseDto> updateUserState(
      @Parameter(description = "상태를 변경할 User ID") @PathVariable UUID userId,
      @Parameter(description = "변경할 User 온라인 상태 정보") @RequestBody UpdateUserStatusDto updateUserDto);
}
