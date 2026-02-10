package com.sprint.mission.discodeit.repository.jpa;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChannelsRepository extends JpaRepository<Channel, UUID> {
//    void save(T model);
//    void deleteById(UUID id);
//    Optional<T> findById(UUID id);
//    List<T> findAll();

//    @Query("SELECT c FROM Channel c WHERE c.type = 'PUBLIC'")
//    List<Channel> findPublicChannels();

    List<Channel> findByType(ChannelType type);
}
