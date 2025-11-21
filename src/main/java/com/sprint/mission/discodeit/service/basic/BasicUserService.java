package com.sprint.mission.discodeit.service.basic;


import com.sprint.mission.discodeit.dto.request.binaryContent.ProfileUpdateRequestDto;
import com.sprint.mission.discodeit.dto.request.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.user.UserPatchRequestDto;
import com.sprint.mission.discodeit.dto.request.user.UserUpdateRequest;
import com.sprint.mission.discodeit.dto.request.userStatus.UserStatusPatchRequestDto;
import com.sprint.mission.discodeit.dto.response.UserCreateResponseDto;
import com.sprint.mission.discodeit.dto.response.UserDto;
import com.sprint.mission.discodeit.dto.response.UserReadResponseDto;
import com.sprint.mission.discodeit.dto.response.UserUserStatusPatchResponseDto;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.entityElement.BinaryContentUsage;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;

import static com.sprint.mission.discodeit.service.util.StaticString.*;
import static com.sprint.mission.discodeit.service.util.StaticString.USER_NOT_EXIST;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final MessageRepository messageRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserStatusRepository userStatusRepository;

    @Override
    public UserCreateResponseDto createUser(UserCreateRequestDto userCreateRequestDto, MultipartFile profile) throws IOException {
        userNameEmailCheck(userCreateRequestDto);
    if(profile!=null) {
        BinaryContent binaryContent = binaryContentRepository.save(BinaryContent.builder()
                .fileName(profile.getName())
                .contentType(profile.getContentType())
                .size(profile.getSize())
                .bytes(profile.getBytes())
                .build()
        );

        User user = userRepository.save(User.builder()
                .userName(userCreateRequestDto.getUsername())
                .email(userCreateRequestDto.getEmail())
                .binaryContent(binaryContent)
                .password(userCreateRequestDto.getPassword())
                .build());

        userStatusRepository.save(UserStatus.builder()
                .user(user)
                .lastOnlineTime(Instant.now())
                .build());

        return UserCreateResponseDto.builder()
                .id(user.getId())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .username(user.getUserName())
                .email(user.getEmail())
                .password(user.getPassword())
                .profileId(user.getBinaryContent().getId())
                .build();
    }
        User user = userRepository.save(User.builder()
                .userName(userCreateRequestDto.getUsername())
                .email(userCreateRequestDto.getEmail())
                .password(userCreateRequestDto.getPassword())
                .build());

        userStatusRepository.save(UserStatus.builder()
                .user(user)
                .lastOnlineTime(Instant.now())
                .build());

        return UserCreateResponseDto.builder()
                .id(user.getId())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .username(user.getUserName())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }

    @Override
    public UserReadResponseDto readUser(UUID userID) {
        User user = userRepository.findById(userID).orElseThrow(() -> new IllegalArgumentException(USER_NOT_EXIST));
        UserStatus userStatus = userStatusRepository.findAll()
                .stream()
                .filter(x -> x.getUser().getId().equals(userID)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException(USER_NOT_EXIST));

        return UserReadResponseDto.of(user, userStatus);
    }

    @Override
    public List<UserReadResponseDto> readAllUser() {
        return userRepository.findAll().stream().map(x ->
                readUser(x.getId())).toList();
    }

    @Override
    public void deleteUser(UUID userId) {
       userRepository.deleteById(userId);
    }

    private void userNameEmailCheck(UserCreateRequestDto dto){
        boolean isUserNameExit = userRepository.findAll().stream().anyMatch(x -> x.getUserName().equals(dto.getUsername()));
        boolean isEmailExit = userRepository.findAll().stream().anyMatch(x -> x.getEmail().equals(dto.getEmail()));
        if(isUserNameExit) throw new IllegalArgumentException("USERNAME_ALREADY_EXIST");
        if(isEmailExit) throw new IllegalArgumentException("EMAIL_ALREADY_EXIST");
    }

    @Override
    public void resetUserRepository() {
        userRepository.deleteAll();
    }

    @Override
    public List<UserDto> advanceFindAllUser(){
        return userRepository.findAll().stream().map(
        x-> UserDto.builder()
                .id(x.getId())
                .createdAt(x.getCreatedAt())
                .updatedAt(x.getUpdatedAt())
                .username(x.getUserName())
                .email(x.getEmail())
                .profileId(x.getBinaryContent() == null? null: x.getBinaryContent().getId())
                .build()
                ).toList();
    }

    @Override
    public UserCreateResponseDto patchUser(UUID userId, UserUpdateRequest dto, MultipartFile profile) throws IOException {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException(USER_NOT_EXIST));
        user.setUserName(dto.newUsername()==null?user.getUserName():dto.newUsername());
        user.setPassword(dto.newPassword()==null?user.getPassword():dto.newPassword());
        user.setEmail(dto.newEmail()==null?user.getEmail():dto.newEmail());
        if(profile!=null) {
            binaryContentRepository.delete(user.getBinaryContent());
            BinaryContent tmpBinaryContent = binaryContentRepository.save(BinaryContent.builder()
                    .bytes(profile.getBytes())
                    .fileName(profile.getName())
                    .size(profile.getSize())
                    .contentType(profile.getContentType())
                    .build());
            user.setBinaryContent(tmpBinaryContent);
        }
        userRepository.save(user);

        return UserCreateResponseDto.builder()
                .id(user.getId())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .email(user.getEmail())
                .username(user.getUserName())
                .password(user.getPassword())
                .profileId(user.getBinaryContent()!=null?user.getBinaryContent().getId():null)
                .build();
    }


}
