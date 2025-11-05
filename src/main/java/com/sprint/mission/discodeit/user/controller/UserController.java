package com.sprint.mission.discodeit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.application.dto.UserDetailInfoDTO;
import com.sprint.mission.discodeit.application.service.UserManagementService;
import com.sprint.mission.discodeit.user.UserService;
import com.sprint.mission.discodeit.user.dto.*;
import com.sprint.mission.discodeit.user.state.UserStatusResponseDTO;
import com.sprint.mission.discodeit.user.state.UserStatusRequestDTO;
import com.sprint.mission.discodeit.user.state.UserStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserManagementService userManagementService;
    private final UserService userService;
    private final UserStatusService userStatusService;
    private final ObjectMapper objectMapper;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> register(
            @RequestBody @Valid UserRequestDTO requestDTO,
            @RequestBody(required = false) MultipartFile multipartFile
    ) {


        UserResponseDTO responseDTO = userManagementService.createUserWithRelatedData(requestDTO, multipartFile);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteUser(
            @RequestBody AuthUserDTO authUser
    ){
        userManagementService.deleteUserWithRelatedData(authUser.authUserId());
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/updateProfile", method = RequestMethod.PUT)
    public ResponseEntity<UserResponseDTO> updateProfile(
            AuthUserDTO authUser,
            @RequestBody @Valid UserProfileUpdateDTO requestDTO) {

        UserResponseDTO responseDTO = userService.updateProfile(authUser.authUserId(), requestDTO);



        return ResponseEntity.ok(responseDTO);
    }

    @RequestMapping(value = "/changePassword", method = RequestMethod.PUT)
    public ResponseEntity<Void> changePassword(
            AuthUserDTO authUser,
            @RequestBody String newPassword) {
        userService.changePassword(authUser.authUserId(), newPassword);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.findAllNonDel().stream()
                .map(UserResponseDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(users);
    }

    @RequestMapping(value = "/status", method = RequestMethod.PUT)
    public ResponseEntity<?> updateUserStatus(
            AuthUserDTO authUser,
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
            AuthUserDTO authUser,
            @RequestBody @Valid ChangeUserNameRequest request) {
        if(authUser.username().equals(request.username())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("기존 아이디와 동일한 아이디입니다.");
        }
        if(userService.existsByUsername(request.username())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 존재하는 아이디입니다.");
        }
        return ResponseEntity.ok(userService.changeUserName(authUser.authUserId(), request.username()));
    }

    @RequestMapping(value = "/detail")
    public ResponseEntity<UserDetailInfoDTO> getUserDetailInfo(
            AuthUserDTO authUser) {
        UserDetailInfoDTO detailInfoDTO = userManagementService.getUserDetailInfo(authUser.authUserId());
        return ResponseEntity.ok(detailInfoDTO);
    }
}

