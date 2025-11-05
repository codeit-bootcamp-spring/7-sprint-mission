package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userStatus.request.*;
import com.sprint.mission.discodeit.dto.userStatus.response.UserStatusResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exceptions.ChannelNotFoundException;
import com.sprint.mission.discodeit.exceptions.UserNotFoundException;
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
        return UserStatusResponse.toDto(userStatusRepository.findById(id));
    }

    @Override
    public List<UserStatusResponse> getAll() {
        return userStatusRepository.findAll().stream()
                .map(UserStatusResponse::toDto)
                .toList();
    }

    @Override
    public UserStatusResponse update(UserStatusUpdateRequest dto) {
        UserStatus userStatus = userStatusRepository.findById(dto.id());
        userStatus.setOnlineStatus(dto.onlineStatus());
        userStatusRepository.update(userStatus);
        return UserStatusResponse.toDto(userStatus);
    }

    @Override
    public UserStatusResponse updateByUserId(UserStatusUpdateByUserIdRequest dto) {
        UserStatus userStatus = userStatusRepository.findByUser(
                userRepository.findByUserId(dto.userId())
                        .orElseThrow(() -> new UserNotFoundException(dto.userId())));
        userStatus.setOnlineStatus(dto.onlineStatus());
        userStatusRepository.update(userStatus);
        return UserStatusResponse.toDto(userStatus);
    }
}
