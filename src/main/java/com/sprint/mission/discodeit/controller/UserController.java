package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.user.UserUpdateRequestDto;
import com.sprint.mission.discodeit.dto.response.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // 사용자 등록
    @RequestMapping(method = RequestMethod.POST)
    public UserResponseDto create(@Valid @RequestBody UserCreateRequestDto userCreateRequestDto) {
        User user = userService.create(userCreateRequestDto);
        boolean online = userService.isOnline(user.getId());

        return UserResponseDto.from(user, online);
    }

    // 사용자 정보 수정
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public UserResponseDto update(@Valid @RequestBody UserUpdateRequestDto userUpdateRequestDto,
                                  @PathVariable("id") UUID id) {
        if(!id.equals(userUpdateRequestDto.id())){
            throw new IllegalArgumentException("Invalid ID");
        }
        User user = userService.update(userUpdateRequestDto);
        boolean online = userService.isOnline(user.getId());

        return UserResponseDto.from(user, online);
    }

    // 사용자 삭제
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") UUID id) {
        userService.delete(id);
    }

    // 모든 사용자 조회
    @RequestMapping(method = RequestMethod.GET)
    public List<UserResponseDto> getAll() {
        List<User> users = userService.getAll();
        return users.stream()
                .map(user ->
                        UserResponseDto.from(user,userService.isOnline(user.getId())))
                .toList();
    }
}
