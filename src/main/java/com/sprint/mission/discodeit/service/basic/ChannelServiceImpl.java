package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channelDto.ChannelDto;
import com.sprint.mission.discodeit.dto.channelDto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channelDto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channelDto.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.entityType.ChannelType;
import com.sprint.mission.discodeit.exception.NotFoundChannelException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChannelServiceImpl implements ChannelService {

    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final ChannelMapper channelMapper;

    @Override
    @Transactional
    public ChannelDto createPublicChannel(PublicChannelCreateRequest requestDto) {

        Channel newChannel = new Channel(
                requestDto.getName(),
                requestDto.getDescription(),
                ChannelType.PUBLIC
                );

        channelRepository.save(newChannel);
        return channelMapper.toDto(newChannel);
    }

    @Override
    @Transactional
    public ChannelDto createPrivateChannel(PrivateChannelCreateRequest requestDto) {

        Channel newChannel = new Channel(ChannelType.PRIVATE);
        channelRepository.save(newChannel);

        if (requestDto.getParticipantIds() != null && !requestDto.getParticipantIds().isEmpty()) {

            List<User> users = userRepository.findAllById(requestDto.getParticipantIds());
            List<ReadStatus> readStatuses = users.stream()
                    .map(user -> new ReadStatus(user, newChannel))
                    .collect(Collectors.toList());
            readStatusRepository.saveAll(readStatuses);
        }
        return channelMapper.toDto(newChannel);
    }

    @Override
    @Transactional(readOnly = true)
    public ChannelDto findChannelById(UUID id) {
        Channel channel = channelRepository.findById(id)
                .orElseThrow(() -> new NotFoundChannelException("채널을 찾을 수 없습니다."));

        return channelMapper.toDto(channel);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChannelDto> findAllByUserId(UUID userId) {

        List<Channel> allChannels = channelRepository.findAll();    // 모든 채널

        // 유저 수신 정보로부터 채널Id 가져오기
        Set<UUID> myChannelIds = readStatusRepository.findAllByUserId(userId)
                .stream().map(readStatus -> readStatus.getChannel().getId())
                .collect(Collectors.toSet());

        return allChannels.stream().filter(channel -> {
                    if (channel.getType() == ChannelType.PUBLIC) {
                        return true;
                    } else {
                        return myChannelIds.contains(channel.getId());
                    }
                })
                .map(channelMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ChannelDto updateChannel(UUID channelId, PublicChannelUpdateRequest updateDto) {

        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NotFoundChannelException("채널을 찾을 수 없습니다."));

        if (channel.getType() == ChannelType.PRIVATE) {
            throw new IllegalStateException("PRIVATE 채널은 수정할 수 없습니다.");
        }

        channel.updateChannelInfo(updateDto.newName(), updateDto.newDescription());

        channelRepository.save(channel);
        return channelMapper.toDto(channel);

    }

    @Override
    @Transactional
    public void deleteChannel(UUID id) {

        channelRepository.findById(id)
                        .orElseThrow(() -> new NotFoundChannelException("채널을 찾을 수 없습니다."));

        channelRepository.deleteById(id);

    }
}
