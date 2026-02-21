package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readStatusDto.NotificationDto;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.exception.auth.AccessDeniedException;
import com.sprint.mission.discodeit.exception.notification.NotificationNotFoundException;
import com.sprint.mission.discodeit.mapper.NotificationMapper;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    @Transactional(readOnly = true)
    public List<NotificationDto> findAllNotifications(UUID userId) {
        log.info("알람 전체 조회");
        return notificationRepository.findAllByUserIdOrderByCreatedAtDesc(userId)
                .stream().map(NotificationMapper::toDto).toList();
    }

    @Override
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
