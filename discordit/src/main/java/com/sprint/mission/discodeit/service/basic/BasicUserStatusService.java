package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.exceptions.user.UserStatusNotExistException;
import com.sprint.mission.discodeit.dto.channel.response.DetailedChannelResponse;
import com.sprint.mission.discodeit.dto.userStatus.request.*;
import com.sprint.mission.discodeit.dto.userStatus.response.DetailedUserStatusResponse;
import com.sprint.mission.discodeit.dto.userStatus.response.UserStatusResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.common.exceptions.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {
    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    @Override
    public UserStatusResponse create(UserStatusCreateRequest dto) {
        User byUserId = userRepository.findByUserId(dto.userId())
                .orElseThrow(() -> new UserNotFoundException(dto.userId()));
        UserStatus userStatus = new UserStatus(byUserId);
        userStatusRepository.save(userStatus);
        return UserStatusResponse.toDto(userStatus);
    }

    @Override
    public UserStatusResponse get(UUID id) {
        return UserStatusResponse.toDto(userStatusRepository.findById(id)
                .orElseThrow(() -> new UserStatusNotExistException(id)));
    }

    @Override
    public List<DetailedUserStatusResponse> getAll() {
        return userStatusRepository.findAll().stream()
                .map(DetailedUserStatusResponse::toDto)
                .toList();
    }

    @Override
    public UserStatusResponse getByUser(UUID userUuid) {
        User user = userRepository.find(userUuid)
                .orElseThrow(() -> new UserNotFoundException(userUuid));
        return UserStatusResponse.toDto(userStatusRepository.findByUserId(userUuid)
                .orElseThrow(() -> new UserStatusNotExistException(user)));
    }

    @Override
    public UserStatusResponse updateByUser(UUID userId, UserStatusUpdateRequest dto) {
        User user = userRepository.find(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        UserStatus userStatus = userStatusRepository.findByUserId(userId)
                .orElseGet(() -> {
                    UserStatus created = new UserStatus(user);
                    userStatusRepository.save(created);
                    return created;
                });

        if (dto.newLastActiveAt() != null) {
            userStatus.setLastActiveAt(dto.newLastActiveAt());
        }
        userStatusRepository.update(userStatus);
        return UserStatusResponse.toDto(userStatus);
    }
}
