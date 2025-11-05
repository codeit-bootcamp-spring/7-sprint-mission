package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.Binarycontent.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.request.UserDeleteRequest;

import com.sprint.mission.discodeit.dto.user.request.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.user.response.UserCreateResponse;
import com.sprint.mission.discodeit.dto.user.response.UserDto;
import com.sprint.mission.discodeit.dto.user.response.UserFindResponse;
import com.sprint.mission.discodeit.dto.user.response.UserUpdateResponse;
import com.sprint.mission.discodeit.dto.userStatus.request.UserStatustUpdateRequest;
import com.sprint.mission.discodeit.dto.userStatus.response.UserStatusResponse;
import com.sprint.mission.discodeit.entity.content.ContentsType;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

     private final UserService userService;
     private final UserStatusService userStatusService;

    // [등록]
    @PostMapping(value = "/create")
    public ResponseEntity<UserCreateResponse> create(
            @RequestPart("user") UserCreateRequest request,
            @RequestPart(value = "profile", required = false) MultipartFile profile
    ){

        Optional<BinaryContentCreateRequest> optionalProfile = Optional.empty();
        
      if (profile != null && !profile.isEmpty()) {
          try {
              optionalProfile = Optional.of(
                      new BinaryContentCreateRequest(
                              ContentsType.PROFILE_IMAGE,
                              profile.getBytes()
                      )
              );
          } catch (IOException e) {
              throw new RuntimeException("앙대");
          }
      }
        UserCreateResponse userCreateResponse = userService.create(request, optionalProfile);

        return  ResponseEntity.ok(userCreateResponse);
    }



// [수정]
    @PostMapping(value = "/update")
    public UserUpdateResponse update(
            @RequestBody UserUpdateRequest request,
            @RequestPart(value = "profile", required = false) MultipartFile profile
    ) throws IOException {

        Optional<BinaryContentCreateRequest> optionalProfile = Optional.empty();
        if (profile != null && !profile.isEmpty()) {
            optionalProfile = Optional.of(
                    new BinaryContentCreateRequest(
                            ContentsType.PROFILE_IMAGE,
                            profile.getBytes()
                    )
            );
        }

        return userService.update(request, optionalProfile);
    }

    // [삭제]
    @PostMapping("/delete")
    public void delete(@RequestBody UserDeleteRequest req) { userService.delete(req.userId()); }



    // [전체 조회]
    @GetMapping("/findAll")
    public ResponseEntity<List<UserDto>> findAll() {

       return ResponseEntity.ok(userService.findAll());
    }



    // [온라인 상태 업데이트]
    @PatchMapping("/userStatus")
    public UserStatusResponse updateStatus(@RequestBody UserStatustUpdateRequest req) {
        return userStatusService.update(req);
    }

}
