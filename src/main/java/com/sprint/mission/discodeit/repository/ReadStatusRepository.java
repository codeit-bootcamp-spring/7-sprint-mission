package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.status.ReadStatus;
import com.sprint.mission.discodeit.entity.status.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, UUID> {

    Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId);


    ReadStatus save(ReadStatus readStatus);

    ReadStatus find(UUID readStatusId);

    List<ReadStatus> findAll();

    List<ReadStatus> findAllByUserId(UUID userId);

    List<ReadStatus> findAllByChannelId(UUID channelId);

    void deleteById(UUID id);


    boolean existsByUserIdAndChannelId(UUID userId, UUID channelId);

    @Query("select rs.channel.id from ReadStatus rs where rs.user.id = :userId")
    List<UUID> findChannelIdsByUserId(UUID userId);

    void deleteAllByChannelId(UUID channelId);


    @Query("""
                select rs.user.id
                from ReadStatus rs
                where rs.channel.id = :channelId
            """)
    List<UUID> findUserIdsByChannelId(@Param("channelId") UUID channelId);
}
