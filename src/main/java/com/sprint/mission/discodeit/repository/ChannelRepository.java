package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;


public interface ChannelRepository {

    Channel save(Channel channel);
    Optional<Channel> findById(UUID id);
    List<Channel> findAll();
    boolean existsById(UUID id);
    void deleteById(UUID id);
}
