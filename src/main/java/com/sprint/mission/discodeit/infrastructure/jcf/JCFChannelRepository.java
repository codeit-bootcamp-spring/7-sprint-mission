package com.sprint.mission.discodeit.infrastructure.jcf;

import com.sprint.mission.discodeit.domain.repository.ChannelRepository;
import com.sprint.mission.discodeit.domain.Channel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@ConditionalOnProperty(prefix = "discodeit.repository", name = "type", havingValue = "jcf",
matchIfMissing = true)
public class JCFChannelRepository implements ChannelRepository {

    private final Map<UUID, Channel> store= new HashMap<>();

    public void save(Channel channel){
        UUID key = channel.getId();
        store.put(key, channel);
    }

    public void remove(Channel channel){
        UUID key = channel.getId();
        store.remove(key);
    }

    public Optional<Channel> findById(UUID id){
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Channel> findAll() {
        return List.copyOf(store.values().stream().toList());
    }

}
