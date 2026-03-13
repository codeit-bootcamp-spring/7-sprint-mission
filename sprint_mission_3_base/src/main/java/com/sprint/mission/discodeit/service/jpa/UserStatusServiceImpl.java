package com.sprint.mission.discodeit.service.jpa;

import com.sprint.mission.discodeit.dto.request.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.event.UserChangedEvent;
import com.sprint.mission.discodeit.mapper.UserStatusMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
@Primary
public class UserStatusServiceImpl implements UserStatusService {

    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;
    private final UserStatusMapper userStatusMapper;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public UserStatusDto create(UserStatusCreateRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        UserStatus status = new UserStatus(
                user,
                request.lastActiveAt()
        );

        userStatusRepository.save(status);
        eventPublisher.publishEvent(new UserChangedEvent(
                "users.updated",
                com.sprint.mission.discodeit.dto.data.UserDto.from(user)
        ));
        return userStatusMapper.toDto(status);
    }

    @Override
    public UserStatusDto getById(UUID userStatusId) {
        return userStatusRepository.findById(userStatusId)
                .map(userStatusMapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("UserStatus not found"));
    }

    @Override
    public UserStatusDto getByUserId(UUID userId) {
        return userStatusRepository.findByUserId(userId)
                .map(userStatusMapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("UserStatus not found"));
    }

    @Override
    public List<UserStatusDto> getAll() {
        return userStatusRepository.findAll()
                .stream()
                .map(userStatusMapper::toDto)
                .toList();
    }

    @Override
    public UserStatusDto updateByUserId(UUID userId, UserStatusUpdateRequest request) {
        UserStatus status = userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("UserStatus not found"));

        status.update(request.newLastActiveAt());
        eventPublisher.publishEvent(new UserChangedEvent(
                "users.updated",
                com.sprint.mission.discodeit.dto.data.UserDto.from(status.getUser())
        ));
        return userStatusMapper.toDto(status);
    }

    @Override
    public void delete(UUID userStatusId) {
        UserStatus status = userStatusRepository.findById(userStatusId)
                .orElseThrow(() -> new IllegalArgumentException("UserStatus not found"));
        com.sprint.mission.discodeit.dto.data.UserDto dto = com.sprint.mission.discodeit.dto.data.UserDto.from(status.getUser());
        userStatusRepository.delete(status);
        eventPublisher.publishEvent(new UserChangedEvent("users.updated", dto));
    }
}