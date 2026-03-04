package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, UUID> {

    List<ReadStatus> findByChannelId(UUID id);

    // TODO: N+1 이 해결되는 구조인지 다시보기
    @Query("""
            SELECT rs
            FROM ReadStatus rs
            JOIN FETCH rs.user
            JOIN FETCH rs.channel
            WHERE rs.user.id = :userId
            """)
    List<ReadStatus> findAllByUserId(UUID userId);

    boolean existsByUserAndChannel(User user, Channel channel);

    Optional<ReadStatus> findByUserAndChannel(User user, Channel channel);

    @Query("""
            SELECT rs.user.id
            FROM ReadStatus rs
            WHERE rs.channel.id = :channelId
            """)
    List<UUID> findUserIdsByChannelId(UUID channelId
    );

    boolean existsByUserIdAndChannelId(UUID userId, UUID channelId);

    void deleteByChannelId(UUID channelId);

    @Query("""
            select rs.user.id
            from ReadStatus rs
            where rs.channel.id = :channelId
            and rs.notificationEnabled = true
            and rs.user.id <> :senderId
            """)
    List<UUID> findReceiverIds(UUID channelId, UUID senderId);
}
