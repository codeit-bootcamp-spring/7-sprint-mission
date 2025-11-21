package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.userStatus.UserStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.userStatus.UserStatusPatchRequestDto;

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
        UserStatus userStatus = userStatusRepository.findById(userId).orElseThrow(()->new IllegalArgumentException("존재하지 않는 userStatus 입니다."));
        userStatusRepository.save(userStatus);

    }

    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    @Override
    public UserStatus createUserStatus(UserStatusCreateRequestDto userStatusCreateRequestDto) {
       User targetUser = userRepository.findById(userStatusCreateRequestDto.getUserId()).orElseThrow(()->new IllegalArgumentException("존재하지 않는 User 입니다."));

        UserStatus userStatus = UserStatus.builder()
                .user(targetUser)
                .lastOnlineTime(userStatusCreateRequestDto.getLastOnlineTime())
                .build();
        return userStatusRepository.save(userStatus);

    }

    @Override
    public void deleteUserStatus(UUID userStatusId) {
        userStatusRepository.deleteById(userStatusId);

    }


    @Override
    public List<UserStatus> findAll() {
       return userStatusRepository.findAll();
    }

    @Override
    public UserUserStatusPatchResponseDto patchUserStatus(UUID userId, UserStatusPatchRequestDto dto) {
        User user = userRepository.findById(userId).orElseThrow(()->new IllegalArgumentException("존재하지 않는 User 입니다."));
        UserStatus userStatus = userStatusRepository.findAll().stream().filter(x->x.getUser().getId()
                .equals(userId)).findFirst()
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 UserStatus 입니다."));
        userStatus.setLastOnlineTime(dto.newLastActiveAt());
        userStatusRepository.save(userStatus);
        userRepository.save(user);

        return UserUserStatusPatchResponseDto.from(userStatus);
    }


}
