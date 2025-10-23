package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;

    @Override
    public Channel createChannel(String channelName) {
        Channel channel = new Channel(channelName);
        return channelRepository.save(channel);
    }

    @Override
    public Channel findByChannelName(String channelName) {
        return channelRepository.findByName(channelName);
    }

    @Override
    public List<Channel> findAllChannels() {
        return channelRepository.findAll();
    }

    @Override
    public Channel updateChannel(UUID id, String channelName) {
        Channel byId = channelRepository.findById(id);
        byId.setChannelName(channelName);
        return channelRepository.save(byId);
    }

    @Override
    public Channel addMember(UUID channelId, UUID userId) {
        Channel byId = channelRepository.findById(channelId);
        byId.addMember(userId);
        return channelRepository.save(byId);
    }

    @Override
    public Channel removeMember(UUID channelId, UUID userId) {
        Channel byId = channelRepository.findById(channelId);
        byId.removeMember(userId);
        return channelRepository.save(byId);
    }

    @Override
    public void deleteChannel(UUID id) {
        channelRepository.delete(id);
    }
}
