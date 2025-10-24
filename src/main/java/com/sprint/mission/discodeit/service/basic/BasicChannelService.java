package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.CreateChannelDto;
import com.sprint.mission.discodeit.dto.channel.UpdateChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
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
    public Channel createChannel(CreateChannelDto createChannelDto) {
        Channel channel = new Channel(
                createChannelDto.getChannelType(),
                createChannelDto.getChannelName(),
                createChannelDto.getDesc()
        );
        channelRepository.save(channel);
        return channel;
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
    public void updateChannel(UpdateChannelDto updateChannelDto) {
        Channel channel = channelRepository.findById(updateChannelDto.getChannelId())
                .orElseThrow(() -> new NoSuchElementException("채널을 찾을 수 없습니다." + updateChannelDto.getChannelId()));
        channel.updateChannel(updateChannelDto.getChannelType(), updateChannelDto.getChannelName(), updateChannelDto.getDesc());
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
