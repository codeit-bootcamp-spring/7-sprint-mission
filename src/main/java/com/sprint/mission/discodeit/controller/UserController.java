package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.user.request.UserCreateReq;
import com.sprint.mission.discodeit.dto.user.request.UserUpdateReq;
import com.sprint.mission.discodeit.dto.user.response.UserDetailInfoRes;
import com.sprint.mission.discodeit.dto.user.response.UserSimpleInfoRes;
import com.sprint.mission.discodeit.dto.userStatus.response.UserStatusSimpleViewRes;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.facade.user.*;
import com.sprint.mission.discodeit.facade.userstatus.UserStatusUpdateFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserCreationFacade userCreationFacade;
    private final UserOverviewFacade userOverviewFacade;
    private final UserDetailViewFacade userDetailViewFacade;
    private final UserUpdateFacade userUpdateFacade;
    private final UserDeletionFacade userDeletionFacade;
    private final UserStatusUpdateFacade userStatusUpdateFacade;

    //사용자 목록 조회
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<UserSimpleInfoRes>> getAllUsers(){
        return ResponseEntity.ok(userOverviewFacade.findAll());
    }

    //사용자 단일 조회
    @RequestMapping(value = "/{userId}",method = RequestMethod.GET)
    public ResponseEntity<UserDetailInfoRes> getUserByNickname(@PathVariable UUID userId){
        return ResponseEntity.ok(userDetailViewFacade.findById(userId));
    }

    //회원 등록
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Void> createUser(@Valid @RequestBody UserCreateReq req){
        User user = userCreationFacade.createUser(req);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(user.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    // 회원 수정
    @RequestMapping(value="/{userId}", method = RequestMethod.PUT)
    public ResponseEntity<Void> updateUser(@PathVariable UUID userId, @Valid @RequestBody UserUpdateReq req){
        userUpdateFacade.updateUser(userId, req);
        return ResponseEntity.noContent().build();
    }

    // 회원 삭제
    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId){
        userDeletionFacade.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    // 유저 온라인 상태 업그레이드
    @RequestMapping(value = "/{userId}/status", method = RequestMethod.PATCH)
    public ResponseEntity<UserStatusSimpleViewRes> updateUserStatus(@PathVariable UUID userId){
        return ResponseEntity.ok(userStatusUpdateFacade.update(userId));
    }
}
