package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.entity.Channel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@ConditionalOnProperty(prefix = "discodeit.repository", name = "type", havingValue = "jcf",
matchIfMissing = true)
public class JCFChannelRepository implements ChannelRepository {

    private final Map<UUID, Channel> store= new HashMap<>();

    public JCFChannelRepository() {
        UUID serverId = UUID.fromString("12121212-1212-1212-1212-121212343434");
        List<UUID> members = new ArrayList<>();
        UUID userId = UUID.fromString("32121212-1212-1212-1212-121212343434");
        members.add(userId);
        Channel channel = new Channel("testChannel", members, false);

        UUID channelId = UUID.fromString("02121212-1212-1212-1212-121212343434");
        store.put(channelId,channel);
        System.out.println("채널 저장 완료"+store.get(channelId).getChannelName());

    }

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
