package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.CreateBinaryContentRequestDto;
import com.sprint.mission.discodeit.dto.request.CreateUserCommand;
import com.sprint.mission.discodeit.dto.request.CreateUserRequestDto;
import com.sprint.mission.discodeit.dto.response.UserResponseDto;
import com.sprint.mission.discodeit.dto.response.UserStatusResponseDto;
import com.sprint.mission.discodeit.dto.update.UpdateUserCommand;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserStatusService userStatusService;


    //PathVariable -> /api/user/{id} - 특정 자원을 식별해야할 때 사용
    //RequestParam -> /api/user?name=value - 조건(필터링, 정렬, 검색), 부가적인 데이터을 전달해야할 때(페이지 번호)
    //PATCH-> 수정에 지향한다. PUT은 자원을 교체, PATCH 자원에 일부분만 변경. 규칙상 더 효율적이고 안전함

    //사용자를 등록할 수 있다.
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<UserResponseDto> createUser(
            //RequestPart 파트별로 분할해서 전달
            @Valid @RequestPart("user") CreateUserRequestDto userDto,
            @RequestPart(value = "data", required = false) MultipartFile data) throws IOException {

        CreateBinaryContentRequestDto content = null;
        if(data != null){
            content = new CreateBinaryContentRequestDto(
                    data.getBytes(),
                    data.getOriginalFilename(),
                    data.getContentType()
            );
        }

        CreateUserCommand from = CreateUserCommand.from(userDto, content);
        UserResponseDto user = userService.createUser(from);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    //사용자 단일 조회
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<UserResponseDto> findUser(@PathVariable UUID id) {
        UserResponseDto user = userService.find(id);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    //심화. 사용자 전체 조회
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public ResponseEntity<List<UserResponseDto>> findAllUsers() {
        List<UserResponseDto> members = userService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(members);
    }

    //사용자 정보를 수정할 수 있다.
    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    public ResponseEntity<UserResponseDto> updateUser(@Valid @PathVariable UUID id, @RequestBody UpdateUserCommand request) {
        UserResponseDto user = userService.updateUser(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    //사용자를 삭제할 수 있다.
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void>deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    //사용자의 온라인 상태를 업데이트할 수 있다.
    @RequestMapping(value = "/{id}/status", method = RequestMethod.PATCH)
    public ResponseEntity<UserStatusResponseDto> updateOnlineStatus(@PathVariable UUID id) {
        UserStatusResponseDto userStatus = userStatusService.updateUserStatus(id);
        return ResponseEntity.status(HttpStatus.OK).body(userStatus);
    }
}


