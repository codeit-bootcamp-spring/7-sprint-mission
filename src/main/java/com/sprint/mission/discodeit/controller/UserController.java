package com.sprint.mission.discodeit.controller;

import static com.sprint.mission.discodeit.common.Util.parsingMultipartFile;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.PATCH;

import com.sprint.mission.discodeit.swaggerDocs.UserDoc;
import com.sprint.mission.discodeit.entity.dto.Dto_BinaryContent;
import com.sprint.mission.discodeit.entity.dto.Dto_UserCreate;
import com.sprint.mission.discodeit.entity.dto.Dto_UserStatusUpdate;
import com.sprint.mission.discodeit.entity.dto.Dto_UserUpdate;
import com.sprint.mission.discodeit.entity.dto.Res_User;
import com.sprint.mission.discodeit.entity.dto.Res_UserStatus;
import com.sprint.mission.discodeit.entity.dto.UserDto;
import com.sprint.mission.discodeit.service.basic.UserService;
import com.sprint.mission.discodeit.service.basic.UserStatusService;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@ResponseBody
@RequestMapping("/api/users")
public class UserController implements UserDoc {
    private final UserService userService;
    private final UserStatusService userStatusService;

    //!! @Valid 검증 == dependencies 'spring-boot-starter-validation'

    @GetMapping
    public ResponseEntity<List<UserDto>> findAll() {
      //💎 전체 User 목록 조회
      List<UserDto> userDtoList = userService.findAll();
      return ResponseEntity
          .status(HttpStatus.OK)
          .body(userDtoList);
    }

    @PostMapping
//    @RequestMapping(method = POST)
    public ResponseEntity<Res_User> create(
        @RequestPart("userCreateRequest") Dto_UserCreate dtoUser,
        @RequestPart(value = "profile", required = false) MultipartFile file) {
        //💎User 등록
        Dto_BinaryContent dtoFile = parsingMultipartFile(file);
        Res_User resUser = userService.create(dtoUser, Optional.ofNullable(dtoFile));

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(resUser);
    }


    @RequestMapping(value = "/{userId}", method = DELETE)
    public ResponseEntity<Objects> delete(
        @PathVariable("userId") UUID userId) {
        //💎User 삭제
        userService.delete(userId);

        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .build();
    }

    @RequestMapping(value = "/{userId}", method = PATCH)
    public ResponseEntity<Res_User> update(
        @PathVariable("userId") UUID userId,
        @RequestPart(value = "userUpdateRequest") Dto_UserUpdate dtoUser,
        @RequestPart(value = "profile", required = false) MultipartFile file) {
        //💎User 정보 수정
        Dto_BinaryContent dtoFile = parsingMultipartFile(file);

        Res_User resUser = userService.update(userId, dtoUser, Optional.ofNullable(dtoFile));

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(resUser);
    }

    @RequestMapping(value = "/{userId}/userStatus", method = PATCH)
    public ResponseEntity<Res_UserStatus> updateUserStatus(
        @PathVariable("userId") UUID userId,
        @RequestBody Dto_UserStatusUpdate userStatusUpdate) {
        //💎User 온라인 상태 업데이트
      Res_UserStatus resUserStatus = userStatusService.updateUserStatus(userId, userStatusUpdate.newLastActiveAt());
      return ResponseEntity
            .status(HttpStatus.OK)
            .body(resUserStatus);
    }
}
