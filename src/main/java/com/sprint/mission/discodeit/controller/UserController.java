package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.common.Util;
import com.sprint.mission.discodeit.entity.dto.*;
import com.sprint.mission.discodeit.service.basic.UserService;
import com.sprint.mission.discodeit.service.basic.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    private final UserStatusService userStatusService;
//    사용자 관리
//    [ ] 사용자를 등록할 수 있다.
//    [ ] 사용자 정보를 수정할 수 있다.
//    [ ] 사용자를 삭제할 수 있다.
//    [ ] 모든 사용자를 조회할 수 있다.
//    [ ] 사용자의 온라인 상태를 업데이트할 수 있다.

    //!! @Valid 검증 == dependencies 'spring-boot-starter-validation'
    @RequestMapping(value = "/create", method = POST)
    public Res_User create(@RequestPart("dtouser") Dto_User dtoUser,
                           @RequestPart("file") MultipartFile file) {

        Dto_BinaryContent dtoFile = null;
        try {
            dtoFile = Dto_BinaryContent.from(
                                                file.getOriginalFilename(),
                                                file.getContentType(),
                                                file.getBytes(),
                                                file.getSize());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Util.okMessage("file.getOriginalFilename() = [" + file.getOriginalFilename() + "]");
        return userService.create(dtoUser, Optional.ofNullable(dtoFile));
    }

//    //!! @Valid 검증 == dependencies 'spring-boot-starter-validation'
//    @RequestMapping(value = "/create", method = POST)
//    public Res_User create(@RequestBody Dto_UserWithContent dto) {
//        return userService.create(dto.dtoUser(), dto.binaryContent());
//    }

    @RequestMapping(value = "/update", method = PUT)
    public Res_User update(@RequestBody Dto_UserWithIDAndContent dto) {
        return userService.update(dto.userId(), dto.dtoUser(), dto.binaryContent());
    }

    @RequestMapping(value = "/delete/{id}", method = DELETE)
    public void delete(@PathVariable("id") UUID userID) {
        userService.delete(userID);
    }

    @RequestMapping(value = "/findAll", method = GET)
    public ResponseEntity<List<UserDto>> findAll() {
        //[ ]  사용자 목록 조회
        //url - /api/user/findAll
        //요청 - 파라미터, 바디 없음
        //응답 - ResponseEntity<List<UserDto>>
        return ResponseEntity.ok(userService.findAll());
    }

    @RequestMapping(value = "/updateOnlineStatus/{id}", method = PUT)
    public void updateOnlineStatus(@PathVariable("id") UUID userId) {
        userStatusService.updateByUserID(userId);
    }
}
