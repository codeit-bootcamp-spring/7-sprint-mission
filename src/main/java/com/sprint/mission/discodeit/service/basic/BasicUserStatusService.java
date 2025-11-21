package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.userStatus.UserStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.userStatus.UserStatusPatchRequestDto;

import com.sprint.mission.discodeit.dto.response.userStatus.UserStatusDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static java.time.Instant.now;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {



    private final UserRepository userRepository;
    private final UserStatusRepository userStatusRepository;
    private final UserStatusMapper userStatusMapper;


    @Override
    public void updateByUserId(UUID userId) {
        UserStatus userStatus = userStatusRepository.findById(userId).orElseThrow(()->new IllegalArgumentException("존재하지 않는 userStatus 입니다."));
        userStatusRepository.save(userStatus);

    }

    @Override
    @Transactional
    public UserStatusDto createUserStatus(UserStatusCreateRequestDto userStatusCreateRequestDto) {
       User targetUser = userRepository.findById(userStatusCreateRequestDto.getUserId()).orElseThrow(()->new IllegalArgumentException("존재하지 않는 User 입니다."));

        UserStatus userStatus = UserStatus.builder()
                .user(targetUser)
                .lastActiveAt(userStatusCreateRequestDto.getLastOnlineTime())
                .build();
        return userStatusMapper.toDto(userStatusRepository.save(userStatus));

    }

    @Override
    public void deleteUserStatus(UUID userStatusId) {
        userStatusRepository.deleteById(userStatusId);

    }


    @Override
    public List<UserStatusDto> findAll() {
       return userStatusRepository.findAll().stream().map(userStatusMapper::toDto).toList();
    }

    @Override
    @Transactional
    public UserStatusDto patchUserStatus(UUID userId, UserStatusPatchRequestDto dto) {
        User user = userRepository.findById(userId).orElseThrow(()->new IllegalArgumentException("존재하지 않는 User 입니다."));
        UserStatus userStatus = userStatusRepository.findAll().stream().filter(x->x.getUser().getId()
                .equals(userId)).findFirst()
                .orElseThrow(()->new IllegalArgumentException("존재하지 않는 UserStatus 입니다."));
        userStatus.setLastActiveAt(dto.newLastActiveAt());
        userStatusRepository.save(userStatus);
        userRepository.save(user);

        return userStatusMapper.toDto(userStatus);
    }


}
