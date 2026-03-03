package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    @Query("select n from Notification n" +
            " join fetch n.receiver" +
            " where n.receiver.id=:receiverId" +
            " order by n.createdAt desc")
    List<Notification> findNotificationsByReceiverId(
            @Param("receiverId") UUID receiverId);

}
