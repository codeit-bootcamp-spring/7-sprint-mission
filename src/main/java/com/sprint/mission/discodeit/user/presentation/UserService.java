package com.sprint.mission.discodeit.user.presentation;


import com.sprint.mission.discodeit.friendrequest.domain.FriendRequest;

import com.sprint.mission.discodeit.user.domain.User;
import com.sprint.mission.discodeit.user.presentation.dto.request.UserCreateRequestDto;
import com.sprint.mission.discodeit.user.presentation.dto.request.UserRequestDto;
import com.sprint.mission.discodeit.user.presentation.dto.request.UserUpdateDto;
import com.sprint.mission.discodeit.user.presentation.dto.response.UserResponseDto;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface UserService{

    //프리젠테이션으로 옮겼으니까 도메인인 User는 모르도록 만듦
    // 기존에 구현체에서 만든 메서드는 private 접근자로 바꿔서 안에서만 사용하거나, 아니면 아예 삭제 (고민중)
//    void save(User userDto);
//
//    void remove(User userDto);
//
//    User findById(UUID id);
//
//    List<User> findAll();
//
//    User findByEmail(String email);
//
//    FriendRequest sendFriendRequest(User sender,User target);
//    List<FriendRequest> getSentFriendRequests(User user);
//    List<FriendRequest> getReceivedFriendRequests(User user);
//    void acceptFriendRequest(User user, FriendRequest request);
//    void register(User user);

    //고도화
    UserResponseDto createUser(UserCreateRequestDto requestDto) throws IOException;


    // find & findAll
    UserResponseDto getUser(UserRequestDto requestDto);
    List<UserResponseDto> getAllUsers();

    UserResponseDto updateUserInfo(UserUpdateDto updateDto) throws IOException;

    //도메인과 관련된 것 모두 삭제
    void delete(UserRequestDto requestDto);
}
