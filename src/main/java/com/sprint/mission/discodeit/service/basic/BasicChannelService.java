package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.VerifiedUtils;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;

    @Override
    public Channel create(Channel channel) {
        Channel c = VerifiedUtils.verifyNull(channel);
        UUID n =  VerifiedUtils.verifyNull(c.getId());
        if(channelRepository.findById(n).isPresent()) {
            throw new IllegalStateException("Channel with id " + n + " already exists");
        }
        return channelRepository.save(c);
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
        return channelRepository.save(c);
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

    @Override
    public boolean join(UUID channelId, UUID userId) {
        Channel c = get(VerifiedUtils.verifyNull(channelId));
        boolean changed = c.join(VerifiedUtils.verifyNull(userId));
        if(changed) {
            channelRepository.save(c);
        }
        return changed;
    }

    @Override
    public boolean leave(UUID channelId, UUID userId) {
        Channel c = get(VerifiedUtils.verifyNull(channelId));
        boolean changed = c.leave(VerifiedUtils.verifyNull(userId));
        if(changed) {
            channelRepository.save(c);
        }
        return changed;
    }

    @Override
    public void setSlowModeSeconds(UUID channelId, int slowModeSeconds) {
        Channel c = get(channelId);
        if(slowModeSeconds < 0) {
            throw new  IllegalStateException("slowModeSeconds cannot be negative");
        }
        c.setSlowModeSeconds(slowModeSeconds);
        channelRepository.save(c);
    }
}
