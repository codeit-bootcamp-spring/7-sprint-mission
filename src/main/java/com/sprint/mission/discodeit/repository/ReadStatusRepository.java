package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, UUID> {

    void deleteAllByChannelId(UUID channelId);

    List<ReadStatus> findAllByUserId(UUID UserId);

    @Query("SELECT rs FROM ReadStatus rs "
            + "JOIN FETCH rs.user u "
            + "LEFT JOIN FETCH u.profile "
            + "WHERE rs.channel = :channel")
    List<ReadStatus> findAllByChannelWithUserAndProfile(@Param("channel") Channel channel);


    @Query("SELECT rs FROM ReadStatus rs "
            + "JOIN FETCH rs.channel "
            + "WHERE rs.user.id = :userId")
    List<ReadStatus> findAllByUserIdWithChannel(UUID userId);

    List<ReadStatus> findAllByChannelIdAndNotificationEnabledIsTrue(UUID channelId);
}
