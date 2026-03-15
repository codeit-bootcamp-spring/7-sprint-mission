package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.notification.NotificationDto;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.type.Role;
import com.sprint.mission.discodeit.event.binaryContent.BinaryContentUploadFailedEvent;
import com.sprint.mission.discodeit.event.message.MessageCreatedEvent;
import com.sprint.mission.discodeit.event.RoleUpdatedEvent;
import com.sprint.mission.discodeit.exception.notification.NotificationAccessDeniedException;
import com.sprint.mission.discodeit.exception.notification.NotificationNotFoundException;
import com.sprint.mission.discodeit.mapper.NotificationMapper;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.NotificationService;
import com.sprint.mission.discodeit.service.SseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicNotificationService implements NotificationService {


    private final SseService sseService;
    private final ReadStatusRepository readStatusRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final UserRepository userRepository;
    private final CacheManager cacheManager;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createForMessageCreated(MessageCreatedEvent event) {
        List<UUID> targetIds = readStatusRepository.findReceiverIds(event.getChannelId(), event.getSenderId());
        String title = event.getSenderName() + " (#" + event.getChannelName() + ")";
        List<Notification> notificationList = targetIds.stream().map(targetId -> Notification.create(targetId, title, event.getContent())).toList();
        try {
            notificationRepository.saveAll(notificationList);
            evictNotificationCaches(targetIds); // NOTE: 캐시 무효화


            for (Notification notification : notificationList) {
                NotificationDto dto = notificationMapper.toDto(notification);
                sseService.send(
                        List.of(notification.getReceiverId()),
                        "notifications.created",
                        dto
                );
            }

            log.info("메세지 생성 notification 저장 성공");
        } catch (Exception e) {
            log.error("메세지 생성 notification 저장 실패", e);
        }
    }

    private void evictNotificationCaches(List<UUID> targetIds) {

        Optional.ofNullable(cacheManager.getCache("notificationsByUserId"))
                .ifPresent(cache -> targetIds.forEach(cache::evict));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createForRoleUpdate(RoleUpdatedEvent event) {
        String content = event.getOldRole().name() + " -> " + event.getNewRole().name();
        String title = "권한이 변경되었습니다.";
        Notification notification = Notification.create(
                event.getReceiverId(),
                title,
                content
        );
        try {
            notificationRepository.save(notification);
            Optional.ofNullable(cacheManager.getCache("notificationsByUserId"))
                    .ifPresent(cache -> cache.evict(event.getReceiverId()));
            log.info("권한 변경 notification 저장 성공");
        } catch (Exception e) {
            log.error("권한 변경 notification 저장 실패", e);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createForFailedUpload(BinaryContentUploadFailedEvent event) {

        List<User> amdinList = userRepository.findByRole(Role.ADMIN);

        List<UUID> targetIds = amdinList.stream().map(User::getId).toList();
        String title = "파일 업로드 실패";
        List<Notification> notificationList = targetIds.stream().map(targetId ->
                Notification.create(targetId, title, event.error())
        ).toList();
        try {
            notificationRepository.saveAll(notificationList);
            evictNotificationCaches(targetIds); // NOTE: 캐시 무효화
            log.info("파일 업로드 실패 생성 notification 저장 성공");
        } catch (Exception e) {
            log.error("파일 업로드 실패 생성 notification 저장 실패", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    @Cacheable(value = "notificationsByUserId", key = "#userId")
    public List<NotificationDto> getMyNotifications(UUID userId) {

        List<Notification> myNotifications = notificationRepository.findAllByReceiverId(userId);
        return myNotifications.stream()
                .map(notificationMapper::toDto)
                .toList();
    }

    @Override
    @CacheEvict(value = "notificationsByUserId", key = "#userId")
    @Transactional
    public void deleteNotification(UUID notificationId, UUID userId) {

        Notification notification = notificationRepository.findById(notificationId).orElseThrow(() -> new NotificationNotFoundException(notificationId));
        if (!notification.getReceiverId().equals(userId)) {
            throw new NotificationAccessDeniedException(userId, notificationId);
        }
        notificationRepository.delete(notification);

    }
}
