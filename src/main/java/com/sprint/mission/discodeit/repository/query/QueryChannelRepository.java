package com.sprint.mission.discodeit.repository.query;

import com.sprint.mission.discodeit.dto.channel.query.ChannelInfoQuery;
import com.sprint.mission.discodeit.entity.Channel;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QueryChannelRepository extends JpaRepository<Channel, UUID> {

  @Query("""
          SELECT new com.sprint.mission.discodeit.dto.channel.query.ChannelInfoQuery(
              ch.id,
              ch.name,
              ch.description,
              ch.publicType,
              (SELECT cm.user.id FROM ChannelMember cm WHERE cm.channel = ch AND cm.role = 'MANAGER'),
              (SELECT MAX(m.createdAt) FROM Message m WHERE m.channel = ch)
          )
          FROM Channel ch
          WHERE (ch.publicType = com.sprint.mission.discodeit.entity.ChannelType.PUBLIC
                 OR ch.id IN (
                     SELECT cm.channel.id
                     FROM ChannelMember cm
                     WHERE cm.user.id = :userId
                 ))
            AND (:searchTxt IS NULL OR ch.name LIKE CONCAT('%', :searchTxt, '%'))
          ORDER BY ch.createdAt DESC
      """)
  List<ChannelInfoQuery> findAllMyChannels(
      @Param("userId") UUID userId,
      @Param("searchTxt") String searchTxt
  );

  @Query("""
            SELECT new com.sprint.mission.discodeit.dto.channel.query.ChannelInfoQuery(
              ch.id,
              ch.name,
              ch.description,
              ch.publicType,
              (SELECT cm.user.id FROM ChannelMember cm WHERE cm.channel = ch AND cm.role = 'MANAGER'),
              (SELECT MAX(m.createdAt) FROM Message m WHERE m.channel = ch)
            )
            FROM Channel ch
            WHERE ch.id = :channelId
      """)
  ChannelInfoQuery findByChannelId(@Param("channelId") UUID channelId);
}
