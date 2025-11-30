package com.sprint.mission.discodeit.repository.jpa;

import com.sprint.mission.discodeit.entity.Channel;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelsRepository extends JpaRepository<Channel, UUID> {
//    void save(T model);
//    void deleteById(UUID id);
//    Optional<T> findById(UUID id);
//    List<T> findAll();
}
