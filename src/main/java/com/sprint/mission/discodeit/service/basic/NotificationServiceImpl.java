package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readStatusDto.NotificationDto;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.auth.AccessDeniedException;
import com.sprint.mission.discodeit.exception.notification.NotificationNotFoundException;
import com.sprint.mission.discodeit.mapper.NotificationMapper;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.service.NotificationService;
import com.sprint.mission.discodeit.service.basic.sse.SseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final SseService sseService;
    private final RedisCacheManager cacheManager;

    @Override
    @Transactional
    @CacheEvict(value = "notification", key = "#user.id")
    public void createNotification(User user, String title, String content) {

        log.info("알림 생성 및 SSE 전송 - userId={}", user.getId());
        Notification notification = Notification.builder()
                .user(user)
                .title(title)
                .content(content)
                .build();
        notificationRepository.save(notification);
        NotificationDto dto = NotificationMapper.toDto(notification);

        sseService.send(List.of(user.getId()), "notifications.created", dto);
    }

    @Override
    @Transactional
    public void createNotifications(List<Notification> notifications) {
        notificationRepository.saveAll(notifications);
        for (Notification notification : notifications) {
            UUID userId = notification.getUser().getId();
            NotificationDto dto = NotificationMapper.toDto(notification);

            if (cacheManager.getCache("notification") != null)
                Objects.requireNonNull(cacheManager.getCache("notification")).evict(userId);

            sseService.send(List.of(userId), "notifications.created", dto);
        }
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "notification", key = "#userId")
    public List<NotificationDto> findAllNotifications(UUID userId) {
        log.info("알람 전체 조회");
        return notificationRepository.findAllByUserIdOrderByCreatedAtDesc(userId)
                .stream().map(NotificationMapper::toDto).toList();
    }

    @Override
    @Transactional
    @CacheEvict(value = "notification", key = "#userId")
    public void deleteNotification(UUID id, UUID userId) {
        log.info("알람 삭제 요청: {}", id);
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("알람 삭제 실패 - 존재하지 않는 알람: {}", id);
                    return new NotificationNotFoundException(id);
                });

        if(!notification.getUser().getId().equals(userId)) {
            log.warn("인가되지 않은 알람 삭제 요청 - 요청한 유저: {}, 알람을 가진 유저: {}",
                    notification.getUser().getId(), userId);
            throw new AccessDeniedException("본인의 알람만 삭제할 수 있습니다.");
        }

        notificationRepository.deleteById(id);
        log.info("알람 삭제 성공: {}", id);
    }
}
