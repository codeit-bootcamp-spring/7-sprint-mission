package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.request.CreatePrivateChannelDto;
import com.sprint.mission.discodeit.dto.channel.request.CreatePublicChannelDto;
import com.sprint.mission.discodeit.dto.channel.request.UpdateChannelDto;
import com.sprint.mission.discodeit.dto.channel.response.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.response.CreateChannelResponseDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.enums.ChannelType;
import com.sprint.mission.discodeit.global.util.exception.CustomException;
import com.sprint.mission.discodeit.global.util.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Override
    public CreateChannelResponseDto createChannel(CreatePublicChannelDto createPublicChannelDto) {
        Channel channel = new Channel(
                ChannelType.PUBLIC,
                createPublicChannelDto.channelName(),
                createPublicChannelDto.desc()
        );
        channelRepository.save(channel);

        return CreateChannelResponseDto.from(channel);
    }

    @Override
    public CreateChannelResponseDto createChannel(CreatePrivateChannelDto createPrivateChannelDto) {
        Channel channel = new Channel(ChannelType.PRIVATE, null, null);
        createPrivateChannelDto.participantsIds().forEach(userId -> {
            User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
            ReadStatus readStatus = new ReadStatus(userId, channel.getId(), Instant.now());
            channel.addParticipant(user);
            user.joinChannel(channel);
            readStatusRepository.save(readStatus);
        });
        channelRepository.save(channel);

        // 채널 생성 시, 해당 채널에 대한 메세지가 없으므로, 가장 최근 메세지의 시간은 Instant.MIN으로 정의한다.
        return CreateChannelResponseDto.from(channel);
    }

    @Override
    public ChannelResponseDto getChannel(UUID channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHANNEL_NOT_FOUND));

        Instant lastMessageAt = messageRepository.findAllByChannelId(channel.getId()).stream()
                .sorted(Comparator.comparing(Message::getCreateAt).reversed())
                .map(Message::getCreateAt)
                .findFirst()
                .orElse(Instant.MIN);

        return ChannelResponseDto.from(channel, lastMessageAt);
    }

    @Override
    public List<ChannelResponseDto> getAllChannelByUserId(UUID userId) {
        List<UUID> participants = readStatusRepository.findAllByUserId(userId).stream()
                .map(ReadStatus::getChannelId)
                .toList();

        // 채널 타입이 퍼블릭이면 전체, private이면 userId로 필터
        return channelRepository.findAll().stream()
                .filter(channel -> channel.getChannelType().equals(ChannelType.PUBLIC)
                        || participants.contains(channel.getId()))
                .map(channel -> {
                    Instant lastMessageAt = messageRepository.findAllByChannelId(channel.getId()).stream()
                            .map(Message::getCreateAt)
                            .max(Comparator.naturalOrder())
                            .orElse(Instant.MIN);

                    return ChannelResponseDto.from(channel, lastMessageAt);
                })
                .toList();
    }

    @Override
    public CreateChannelResponseDto updateChannel(UUID channelId, UpdateChannelDto updateChannelDto) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHANNEL_NOT_FOUND));

        if (channel.getChannelType().equals(ChannelType.PRIVATE)) {
            throw new CustomException(ErrorCode.PRIVATE_CHANNEL_UPDATE_NOT_ALLOWED);
        }


        channel.updateChannel(updateChannelDto.channelName(), updateChannelDto.desc());

        return CreateChannelResponseDto.from(channel);
    }

    @Override
    public void deleteChannel(UUID channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHANNEL_NOT_FOUND));

        // 채널 삭제되면, 유저가 해당 채널에서 이탈
        userRepository.findAll().forEach(user -> user.leaveChannel(channel));

        // 채널이 삭제되면, 연관 데이터 readStatus, message도 삭제
        channelRepository.deleteById(channelId);
        readStatusRepository.deleteAllByChannelId(channelId);
        messageRepository.deleteAllByChannelId(channelId);

    }
}
