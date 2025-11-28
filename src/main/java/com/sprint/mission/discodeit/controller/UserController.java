package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.UserCreateRequest;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.mapper.dto.UserStatusDto;
import com.sprint.mission.discodeit.service.basic.BinaryContentService;
import com.sprint.mission.discodeit.swaggerDocs.UserDoc;
import com.sprint.mission.discodeit.dto.Dto_UserStatusUpdate;
import com.sprint.mission.discodeit.mapper.dto.UserUpdateRequest;
import com.sprint.mission.discodeit.mapper.dto.UserDto;
import com.sprint.mission.discodeit.service.basic.UserService;
import com.sprint.mission.discodeit.service.basic.UserStatusService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController //👍 @controller + @responsebody
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController implements UserDoc {
    private final UserService userService;
    private final BinaryContentService binaryContentService;
    private final UserStatusService userStatusService;
    private final UserMapper userMapper;

    //!! @Valid 검증 == dependencies 'spring-boot-starter-validation'

    @GetMapping
    public ResponseEntity<List<UserDto>> findAll() {
    //💎♨️ 전체 User 목록 조회
    List<UserDto> userDtoList = userService.findAll();

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(userDtoList);
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<UserDto> create(
        @Valid @RequestPart("userCreateRequest") UserCreateRequest dtoUser, //?? ❌❌❌
        @RequestPart(value = "profile", required = false) MultipartFile file) {

        //💎User 등록
        UserDto resUser = userService.create(dtoUser, Optional.ofNullable(file));

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(resUser);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Objects> delete(
        @PathVariable("userId") UUID userId) {
        //💎User 삭제
        userService.delete(userId);

        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build();
    }

    @PatchMapping(value = "/{userId}", consumes = "multipart/form-data")
    public ResponseEntity<UserDto> update(
        @PathVariable("userId") UUID userId,
        @Valid @RequestPart(value = "userUpdateRequest") UserUpdateRequest dtoUser,
        @RequestPart(value = "profile", required = false) MultipartFile file) {
        //💎User 정보 수정
        UserDto resUser = userService.update(userId, dtoUser, Optional.ofNullable(file));

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(resUser);
    }

    @PatchMapping("/{userId}/userStatus")
    public ResponseEntity<UserStatusDto> updateUserStatus(
        @PathVariable("userId") UUID userId,
        @Valid @RequestBody Dto_UserStatusUpdate userStatusUpdate) {

        //💎User 온라인 상태 업데이트
        UserStatusDto userStatusDto = userStatusService.updateUserStatus(userId, userStatusUpdate.newLastActiveAt());

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(userStatusDto);
    }
}
