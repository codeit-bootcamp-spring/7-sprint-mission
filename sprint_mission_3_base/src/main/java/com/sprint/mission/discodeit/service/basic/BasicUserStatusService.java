package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicUserStatusService implements UserStatusService {

    private final UserStatusRepository userStatusRepository;
    private final UserRepository userRepository;

    @Override
    public UUID create(UserStatusCreateRequest request) {
        if (!userRepository.existsById(request.userId())) {
            throw new NoSuchElementException("User not found: " + request.userId());
        }
        if (userStatusRepository.existsByUserId(request.userId())) {
            throw new IllegalStateException("UserStatus already exists for userId=" + request.userId());
        }
        var us = new UserStatus(request.userId(), request.lastSeenAt());
        userStatusRepository.save(us);
        return us.getId();
    }

    @Override
    public Optional<Instant> findLastSeenByUserId(UUID userId) {
        return userStatusRepository.findByUserId(userId).map(UserStatus::getLastSeenAt);
    }

    @Override
    public List<UUID> findAllUserIdsOnlineWithinMinutes(int minutes) {
        Instant threshold = Instant.now().minus(Duration.ofMinutes(minutes));
        return userStatusRepository.findAll().stream()
                .filter(us -> !us.getLastSeenAt().isBefore(threshold))
                .map(UserStatus::getUserId)
                .collect(Collectors.toList());
    }

    @Override
    public void update(UserStatusUpdateRequest request) {
        var us = userStatusRepository.findById(request.id())
                .orElseThrow(() -> new NoSuchElementException("UserStatus not found: " + request.id()));
        us.update(request.lastSeenAt()); // 엔티티에 update(Instant) 메서드가 있다고 가정
        userStatusRepository.save(us);
    }

    @Override
    public void updateByUserId(UUID userId, Instant lastSeenAt) {
        var usOpt = userStatusRepository.findByUserId(userId);
        if (usOpt.isPresent()) {
            var us = usOpt.get();
            us.update(lastSeenAt);
            userStatusRepository.save(us);
        } else {
            // 없으면 생성
            var us = new UserStatus(userId, lastSeenAt);
            userStatusRepository.save(us);
        }
    }

    @Override
    public void delete(UUID id) {
        userStatusRepository.deleteById(id);
    }
}
