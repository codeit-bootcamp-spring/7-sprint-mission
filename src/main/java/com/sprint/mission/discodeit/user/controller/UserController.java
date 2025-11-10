package com.sprint.mission.discodeit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.application.dto.UserDetailInfo;
import com.sprint.mission.discodeit.application.service.UserManagementService;
import com.sprint.mission.discodeit.config.SessionConstants;
import com.sprint.mission.discodeit.content.binary.BinaryContentResponse;
import com.sprint.mission.discodeit.user.User;
import com.sprint.mission.discodeit.user.UserService;
import com.sprint.mission.discodeit.user.dto.*;
import com.sprint.mission.discodeit.user.state.UserStatusResponseDTO;
import com.sprint.mission.discodeit.user.state.UserStatusRequestDTO;
import com.sprint.mission.discodeit.user.state.UserStatusService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserManagementService userManagementService;
    private final UserService userService;
    private final UserStatusService userStatusService;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> register(
            @RequestPart(value = "userRequest") UserRequestDTO requestDTO,
            @RequestPart(value = "profileImage", required = false) MultipartFile multipartFile
    ) {
        UserResponseDTO responseDTO = userManagementService.createUserWithRelatedData(requestDTO, multipartFile);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteUser(
            @SessionAttribute(SessionConstants.AUTH_USER) AuthUserDTO authUser
    ){
        userManagementService.deleteUserWithRelatedData(authUser.authUserId());
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/updateProfile", method = RequestMethod.PUT)
    public ResponseEntity<UserResponseDTO> updateProfile(
            @SessionAttribute(SessionConstants.AUTH_USER) AuthUserDTO authUser,
            @RequestBody @Valid UserProfileUpdateDTO requestDTO) {

        UserResponseDTO responseDTO = userService.updateProfile(authUser.authUserId(), requestDTO);



        return ResponseEntity.ok(responseDTO);
    }

    @RequestMapping(value = "/changePassword", method = RequestMethod.PUT)
    public ResponseEntity<Void> changePassword(
            @SessionAttribute(SessionConstants.AUTH_USER) AuthUserDTO authUser,
            @RequestBody String newPassword) {
        userService.changePassword(authUser.authUserId(), newPassword);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/allusers", method = RequestMethod.GET)
    public ResponseEntity<List<UserDetailInfo>> getAllUsers() {
        List<UUID> ids = userService.findAll().stream()
                .map(User::getId).toList();
        List<UserDetailInfo> users = ids.stream()
                .map(userManagementService::getUserDetailInfo).toList();
        return ResponseEntity.ok(users);
    }

    @RequestMapping(value = "/status", method = RequestMethod.PUT)
    public ResponseEntity<?> updateUserStatus(
            @SessionAttribute(SessionConstants.AUTH_USER) AuthUserDTO authUser,
            @RequestBody UserStatusRequestDTO requestDTO) {
        UUID userId = authUser.authUserId();
        UserStatusResponseDTO responseDTO;
        switch (requestDTO.status()) {
            case ONLINE:
                 responseDTO = userStatusService.toOnline(userId);
                break;
            case OFFLINE:
                responseDTO = userStatusService.toOffline(userId);
                break;
            case AFK:
                responseDTO = userStatusService.toAway(userId);
                break;
            case DND:
                responseDTO = userStatusService.toDoNotDisturb(userId, requestDTO.message());
                break;
            default:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("확인 할 수 없는 상태값입니다.");
        }
        return ResponseEntity.ok(responseDTO);
    }

    @RequestMapping(value = "/changeusername", method = RequestMethod.PUT)
    public ResponseEntity<?> changeUserName(
            @SessionAttribute(SessionConstants.AUTH_USER) AuthUserDTO authUser,
            @RequestBody @Valid ChangeUserNameRequest request) {
        if(authUser.username().equals(request.username())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("기존 아이디와 동일한 아이디입니다.");
        }
        if(userService.existsByUsername(request.username())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 존재하는 아이디입니다.");
        }
        return ResponseEntity.ok(userService.changeUserName(authUser.authUserId(), request.username()));
    }

    @RequestMapping(value = "/details")
    public ResponseEntity<UserDetailInfo> getAllDetailInfo(
            @SessionAttribute(SessionConstants.AUTH_USER) AuthUserDTO authUser) {
        UserDetailInfo detailInfoDTO = userManagementService.getUserDetailInfo(authUser.authUserId());
        return ResponseEntity.ok(detailInfoDTO);
    }

    @RequestMapping(value = "/updateprofileimg", method = RequestMethod.PUT)
    public ResponseEntity<BinaryContentResponse> updateProfileImg(
            @SessionAttribute(SessionConstants.AUTH_USER) AuthUserDTO authUser,
            @RequestPart(value = "profileImage", required = false) MultipartFile multipartFile
            ){
        BinaryContentResponse responseDTO = userManagementService.updateProfileImg(authUser.authUserId(), multipartFile);
        return ResponseEntity.ok(responseDTO);
    }

    @RequestMapping(value = "/userdetails", method = RequestMethod.GET)
    public ResponseEntity<UserDetailInfo> getUserDetailInfo(
            @SessionAttribute(SessionConstants.AUTH_USER) AuthUserDTO authUser
            ){
        UserDetailInfo detailInfoDTO = userManagementService.getUserDetailInfo(authUser.authUserId());
        return ResponseEntity.ok(detailInfoDTO);
    }
}

