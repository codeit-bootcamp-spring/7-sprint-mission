package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.VerifiedUtils;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;

public class JCFChannelService implements ChannelService {

    private final Map<UUID, Channel> data;

    public JCFChannelService() {
        this.data = new HashMap<>();
    }

    @Override
    public Channel create(Channel channel) {
        Channel c = VerifiedUtils.verifyNull(channel);
        UUID n =  VerifiedUtils.verifyNull(c.getId());
        if(data.containsKey(n)) {
            throw new IllegalArgumentException("Channel with id " + n + " already exists");
        }
        data.put(n, c);
        return c;
    }

    @Override
    public Channel get(UUID id) {
        UUID n = VerifiedUtils.verifyNull(id);
        Channel c = data.get(n);
        if(c == null) {
            throw new NoSuchElementException("Channel with id " + n + " not found");
        }
        return c;
    }

    @Override
    public Channel update(Channel channel) {
        Channel c = VerifiedUtils.verifyNull(channel);
        UUID n = VerifiedUtils.verifyNull(c.getId());
        if(!data.containsKey(n)) {
            throw new NoSuchElementException("Channel already exists");
        }
        data.put(n,c);
        return c;
    }

    @Override
    public boolean delete(UUID id) {
        UUID n = VerifiedUtils.verifyNull(id);
        return data.remove(n) != null;
    }

    @Override
    public List<Channel> getAll() {
        return  new ArrayList<>(data.values());
    }
}
