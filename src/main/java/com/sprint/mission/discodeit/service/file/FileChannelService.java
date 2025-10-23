package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class FileChannelService implements ChannelService {

    private final ChannelRepository channelRepository;

    public FileChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @Override
    public Channel createChannel(Channel.ChannelType type, String channelName, String channelDescription) {
        Channel newChannel = new Channel(type, channelName, channelDescription);
        channelRepository.save(newChannel);
        return newChannel;
    }

    @Override
    public Channel getChannel(UUID channelId) {
        return channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("채널을 찾을 수 없습니다." + channelId));
    }

    @Override
    public List<Channel> getAllChannels() {
        return channelRepository.findAll();
    }

    @Override
    public void updateChannel(UUID channelId, Channel.ChannelType type, String channelName, String channelDescription) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("채널을 찾을 수 없습니다." + channelId));
        channel.setType(type);
        channel.setChannelName(channelName);
        channel.setDesc(channelDescription);
        channel.touch();
    }

    @Override
    public boolean isExistsChannel(UUID channelId) {
        return channelRepository.existsById(channelId);
    }

    @Override
    public void deleteChannel(UUID channelId) {
        if (!isExistsChannel(channelId)) {
            throw new NoSuchElementException("채널을 찾을 수 없습니다." + channelId);
        }
        channelRepository.deleteById(channelId);
    }
}

