package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelDto;
import com.sprint.mission.discodeit.dto.channel.UpdateChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.enums.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Override
    public Channel createChannel(PublicChannelDto publicChannelDto) {
        Channel channel = new Channel(
                ChannelType.PUBLIC,
                publicChannelDto.getChannelName(),
                publicChannelDto.getDesc()
        );
        channelRepository.save(channel);
        return channel;
    }

    @Override
    public Channel createChannel(PrivateChannelDto privateChannelDto) {
        Channel channel = new Channel(ChannelType.PRIVATE, null, null);
        privateChannelDto.getParticipants().forEach(userId -> {
            User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException("찾을 수 없는 유저입니다." + userId));
            ReadStatus readStatus = new ReadStatus(userId, channel.getId(), Instant.now());
            channel.addParticipant(user);
            user.joinChannel(channel);
            readStatusRepository.save(readStatus);
        });
        channelRepository.save(channel);
        return channel;
    }

    @Override
    public ChannelResponseDto getChannel(UUID channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("채널을 찾을 수 없습니다." + channelId));

        Instant lastMessageAt = messageRepository.findAllByChannelId(channel.getId()).stream()
                .sorted(Comparator.comparing(Message::getCreateAt).reversed())
                .map(Message::getCreateAt)
                .findFirst()
                .orElse(null);

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
                            .orElse(null);

                    return ChannelResponseDto.from(channel, lastMessageAt);
                })
                .toList();
    }

    @Override
    public void updateChannel(UpdateChannelDto updateChannelDto) {
        Channel channel = channelRepository.findById(updateChannelDto.getChannelId())
                .orElseThrow(() -> new NoSuchElementException("채널을 찾을 수 없습니다." + updateChannelDto.getChannelId()));

        if (updateChannelDto.getChannelType().equals(ChannelType.PUBLIC)) {
            throw new IllegalArgumentException("Private Channel은 수정할 수 없습니다." + channel.getId());
        }

        channel.updateChannel(updateChannelDto.getChannelType(), updateChannelDto.getChannelName(), updateChannelDto.getDesc());
    }

    @Override
    public void deleteChannel(UUID channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("찾을 수 없는 채널입니다." + channelId));

        // 채널 삭제되면, 유저가 해당 채널에서 이탈
        userRepository.findAll().forEach(user -> user.leaveChannel(channel));

        // 채널이 삭제되면, 연관 데이터 readStatus, message도 삭제
        channelRepository.deleteById(channelId);
        readStatusRepository.deleteAllByChannelId(channelId);
        messageRepository.deleteAllByChannelId(channelId);


    }
}
