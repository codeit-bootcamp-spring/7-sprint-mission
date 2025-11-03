package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.userStatus.request.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.userStatus.request.UserStatusUpdateByIdRequest;
import com.sprint.mission.discodeit.dto.userStatus.request.UserStatusUpdateByUserIdRequest;
import com.sprint.mission.discodeit.dto.userStatus.response.UserStatusResponse;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
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

    @Override
    public UserStatusResponse create(UserStatusCreateRequest dto) {
        User byUserId = userRepository.findByUserId(dto.userId());
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
    public void update(UserStatusUpdateByIdRequest dto) {
        UserStatus userStatus = userStatusRepository.findById(dto.id());
        userStatus.setOnlineStatus(dto.onlineStatus());
        userStatusRepository.update(userStatus);
    }

    @Override
    public void updateById(UserStatusUpdateByUserIdRequest dto) {
        UserStatus userStatus = userStatusRepository.findByUser(
                userRepository.findByUserId(dto.userId()));
        userStatus.setOnlineStatus(dto.onlineStatus());
        userStatusRepository.update(userStatus);
    }

    @Override
    public void delete(UUID id) {
        userStatusRepository.deleteById(id);
    }
}
