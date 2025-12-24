package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.common.exception.userstatus.InvalidUserStatusRequestException;
import com.sprint.mission.discodeit.common.exception.userstatus.UserStatusAlreadyExistsException;
import com.sprint.mission.discodeit.common.exception.userstatus.UserStatusNotFoundException;
import com.sprint.mission.discodeit.dto.request.userstatus.UserStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.userstatus.UserStatusUpdateByUserIdRequestDto;
import com.sprint.mission.discodeit.dto.request.userstatus.UserStatusUpdateRequestDto;
import com.sprint.mission.discodeit.dto.response.userstatus.UserStatusResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class BasicUserStatusService implements UserStatusService {
    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;
    private final UserStatusMapper userStatusMapper;

    @Transactional
    @Override
    public UserStatusResponseDto create(UserStatusCreateRequestDto userStatusCreateRequestDto) {
        UUID userId = Objects.requireNonNull(userStatusCreateRequestDto.userId());

        log.debug("Creating user status: userId = {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        if(userStatusRepository.existsByUserId(userId)) {
            log.warn("Create user status rejected: already exists: userId = {}", userId);
            throw new UserStatusAlreadyExistsException(userId);
        }

        Instant statusAt = userStatusCreateRequestDto.lastActiveAt() == null
                ? Instant.now() : userStatusCreateRequestDto.lastActiveAt();

        UserStatus userStatus = new UserStatus(user);
        userStatus.setLastActiveAt(statusAt);

        UserStatus save = userStatusRepository.save(userStatus);
        log.info("유저 상태가 생성되었습니다. userStatusId = {}", save.getId());

        return userStatusMapper.toDto(save);
    }

    @Transactional
    @Override
    public UserStatusResponseDto update(UserStatusUpdateRequestDto userStatusUpdateRequestDto, UUID id) {
        if (id == null ) {
            throw new InvalidUserStatusRequestException("id is null");
        }

        log.debug("Updating user status. user statusId = {}", id);

        UserStatus userStatus = userStatusRepository.findById(id)
                .orElseThrow(() -> new UserStatusNotFoundException(id));

        if (userStatusUpdateRequestDto.lastActiveAt() != null
                && (userStatus.getLastActiveAt() == null
                || userStatusUpdateRequestDto.lastActiveAt().isAfter(userStatus.getLastActiveAt()))) {
            userStatus.setLastActiveAt(userStatusUpdateRequestDto.lastActiveAt());
        }

        UserStatus save = userStatusRepository.save(userStatus);

        log.info("유저 상태가 변경되었습니다. userStatusId = {}", save.getId());
        return userStatusMapper.toDto(save);
    }

    @Transactional
    @Override
    public UserStatusResponseDto updateByUserId(UUID userId,
                                                UserStatusUpdateByUserIdRequestDto userStatusUpdateByUserIdRequestDto) {
        log.debug("Updating user status by user id {}", userId);
        UserStatus userStatus = userStatusRepository.findByUserId(Objects.requireNonNull(userId))
                .orElseThrow(() -> new UserStatusNotFoundException(userId));

        if(userStatusUpdateByUserIdRequestDto.newLastActiveAt() != null
                && (userStatusUpdateByUserIdRequestDto.newLastActiveAt()
                .isAfter(Objects.requireNonNull(userStatus.getLastActiveAt())))) {
            userStatus.setLastActiveAt(userStatusUpdateByUserIdRequestDto.newLastActiveAt());
        }
        UserStatus save = userStatusRepository.save(userStatus);

        log.info("유저 상태가 변경되었습니다. userStatusId = {}", save.getId());
        return userStatusMapper.toDto(save);
    }

    @Override
    public UserStatusResponseDto get(UUID id) {
        log.debug("Getting user status {}", id);
        UserStatus userStatus = userStatusRepository.findById(Objects.requireNonNull(id))
                .orElseThrow(() -> new UserStatusNotFoundException(id));

        return userStatusMapper.toDto(userStatus);
    }

    @Override
    public List<UserStatusResponseDto> getAll() {
        log.debug("Getting all user status");
        List<UserStatus> all = userStatusRepository.findAll();

        return all.stream().map(userStatus -> userStatusMapper.toDto(userStatus))
                .toList();
    }

    @Transactional
    @Override
    public boolean delete(UUID id) {
        log.debug("Deleting user status: userStatusId = {}", id);
        if(!userStatusRepository.existsById(id)) {
            throw new UserStatusNotFoundException(id);
        }
        userStatusRepository.deleteById(Objects.requireNonNull(id));
        log.info("유저 상태가 제거되었습니다. userStatusId = {}", id);
        return true;
    }
}
