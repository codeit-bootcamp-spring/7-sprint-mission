package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.request.CreatePrivateChannelDto;
import com.sprint.mission.discodeit.dto.channel.request.CreatePublicChannelDto;
import com.sprint.mission.discodeit.dto.channel.request.UpdateChannelDto;
import com.sprint.mission.discodeit.dto.channel.response.ChannelResponseDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.base.BaseEntity;
import com.sprint.mission.discodeit.entity.enums.ChannelType;
import com.sprint.mission.discodeit.global.exception.discodietException.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.global.exception.discodietException.channel.PrivateChannelUpdateException;
import com.sprint.mission.discodeit.global.exception.discodietException.user.UserNotFoundException;
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
        // 중복 제거 ParticipantIds
        // 조회가 되지 않는 user가 존재한다면, throws
        List<UUID> participantIds = createPrivateChannelDto.participantIds().stream().distinct().toList();
        List<User> users = userRepository.findAllById(participantIds);
        List<UUID> foundUserIds = users.stream().map(BaseEntity::getId).toList();
        List<UUID> notFoundUserIds = participantIds.stream()
                .filter(participantId -> !foundUserIds.contains(participantId))
                .toList();
        if (!notFoundUserIds.isEmpty()) {
            throw UserNotFoundException.byIds(notFoundUserIds);
        }

        Channel channel = Channel.builder().type(ChannelType.PRIVATE).build();
        channelRepository.save(channel);

        List<ReadStatus> readStatuses = users.stream()
                .map(user ->
                        ReadStatus.builder()
                                .user(user)
                                .channel(channel)
                                .lastReadAt(channel.getCreatedAt())
                                .build())
                .toList();
        readStatusRepository.saveAll(readStatuses);

        return channelMapper.toResponseDto(channel);
    }


    @Override
    @Transactional(readOnly = true)
    public ChannelResponseDto getChannel(UUID channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> ChannelNotFoundException.byId(channelId));

        return channelMapper.toResponseDto(channel);
    }

    // TODO: 여기 부분도 N+1 문제가 심각하게 발생
    // Return 할 때, 매번 쿼리를 수행하니, 채널만큼 쿼리가 돌아감. 수정 필요
    // How??
    @Override
    @Transactional(readOnly = true)
    public List<ChannelResponseDto> getAllChannelByUserId(UUID userId) {
        // user가 참여한 채널 ID 조회
        List<UUID> channelIds = readStatusRepository.findAllByUserIdWithChannel(userId)
                .stream()
                .map(readStatus -> readStatus.getChannel().getId())
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
                .orElseThrow(() -> ChannelNotFoundException.byId(channelId));

        if (channel.getType().equals(ChannelType.PRIVATE)) {
            throw PrivateChannelUpdateException.notAllowed(channelId);
        }

        channel.updateChannel(updateChannelDto.newName(), updateChannelDto.newDescription());

        return channelMapper.toResponseDto(channel);
    }

    @Override
    @Transactional
    public void deleteChannel(UUID channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> ChannelNotFoundException.byId(channelId));

        // 채널이 삭제되면, 연관 데이터 readStatus, message도 삭제
        channelRepository.delete(channel);
        readStatusRepository.deleteAllByChannelId(channelId);
        messageRepository.deleteAllByChannelId(channelId);

    }
}
