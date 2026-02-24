package com.sprint.mission.discodeit.repository.jpa;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReadStatusesRepository extends JpaRepository<ReadStatus, UUID> {

    List<ReadStatus> findAllByChannelId(UUID channelId);

    Optional<ReadStatus> findReadStatusByUserIdAndChannelId(UUID userID, UUID channelID);

    List<ReadStatus> findAllByChannelIdAndNotificationEnabledIsTrue(UUID channelId);
}
