package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicUserStatusService implements UserStatusService {
    private final UserStatusRepository userStatusRepository;

    @Override
    public UserStatus create(UserStatus userStatus) {
        return userStatusRepository.save(userStatus);
    }

    @Override
    public UserStatus findById(UUID id) {
        return userStatusRepository.findById(id);
    }

    @Override
    public UserStatus findByUserId(UUID userId) {
        return userStatusRepository.findByUserId(userId);
    }

    @Override
    public UserStatus updateOnlineAt(UUID userId) {
        return userStatusRepository.updateOnlineAt(userId);
    }

    @Override
    public UserStatus updateOfflineAt(UUID userId) {
        return userStatusRepository.updateOfflineAt(userId);
    }

    @Override
    public UserStatus delete(UUID id) {
        return userStatusRepository.delete(id);
    }
}
