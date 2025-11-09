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
@RequestMapping("/api/users")
public class UserController {

     private final UserService userService;


    // [등록]

    @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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

    @RequestMapping(value = "/update", method = RequestMethod.PATCH, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserUpdateResponse> update(
            @RequestPart("user") UserUpdateRequest request,
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

        UserUpdateResponse update = userService.update(request, optionalProfile);
        return   ResponseEntity.ok(update);
    }

    // [삭제]

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public void delete(@RequestBody UserDeleteRequest req) { userService.delete(req.userId()); }



    // [전체 조회]

    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public ResponseEntity<List<UserDto>> findAll() {

       return ResponseEntity.ok(userService.findAll());
    }




}
