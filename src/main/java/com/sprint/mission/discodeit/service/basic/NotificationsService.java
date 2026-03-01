package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.dto_Neo.S3UploadFailedEvent;
import com.sprint.mission.discodeit.dto.dto_Neo.MessageCreatedEvent;
import com.sprint.mission.discodeit.dto.dto_Neo.NotificationDto;
import com.sprint.mission.discodeit.dto.dto_Neo.RoleUpdatedEvent;
import com.sprint.mission.discodeit.entity.Notifications;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.mapper.NotificationsMapper;
import com.sprint.mission.discodeit.repository.jpa.NotificationsRepository;
import com.sprint.mission.discodeit.repository.jpa.ReadStatusesRepository;
import com.sprint.mission.discodeit.repository.jpa.UsersRepository;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.service.InterfaceNotificationsService;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class NotificationsService implements InterfaceNotificationsService {
    private final NotificationsRepository notificationsRepository;
    private final NotificationsMapper notificationsMapper;
    private final ReadStatusesRepository readStatusesRepository;
    private final UsersRepository userRepository;
    private final CacheManager cacheManager;

    @Override
    @Cacheable(cacheNames = "notificationsByUser", key = "#userDetails.user.id")
    public List<NotificationDto> getNotifications(DiscodeitUserDetails userDetails) {

        List<NotificationDto> notificationDtos = notificationsRepository.findAllByReceiverId(userDetails.getUser().id())
            .stream()
            .map(notificationsMapper::toDto)
            .peek(dto -> log.info("💙💙💙getNotifications.dto = {}", dto.toString()))
            .toList();

        return notificationDtos;
    }

    @Override
    @Transactional
    @CacheEvict(
        cacheNames = "notificationsByUser",
        key = "#userDetails.user.id"
    )
    public void deleteNotification(UUID notificationId, DiscodeitUserDetails userDetails) {
        Notifications notification = notificationsRepository.findById(notificationId).orElseThrow(
            () -> new DiscodeitException(ErrorCode.ERROR_RESPONSE_404,
                Map.of("notificationId", notificationId.toString())));

        if (notification.getReceiverId().equals(userDetails.getUser().id())) {
            notificationsRepository.delete(notification);
            log.info("💙💙💙 notificationsRepository.delete.notificationId = {}", notification.toString());
        }
        else {
            throw new DiscodeitException(ErrorCode.ERROR_RESPONSE_403,
                Map.of("인가되지 않은 요청", "요청자 본인의 알림에 대해서만 수행할 수 있습니다."));
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveMessageCreatedEvent(MessageCreatedEvent event) {

        log.info("🟪🟪 MessageCreatedEvent = {}", event.toString());

        Cache cache = cacheManager.getCache("notificationsByUser");

        List<ReadStatus> readStatusList = readStatusesRepository.findAllByChannelIdAndNotificationEnabledIsTrue(
            event.getChannelId());

        readStatusList.forEach(readStatus -> {
            Notifications notification = new Notifications(
                readStatus.getUser().getId(),
                event.getTitle(),
                event.getContent());

            notificationsRepository.save(notification);

            if (cache != null) {
                cache.evict(readStatus.getUser().getId());
            }

            log.info("🟪🟪🟪 saved MessageCreatedEvent = {}", notification.toString());
        });
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @CacheEvict(
        cacheNames = "notificationsByUser",
        key = "#event.receiverId"
    )
    public void saveRoleUpdateEvent(RoleUpdatedEvent event) {

        log.info("🟧 RoleUpdatedEvent = {}", event.toString());

        Notifications savedNotification = notificationsRepository.save(
            new Notifications(
                event.getReceiverId(),
                event.getTitle(),
                event.getContent()));

        log.debug("🟧 saved RoleUpdatedEvent = {}", savedNotification.toString());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)@CacheEvict(
        cacheNames = "notificationsByUser",
        key = "#admin.id"
    )
    public void saveBinaryContentStorageErrorEvent(S3UploadFailedEvent event) {
        log.info("❎ saveBinaryContentStorageErrorEvent");

        User admin = userRepository.findUserByUsername("admin")
            .orElseThrow(() -> new UsernameNotFoundException("🚨 사용자를 찾을 수 없습니다: "));

        Notifications savedNotification = notificationsRepository.save(
            new Notifications(
                admin.getId(),
                "S3 파일 업로드 실패",
                "RequestId: " + event.getRequestId().toString()));
    }
}
