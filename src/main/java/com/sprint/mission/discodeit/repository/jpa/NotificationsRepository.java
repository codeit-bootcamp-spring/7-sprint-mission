package com.sprint.mission.discodeit.repository.jpa;

import com.sprint.mission.discodeit.entity.Notifications;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationsRepository extends JpaRepository<Notifications, UUID> {

    List<Notifications> findAllByReceiverId(UUID id);
}