package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.dto.channel.response.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.status.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;


public interface ChannelRepository extends JpaRepository<Channel, UUID> {

    Channel save(Channel channel);

    Optional<Channel> findById(UUID id);

    List<Channel> findAll();

    boolean existsById(UUID id);

    void deleteById(UUID id);

    @Query("""
              select c
              from Channel c
              where c.type = "PUBLIC"
                 or c.id in (
                      select rs.channel.id from ReadStatus rs where rs.user.id = :userId
                 )
            """)
    List<Channel> findAllVisibleForUser(UUID userId);
}
