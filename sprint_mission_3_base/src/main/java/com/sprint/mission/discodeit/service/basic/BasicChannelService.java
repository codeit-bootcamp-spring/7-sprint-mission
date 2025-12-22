package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final ChannelMapper channelMapper;

    @Transactional
    @Override
    public ChannelDto create(ChannelCreateRequest request) {
        Channel channel = new Channel(
                ChannelType.PUBLIC,
                request.name(),
                null
        );

        channelRepository.save(channel);
        return channelMapper.toDto(channel);
    }

    @Transactional(readOnly = true)
    @Override
    public ChannelDto find(UUID channelId) {
        return channelRepository.findById(channelId)
                .map(channelMapper::toDto)
                .orElseThrow(
                        () -> new NoSuchElementException("Channel with id " + channelId + " not found"));
    }

    @Transactional(readOnly = true)
    @Override
    public List<ChannelDto> findAll() {
        return channelRepository.findAll().stream()
                .map(channelMapper::toDto)
                .toList();
    }

    @Transactional
    @Override
    public ChannelDto update(UUID channelId, ChannelUpdateRequest request) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(
                        () -> new NoSuchElementException("Channel with id " + channelId + " not found"));

        channel.update(request.name(), null);
        return channelMapper.toDto(channel);
    }

    @Transactional
    @Override
    public void delete(UUID channelId) {
        if (!channelRepository.existsById(channelId)) {
            throw new NoSuchElementException("Channel with id " + channelId + " not found");
        }
        channelRepository.deleteById(channelId);
    }
}
