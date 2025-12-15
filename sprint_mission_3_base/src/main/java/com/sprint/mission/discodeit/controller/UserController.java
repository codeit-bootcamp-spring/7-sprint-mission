package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.service.UserService;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @RequestBody(
            content = @Content(
                    mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                    schema = @Schema(
                            description = "User 생성 요청(JSON + 프로필 파일)"
                    )
            )
    )
    public ResponseEntity<UserDto> create(
            @RequestPart("userCreateRequest")
            @Schema(
                    description = "JSON 입력: {\"username\":\"...\", \"email\":\"...\", \"password\":\"...\"}"
            )
            UserCreateRequest userCreateRequest,

            @RequestPart(name = "profile", required = false)
            @Schema(type = "string", format = "binary")
            MultipartFile profile
    ) {
        Optional<BinaryContentCreateRequest> profileReq =
                Optional.ofNullable(profile).flatMap(this::convert);

        UserDto dto = userService.create(userCreateRequest, profileReq);
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PatchMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDto> update(
            @PathVariable UUID userId,

            @RequestPart("userUpdateRequest")
            @Schema(description = "JSON 입력: {\"name\":\"새 이름\"}")
            UserUpdateRequest userUpdateRequest,

            @RequestPart(name = "profile", required = false)
            @Schema(type = "string", format = "binary")
            MultipartFile profile
    ) {
        Optional<BinaryContentCreateRequest> profileReq =
                Optional.ofNullable(profile).flatMap(this::convert);

        UserDto dto = userService.update(userId, userUpdateRequest, profileReq);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> find(@PathVariable UUID userId) {
        return ResponseEntity.ok(userService.find(userId));
    }

    @GetMapping
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(@PathVariable UUID userId) {
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }

    private Optional<BinaryContentCreateRequest> convert(MultipartFile file) {
        try {
            return Optional.of(
                    new BinaryContentCreateRequest(
                            file.getOriginalFilename(),
                            file.getContentType(),
                            file.getBytes()
                    )
            );
        } catch (Exception e) {
            throw new RuntimeException("파일 변환 중 오류", e);
        }
    }
}
//test