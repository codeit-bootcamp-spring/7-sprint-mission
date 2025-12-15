package com.sprint.mission.discodeit.service.jpa;

import com.sprint.mission.discodeit.dto.channel.ChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
@Primary
@Service
@RequiredArgsConstructor
@Transactional
public class ChannelServiceImpl implements ChannelService {

    private final ChannelRepository channelRepository;

    @Override
    public ChannelDto create(ChannelCreateRequest request) {
        Channel channel = new Channel(
                ChannelType.PUBLIC,
                request.name(),
                null
        );

        channelRepository.save(channel);
        return ChannelDto.from(channel);
    }

    @Override
    public ChannelDto update(UUID channelId, ChannelUpdateRequest request) {

        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new IllegalArgumentException("Channel not found"));

        channel.update(request.name(), null);

        return ChannelDto.from(channel);
    }

    @Override
    public void delete(UUID id) {      // <-- UUID로 변경됨
        channelRepository.deleteById(id);
    }

    @Override
    public ChannelDto find(UUID id) {  // <-- UUID로 변경됨
        return channelRepository.findById(id)
                .map(ChannelDto::from)
                .orElseThrow(() -> new IllegalArgumentException("채널을 찾을 수 없습니다."));
    }

    @Override
    public List<ChannelDto> findAll() {
        return channelRepository.findAll()
                .stream()
                .map(ChannelDto::from)
                .toList();
    }


}
