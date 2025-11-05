package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.dto.Dto_UserWithContent;
import com.sprint.mission.discodeit.entity.dto.Dto_UserWithIDAndContent;
import com.sprint.mission.discodeit.entity.dto.Res_IsOnlineUser;
import com.sprint.mission.discodeit.entity.dto.Res_User;
import com.sprint.mission.discodeit.service.basic.UserService;
import com.sprint.mission.discodeit.service.basic.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
// http://localhost:8080/user-list.html
public class UserController  extends BaseController {
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
    public Res_User create(@RequestBody Dto_UserWithContent dto) {
        return userService.create(dto.dtoUser(), dto.binaryContent());
    }

    @RequestMapping(value = "/update", method = POST)
    public Res_User update(@RequestBody Dto_UserWithIDAndContent dto) {
        return userService.update(dto.userId(), dto.dtoUser(), dto.binaryContent());
    }

    @RequestMapping(value = "/delete/{id}", method = DELETE)
    public void delete(@PathVariable("id") UUID userID) {
        userService.delete(userID);
    }

    @RequestMapping(value = "/findAll", method = POST)
    public List<Res_IsOnlineUser> findAll() {
        return userService.findAll();
    }

    @RequestMapping(value = "/updateOnlineStatus/{id}", method = POST)
    public void updateOnlineStatus(@PathVariable("id") UUID userId) {
        userStatusService.updateByUserID(userId);
    }
}
