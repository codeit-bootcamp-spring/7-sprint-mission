package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.user.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.request.UserDeleteRequest;

import com.sprint.mission.discodeit.dto.user.request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.user.response.UserCreateResponse;
import com.sprint.mission.discodeit.dto.user.response.UserFindResponse;
import com.sprint.mission.discodeit.dto.user.response.UserUpdateResponse;
import com.sprint.mission.discodeit.dto.userStatus.request.UserStatustUpdateRequest;
import com.sprint.mission.discodeit.dto.userStatus.response.UserStatusResponse;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

     private final UserService userService;
     private final UserStatusService userStatusService;

    // [등록]
    @PostMapping("/create")
    public UserCreateResponse create(@RequestBody UserCreateRequest req) { return userService.create(req); }

    // [수정]
    @PutMapping("/update")
    public UserUpdateResponse update(@RequestBody UserUpdateRequest req) {
        return userService.update(req);
    }

    // [삭제]
    @PostMapping("/delete")
    public void delete(@RequestBody UserDeleteRequest req) { userService.delete(req.userId()); }

    // [전체 조회]
    @PostMapping("/findAll")
    public List<UserFindResponse> findAll() { return userService.findAll(); }

    // [온라인 상태 업데이트]
    @PatchMapping("/userStatus")
    @RequestMapping(value="/userStatus", method = RequestMethod.PATCH)
    public UserStatusResponse updateStatus(@RequestBody UserStatustUpdateRequest req) {
        return userStatusService.update(req);
    }

}
