package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.VerifiedUtils;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;

public class JCFChannelService implements ChannelService {

    private final Map<UUID, Channel> data;

    private JCFChannelService() {
        this.data = new HashMap<>();
    }

    private static final JCFChannelService jcfChannelService = new JCFChannelService();
    public static JCFChannelService getInstance() {
        return jcfChannelService;
    }

    @Override
    public Channel create(Channel channel) {
        Channel c = VerifiedUtils.verifyNull(channel);
        UUID n =  VerifiedUtils.verifyNull(c.getId());
        if(data.containsKey(n)) {
            throw new IllegalStateException("Channel with id " + n + " already exists");
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
            throw new NoSuchElementException("Channel with id " + n + " not found");
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

    // join
    @Override
    public boolean join(UUID channelId, UUID userId) {
        Channel c = get(channelId);
        return c.join(userId);
    }
    // leave
    @Override
    public boolean leave(UUID channelId, UUID userId) {
        Channel c = get(channelId);
        return c.leave(userId);
    }
    // slowMode
    @Override
    public void setSlowModeSeconds(UUID channelId, int slowModeSeconds) {
        Channel c = get(channelId);
        c.setSlowModeSeconds(slowModeSeconds);
    }
}
