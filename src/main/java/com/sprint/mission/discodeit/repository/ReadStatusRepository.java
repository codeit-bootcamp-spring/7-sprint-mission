package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;

import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, UUID> {

  Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId);

  List<ReadStatus> findAllByUserId(UUID userId);

  List<ReadStatus> findAllByChannelId(UUID channelId);

  @Query("SELECT rs.user FROM ReadStatus rs WHERE rs.channel.id = :channelId")
  List<User> findUsersByChannelId(@Param("channelId") UUID channelId);

  void deleteByChannelId(UUID channelId);
}
