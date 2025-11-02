package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.jcf.JCFUserStatusRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BasicUserStatusServiceTest {
    private BasicUserStatusService userStatusService;
    private JCFUserStatusRepository userStatusRepository;

    private UserStatus testStatus;

    @BeforeEach
    void setup() {
        userStatusRepository = new JCFUserStatusRepository();
        userStatusService = new BasicUserStatusService(userStatusRepository);

        // 테스트용 UserStatus 생성 및 저장
        testStatus = new UserStatus(UUID.randomUUID());
        userStatusRepository.save(testStatus);
    }

    @Test
    void updateOnlineAt_shouldUpdateUpdatedAt() throws InterruptedException {
        Instant before = testStatus.getUpdatedAt();
        userStatusService.updateOnlineAt(testStatus.getId());
        UserStatus updated = userStatusRepository.findById(testStatus.getId()).orElseThrow();
        assertTrue(updated.getUpdatedAt().isAfter(before));
    }

    @Test
    void updateOfflineAt_shouldUpdateUpdatedAt() {
        Instant before = testStatus.getUpdatedAt();
        userStatusService.updateOfflineAt(testStatus.getId());
        UserStatus updated = userStatusRepository.findById(testStatus.getId()).orElseThrow();
        assertTrue(updated.getUpdatedAt().isAfter(before));
    }

    @Test
    void update_shouldUpdateUpdatedAt() {
        Instant before = testStatus.getUpdatedAt();
        userStatusService.update(testStatus.getId());
        UserStatus updated = userStatusRepository.findById(testStatus.getId()).orElseThrow();
        assertTrue(updated.getUpdatedAt().isAfter(before));
    }

    @Test
    void updateByUserId_shouldUpdateUpdatedAt() {
        Instant before = testStatus.getUpdatedAt();
        userStatusService.updateByUserId(testStatus.getUserId()); // <-- 만약 updateByUserId가 UUID id를 받는다면 이렇게
        UserStatus updated = userStatusRepository.findById(testStatus.getId()).orElseThrow();
        assertTrue(updated.getUpdatedAt().isAfter(before));
    }

    @Test
    void delete_shouldRemoveFromRepository() {
        UUID id = testStatus.getId();
        assertTrue(userStatusRepository.existsById(id));
        userStatusService.delete(id);
        assertFalse(userStatusRepository.existsById(id));
    }
}