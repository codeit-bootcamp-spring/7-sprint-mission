package com.sprint.mission.discodeit.service.jpa;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserStatusServiceImpl implements UserStatusService {

    private final UserStatusRepository userStatusRepository;

    @Override
    public void update(UUID userId) {

        UserStatus status = userStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("UserStatus not found"));

        status.updateLastSeen();  // JPA dirty checking
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        return userStatusRepository.findByUserId(userId);
    }
}
