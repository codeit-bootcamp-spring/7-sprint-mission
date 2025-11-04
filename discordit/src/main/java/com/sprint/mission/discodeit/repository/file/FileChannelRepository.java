package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.enums.ChannelScope;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@Primary
public class FileChannelRepository implements ChannelRepository {

    private final Map<UUID, Channel> data = new HashMap<>();


    @Override
    public void save(Channel channel) {
        data.put(channel.getUuid(), channel);
        write();
    }

    @Override
    public void update(Channel channel) {
        data.put(channel.getUuid(), channel);
        write();
    }


    @Override
    public Optional<Channel> findById(UUID uuid) {
        return Optional.ofNullable(data.get(uuid));
    }

    @Override
    public List<Channel> findAll() {
        return data.values().stream()
                .sorted(Comparator.comparing(Channel::getDisplayName))
                .toList();
    }

    @Override
    public List<Channel> findAllPublic() {
        return data.values().stream()
                .filter(c -> c.getScope() == ChannelScope.PUBLIC)
                .toList();
    }

    @Override
    public List<Channel> findAllPrivateByUser(User user) {
        return data.values().stream()
                .filter(c -> c.getScope() == ChannelScope.PRIVATE)
                .filter(c -> c.getMembers().contains(user))
                .toList();
    }

    @Override
    public void deleteById(UUID uuid) {
        data.remove(uuid);
        write();
    }

    @Override
    public boolean existsById(UUID uuid) {
        return data.containsKey(uuid);
    }

    private void write() {
        DataWriter.writeChannel(data);
    }

}
