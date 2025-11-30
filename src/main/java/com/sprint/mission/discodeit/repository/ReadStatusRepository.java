package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, UUID> {

  void deleteAllByChannelId(UUID channelId);

  List<ReadStatus> findAllByUserId(UUID UserId);

  @Query("SELECT rs FROM ReadStatus rs "
      + "JOIN FETCH rs.user u "
      + "LEFT JOIN FETCH u.profile "
      + "LEFT JOIN FETCH u.userStatus "
      + "WHERE rs.channel = :channel")
  List<ReadStatus> findAllByChannelWithUserAndProfileAndStatus(@Param("channel") Channel channel);


  @Query("SELECT rs FROM ReadStatus rs "
      + "JOIN FETCH rs.channel "
      + "WHERE rs.user.id = :userId")
  List<ReadStatus> findAllByUserIdWithChannel(UUID userId);
}
