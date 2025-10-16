package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.VerifiedUtils;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;

public class JCFChannelService implements ChannelService {
    private final ChannelRepository channelRepository;

    public JCFChannelService(ChannelRepository channelRepository) {
        this.channelRepository = VerifiedUtils.verifyNull(channelRepository);
    }

    @Override
    public Channel create(Channel channel) {
        Channel c = VerifiedUtils.verifyNull(channel);
        UUID n =  VerifiedUtils.verifyNull(c.getId());
        if(channelRepository.findById(n).isPresent()) {
            throw new IllegalStateException("Channel with id " + n + " already exists");
        }
        return channelRepository.save(channel);
    }

    @Override
    public Channel get(UUID id) {
        UUID n = VerifiedUtils.verifyNull(id);
        return channelRepository.findById(n).orElseThrow(() -> new NoSuchElementException("Channel with id " + n + " not found"));
    }

    @Override
    public Channel update(Channel channel) {
        Channel c = VerifiedUtils.verifyNull(channel);
        UUID n = VerifiedUtils.verifyNull(c.getId());
        channelRepository.findById(n).orElseThrow(() -> new NoSuchElementException("Channel with id " + n + " not found"));
        return channelRepository.save(channel);
    }

    @Override
    public boolean delete(UUID id) {
        UUID n = VerifiedUtils.verifyNull(id);
        return channelRepository.deleteById(n);
    }

    @Override
    public List<Channel> getAll() {
        return channelRepository.findAll();
    }

    // join
    @Override
    public boolean join(UUID channelId, UUID userId) {
        Channel c = get(VerifiedUtils.verifyNull(channelId));
        boolean changed = c.join(VerifiedUtils.verifyNull(userId));
        if(changed) {
            channelRepository.save(c);
        }
        return changed;
    }
    // leave
    @Override
    public boolean leave(UUID channelId, UUID userId) {
        Channel c = get(VerifiedUtils.verifyNull(channelId));
        boolean changed = c.leave(VerifiedUtils.verifyNull(userId));
        if(changed) {
            channelRepository.save(c);
        }
        return changed;
    }
    // slowMode
    @Override
    public void setSlowModeSeconds(UUID channelId, int slowModeSeconds) {
        Channel c = get(channelId);
        c.setSlowModeSeconds(slowModeSeconds);
        channelRepository.save(c);
    }
}
