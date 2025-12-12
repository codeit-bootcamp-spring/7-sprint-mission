package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.userStatus.UserStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.userStatus.UserStatusPatchRequestDto;

import com.sprint.mission.discodeit.dto.response.userStatus.UserStatusDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.domain.user.UserNotExistException;
import com.sprint.mission.discodeit.exception.domain.userStatus.UserStatusNotExistException;
import com.sprint.mission.discodeit.exception.domain.userStatus.UserStatusNotMatchException;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.UnknownServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        User user = userRepository.findById(userId).orElseThrow(()-> new UserNotExistException(userId));
        UUID userStatusId = user.getUserStatus().getId();
        UserStatus userStatus = userStatusRepository.findById(userStatusId).orElseThrow(()->new UserStatusNotExistException(userStatusId));
        userStatusRepository.save(userStatus);

    }

    @Override
    @Transactional
    public UserStatusDto createUserStatus(UserStatusCreateRequestDto userStatusCreateRequestDto) {
        UUID userId = userStatusCreateRequestDto.userId();
       User targetUser = userRepository.findById(userId).orElseThrow(()->new UserNotExistException(userId));
        UserStatus userStatus = new UserStatus(targetUser,userStatusCreateRequestDto.lastOnlineTime());

        return userStatusMapper.toDto(userStatusRepository.save(userStatus));

    }

    @Override
    public void deleteUserStatus(UUID userStatusId) {
        userStatusRepository.deleteById(userStatusId);

    }


    @Override
    @Transactional(readOnly = true)
    public List<UserStatusDto> findAll() {
       return userStatusRepository.findAll().stream().map(userStatusMapper::toDto).toList();
    }

    @Override
    @Transactional
    public UserStatusDto patchUserStatus(UUID userId, UserStatusPatchRequestDto dto) {
        User user = userRepository.findById(userId).orElseThrow(()->new UserNotExistException(userId));
        UserStatus userStatus = userStatusRepository.findAll().stream().filter(x->x.getUser().getId()
                .equals(userId)).findFirst()
                .orElseThrow(()->new UserStatusNotMatchException(userId));
        userStatus.setLastActiveAt(dto.newLastActiveAt());
        userStatusRepository.save(userStatus);
        userRepository.save(user);

        return userStatusMapper.toDto(userStatus);
    }


}
