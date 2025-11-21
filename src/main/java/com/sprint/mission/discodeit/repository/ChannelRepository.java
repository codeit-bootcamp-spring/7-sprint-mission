package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.domain.Channel;
import com.sprint.mission.discodeit.entity.ChannelEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelRepository extends JpaRepository<ChannelEntity, String> {
//    void save(Channel channel);
//
//    void remove(Channel channel);
//
//    Optional<Channel> findById(UUID id);
//
//    List<Channel> findAll();
}
