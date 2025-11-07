package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.common.response.CustomApiResponse;
import com.sprint.mission.discodeit.dto.binarycontent.request.BinaryContentCreateReq;
import com.sprint.mission.discodeit.dto.user.request.UserCreateReq;
import com.sprint.mission.discodeit.dto.user.request.UserInfoReq;
import com.sprint.mission.discodeit.dto.user.request.UserUpdateReq;
import com.sprint.mission.discodeit.dto.user.response.UserDetailInfoRes;
import com.sprint.mission.discodeit.dto.user.response.UserSimpleInfoRes;
import com.sprint.mission.discodeit.dto.userStatus.response.UserStatusSimpleViewRes;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.facade.user.*;
import com.sprint.mission.discodeit.facade.userstatus.UserStatusUpdateFacade;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserCreationFacade userCreationFacade;
    private final UserOverviewFacade userOverviewFacade;
    private final UserDetailViewFacade userDetailViewFacade;
    private final UserUpdateFacade userUpdateFacade;
    private final UserDeletionFacade userDeletionFacade;
    private final UserStatusUpdateFacade userStatusUpdateFacade;
    private final UserService userService;

    //사용자 목록 조회
    @RequestMapping(method = RequestMethod.GET, value="/list")
    public ResponseEntity<CustomApiResponse<List<UserSimpleInfoRes>>> getAllUsers(){
        return ResponseEntity.ok(CustomApiResponse.success(userOverviewFacade.findAll()));
    }

    //사용자 단일 조회
    @RequestMapping(method = RequestMethod.GET, value = "/{userId}")
    public ResponseEntity<CustomApiResponse<UserDetailInfoRes>> getUserByNickname(@PathVariable UUID userId){
        return ResponseEntity.ok(CustomApiResponse.success(userDetailViewFacade.findById(userId)));
    }

    //회원 등록
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<CustomApiResponse<Void>> createUser(
            @Valid @RequestPart("userInfoReq") UserInfoReq userInfoReq,
            @RequestPart(value = "profile", required = false) MultipartFile profileFile){

        log.info("회원 가입 요청: nickname={}, email={}", userInfoReq.nickname(), userInfoReq.email());

        BinaryContentCreateReq binaryContentCreateReq = BinaryContentCreateReq.from(profileFile);
        User user = userCreationFacade.createUser(UserCreateReq.from(userInfoReq, binaryContentCreateReq));
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(user.getId())
                .toUri();
        return ResponseEntity.created(location).body(CustomApiResponse.success());
    }

    // 회원 수정
    @RequestMapping(method = RequestMethod.PUT, value="/{userId}")
    public ResponseEntity<CustomApiResponse<Void>> updateUser(
            @PathVariable UUID userId,
            @Valid @RequestPart("userInfoReq") UserInfoReq userInfoReq,
            @RequestPart(value = "profile", required = false) MultipartFile profileFile){

        log.info("회원 정보 수정 요청: nickname={}, email={}", userInfoReq.nickname(), userInfoReq.email());
        
        BinaryContentCreateReq binaryContentCreateReq = BinaryContentCreateReq.from(profileFile);
        UserUpdateReq req = UserUpdateReq.from(userInfoReq, binaryContentCreateReq);
        userUpdateFacade.updateUser(userId, req);
        return ResponseEntity.ok(CustomApiResponse.success());
    }

    // 회원 삭제
    @RequestMapping(method = RequestMethod.DELETE, value = "/{userId}")
    public ResponseEntity<CustomApiResponse<Void>> deleteUser(@PathVariable UUID userId){
        User user = userService.findById(userId);
        log.info("유저 탈퇴: user.nickname = {}, user.email = {} ", user.getNickname(), user.getEmail());

        userDeletionFacade.deleteUser(userId);
        return ResponseEntity.ok(CustomApiResponse.success());
    }

    // 유저 온라인 상태 업그레이드
    @RequestMapping(method = RequestMethod.PATCH, value = "/{userId}/status")
    public ResponseEntity<CustomApiResponse<UserStatusSimpleViewRes>> updateUserStatus(@PathVariable UUID userId){
        return ResponseEntity.ok(CustomApiResponse.success(userStatusUpdateFacade.update(userId)));
    }
}
