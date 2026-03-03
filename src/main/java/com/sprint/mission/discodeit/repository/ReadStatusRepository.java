package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, UUID> {
    List<ReadStatus> findAllByUserId(UUID userId);

    @Query("""
            SELECT rs
            FROM ReadStatus rs
            JOIN FETCH rs.user
            WHERE rs.channel.id IN :channelIds
            """)
    List<ReadStatus> findAllByChannelIds(
            @Param("channelIds") List<UUID> channelIds);

    @Query(" SELECT r FROM ReadStatus r WHERE r.user.id =:userId")
    List<ReadStatus> findAllWithChannelByUserId(UUID userId);

    @Query("""
            SELECT rs
            FROM ReadStatus rs
            JOIN FETCH rs.user
            WHERE rs.channel.id = :channelId
              AND rs.notificationEnabled = true
            """)
    List<ReadStatus> findAllEnabledByChannelId(@Param("channelId") UUID channelId);
}
