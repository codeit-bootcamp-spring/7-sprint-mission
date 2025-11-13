package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.entity.dto.channelDto.*;
import com.sprint.mission.discodeit.entity.entityType.ChannelType;
import com.sprint.mission.discodeit.exception.NotFoundChannelException;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;

    private ChannelResponseDto toDto(Channel channel) {

        Optional<Message> lastMessage = messageRepository.findTopByChannelId(channel.getId());

        List<UUID> participantIds = readStatusRepository.findAllByChannelId(channel.getId())
                .stream().map(ReadStatus::getUserId).toList();

        return ChannelResponseDto.from(channel, participantIds, lastMessage.orElse(null));
    }


    @Override
    public Channel createPublicChannel(PublicChannelRequestDto requestDto) {

        Channel newChannel = new Channel(
                requestDto.getName(),
                requestDto.getDescription(),
                ChannelType.PUBLIC
                );

        channelRepository.save(newChannel);
        return newChannel;
    }

    @Override
    public Channel createPrivateChannel(PrivateChannelRequestDto requestDto) {

        Channel newChannel = new Channel(ChannelType.PRIVATE);
        channelRepository.save(newChannel);
        if (requestDto.getParticipantIds() != null && !requestDto.getParticipantIds().isEmpty()) {

            for (UUID userId : requestDto.getParticipantIds()) {
                readStatusRepository.save(new ReadStatus(userId, newChannel.getId()));
            }
        }
        return newChannel;
    }

    @Override
    public ChannelResponseDto findChannelById(UUID id) {
        Channel channel = channelRepository.findById(id)
                .orElseThrow(() -> new NotFoundChannelException("채널을 찾을 수 없습니다."));

        return toDto(channel);
    }

    @Override
    public List<ChannelResponseDto> findAllByUserId(UUID userId) {

        List<Channel> allChannels = channelRepository.findAll();    // 모든 채널

        // 유저 수신 정보로부터 채널Id 가져오기
        Set<UUID> myChannelIds = readStatusRepository.findAllByUserId(userId)
                .stream().map(ReadStatus::getChannelId).collect(Collectors.toSet());

        return allChannels.stream().filter(channel -> {
                    if (channel.getType() == ChannelType.PUBLIC) {
                        return true;
                    } else {
                        return myChannelIds.contains(channel.getId());
                    }
                })
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Channel updateChannel(UUID channelId, PublicChannelUpdateDto updateDto) {

        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NotFoundChannelException("채널을 찾을 수 없습니다."));

        if (channel.getType() == ChannelType.PRIVATE) {
            throw new IllegalStateException("PRIVATE 채널은 수정할 수 없습니다.");
        }

        channel.updateChannelInfo(updateDto.newName(), updateDto.newDescription());
        channelRepository.save(channel);

        return channel;

    }

    @Override
    public void deleteChannel(UUID id) {

        channelRepository.findById(id)
                        .orElseThrow(() -> new NotFoundChannelException("채널을 찾을 수 없습니다."));

            readStatusRepository.deleteAllByChannelId(id);
            messageRepository.deleteAllByChannelId(id);
            channelRepository.deleteById(id);

    }
}
