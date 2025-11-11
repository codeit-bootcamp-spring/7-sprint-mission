package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.userStatus.UserStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.userStatus.UserStatusPatchRequestDto;
import com.sprint.mission.discodeit.dto.request.userStatus.UserStatusUpdateRequestDto;
import com.sprint.mission.discodeit.dto.response.UserUserStatusPatchResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;

import static java.time.Instant.now;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {
    @Override
    public void updateByUserId(UUID userId) {
        UserStatus userStatus = userStatusRepository.readUserStatus(userId).orElseThrow(()->new IllegalArgumentException("존재하지 않는 userStatus 입니다."));
        userStatus.updateEntity();
        userStatusRepository.updateUserStatus(userStatus);

    }

    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    @Override
    public UserStatus createUserStatus(UserStatusCreateRequestDto userStatusCreateRequestDto) {
        if(!userRepository.isUserExit(userStatusCreateRequestDto.getUserId())){
            throw new IllegalArgumentException("존재하지 않는 User 입니다.");
        }

        if(userStatusRepository.readAllUserStatus().stream().anyMatch(x->x.getUserId().equals(userStatusCreateRequestDto.getUserId()))){
            throw new IllegalArgumentException("이미 존재하는 UserStatus 입니다.");
        }
        UserStatus userStatus = UserStatus.builder()
                .userId(userStatusCreateRequestDto .getUserId())
                .lastOnlineTime(userStatusCreateRequestDto.getLastOnlineTime())
                .build();
        return userStatusRepository.createUserStatus(userStatus);

    }

    @Override
    public void deleteUserStatus(UUID userStatusId) {
        userStatusRepository.deleteUserStatus(userStatusId);

    }

    @Override
    public <T>void updateUserStatus(UserStatusUpdateRequestDto<T> userStatusUpdateRequestDto) {
        UserStatus userStatus = userStatusRepository.readUserStatus(userStatusUpdateRequestDto.getUserStatusId()).orElseThrow(()->new IllegalArgumentException("존재하지 않는 userStatus 입니다."));
        BiConsumer<UserStatus ,T> biConsumer = (BiConsumer<UserStatus, T>) userStatusUpdateRequestDto.getType().setter;
        biConsumer.accept(userStatus, userStatusUpdateRequestDto.getUpdateValue());
        userStatus.updateEntity();
        userStatusRepository.updateUserStatus(userStatus);
    }

    @Override
    public UserStatus find(UUID userStatusId) {
        return userStatusRepository.readUserStatus(userStatusId).orElseThrow(()->new IllegalArgumentException("존재하지 않는 userStatus 입니다."));
    }

    @Override
    public List<UserStatus> findAll() {
       return userStatusRepository.readAllUserStatus();
    }

    @Override
    public UserUserStatusPatchResponseDto patchUserStatus(UUID userId, UserStatusPatchRequestDto dto) {
        User user = userRepository.getUserById(userId).orElseThrow(()->new IllegalArgumentException("존재하지 않는 User 입니다."));
        UserStatus userStatus = userStatusRepository.readAllUserStatus().stream().filter(x->x.getUserId().equals(userId)).findFirst().orElseThrow(()->new IllegalArgumentException("존재하지 않는 UserStatus 입니다."));
        userStatus.setLastOnlineTime(dto.newLastActiveAt());
        user.setOnline(userStatus.isUserOnline());

        userStatus.updateEntity();
        user.updateEntity();


        return UserUserStatusPatchResponseDto.from(userStatus);
    }


}
