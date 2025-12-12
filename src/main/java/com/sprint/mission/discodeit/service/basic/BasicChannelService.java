package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.request.CreatePrivateChannelDto;
import com.sprint.mission.discodeit.dto.channel.request.CreatePublicChannelDto;
import com.sprint.mission.discodeit.dto.channel.request.UpdateChannelDto;
import com.sprint.mission.discodeit.dto.channel.response.ChannelResponseDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.enums.ChannelType;
import com.sprint.mission.discodeit.global.exception.CustomException;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    private final ChannelMapper channelMapper;

    @Override
    @Transactional
    public ChannelResponseDto createChannel(CreatePublicChannelDto createPublicChannelDto) {
        Channel channel = Channel.builder()
                .type(ChannelType.PUBLIC)
                .name(createPublicChannelDto.name())
                .description(createPublicChannelDto.description())
                .build();

        channelRepository.save(channel);

        return channelMapper.toResponseDto(channel);
    }


    @Override
    @Transactional
    public ChannelResponseDto createChannel(CreatePrivateChannelDto createPrivateChannelDto) {
        Channel channel = Channel.builder().type(ChannelType.PRIVATE).build();
        channelRepository.save(channel);

        createPrivateChannelDto.participantIds().forEach(userId -> {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

            ReadStatus readStatus = ReadStatus.builder()
                    .user(user)
                    .channel(channel)
                    .lastReadAt(channel.getCreatedAt())
                    .build();

            readStatusRepository.save(readStatus);
        });

        return channelMapper.toResponseDto(channel);
    }


    @Override
    @Transactional(readOnly = true)
    public ChannelResponseDto getChannel(UUID channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHANNEL_NOT_FOUND));

        return channelMapper.toResponseDto(channel);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChannelResponseDto> getAllChannelByUserId(UUID userId) {
        // user가 참여한 채널 ID 조회
        List<UUID> channelIds = readStatusRepository.findAllByUserIdWithChannel(userId)
                .stream()
                .map(rs -> rs.getChannel().getId())
                .toList();

        // channelIds(참여 채널들) + PUBLIC 채널 조회
        List<Channel> channels = channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC,
                channelIds);

        // DTO로 변환
        return channels.stream()
                .map(channelMapper::toResponseDto)
                .toList();
    }


    @Override
    @Transactional
    public ChannelResponseDto updateChannel(UUID channelId, UpdateChannelDto updateChannelDto) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHANNEL_NOT_FOUND));

        if (channel.getType().equals(ChannelType.PRIVATE)) {
            throw new CustomException(ErrorCode.PRIVATE_CHANNEL_UPDATE_NOT_ALLOWED);
        }

        channel.updateChannel(updateChannelDto.newName(), updateChannelDto.newDescription());

        return channelMapper.toResponseDto(channel);
    }

    @Override
    @Transactional
    public void deleteChannel(UUID channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHANNEL_NOT_FOUND));

        // 채널이 삭제되면, 연관 데이터 readStatus, message도 삭제
        channelRepository.deleteById(channelId);
        readStatusRepository.deleteAllByChannelId(channelId);
        messageRepository.deleteAllByChannelId(channelId);

    }
}
