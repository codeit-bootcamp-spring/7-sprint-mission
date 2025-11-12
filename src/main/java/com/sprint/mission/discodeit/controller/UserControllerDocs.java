package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.user.UserPatchRequestDto;
import com.sprint.mission.discodeit.dto.request.user.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.request.userStatus.UserStatusPatchRequestDto;
import com.sprint.mission.discodeit.dto.response.UserCreateResponseDto;
import com.sprint.mission.discodeit.dto.response.UserDto;
import com.sprint.mission.discodeit.dto.response.UserUserStatusPatchResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
@Tag(name = "유저 제어 API(User Controller)",description = "유저를 조회,생성, 삭제, 업데이트를 관리하는 API 입니다.")
public interface UserControllerDocs {

    @Operation(summary = "유저 삭제(Delete User)", description = "유저를 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "유저 삭제 성공",
    content = @Content(mediaType = "String", examples = @ExampleObject(value = """
            유저 삭제 성공시, user-status도 동시에 삭제됩니다.
            """))
    )
    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
    void delete(@PathVariable UUID userId);

    @Operation(summary = "유저 조회(Read User)", description = "유저를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "유저 조회 성공",
    content = @Content( mediaType = "application/json",
    examples = @ExampleObject(value = """
            유저를 전부 조회합니다.채팅 화면 부를때 수시로
            유저 전체 조회를 호출합니다.
    """)
    )
    )
    @RequestMapping(value = "",method = RequestMethod.GET)
    ResponseEntity<List<UserDto>> readAll();

    @Operation(summary = "유저 생성(Create User)", description = "유저를 생성합니다.")
    @ApiResponse(responseCode = "201", description = "유저 생성 성공",
    content = @Content(mediaType = "application/json",
    schema = @Schema(implementation = UserCreateRequestDto.class),
    examples = @ExampleObject(value = """
            "username": "Ronaldo"
            "email": "genius5375@gmail.com"
            "password" : "1234"
    """)
    )
    )
    @RequestMapping(value = "", method = RequestMethod.POST,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<UserCreateResponseDto> createUser(
            @RequestPart("userCreateRequest") UserCreateRequestDto dto
            , @RequestPart(value = "profile", required = false) MultipartFile profile
    ) throws IOException;

    @Operation(summary = "유저 수정(Patch User)", description = "유저를 수정합니다.")
    @ApiResponse(responseCode = "200", description = "유저 수정 성공",
    content = @Content(mediaType = "application/json",
    schema = @Schema(implementation = UserUpdateRequest.class),
    examples = @ExampleObject(value = """
           "newUsername":"Messi"
           "newEmail": "loser@gmail.com"
           "newPassword":"4321"
    """)
    )
    )
    @RequestMapping(value = "/{userId}", method = RequestMethod.PATCH,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<UserCreateResponseDto> patchUser(@PathVariable UUID userId,
                                                    @RequestPart("userUpdateRequest") UserUpdateRequest dto,
                                                    @RequestPart(value = "profile", required = false) MultipartFile profile) throws IOException;
    @Operation(summary = "유저 상태 수정(Patch User Status)", description = "유저 상태를 수정합니다.")
    @ApiResponse(responseCode = "200", description = "유저 상태 수정 성공",
    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserUserStatusPatchResponseDto.class),
    examples = @ExampleObject(value = """
            "newLastActiveAt": Instant
            """)))
    @RequestMapping(value = "/{userId}/userStatus",method = RequestMethod.PATCH)
    ResponseEntity<UserUserStatusPatchResponseDto> patchUserStatus(@PathVariable UUID userId, @RequestBody UserStatusPatchRequestDto dto);
}
