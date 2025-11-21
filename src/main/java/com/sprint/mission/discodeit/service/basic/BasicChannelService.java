package com.sprint.mission.discodeit.service.basic;


import com.sprint.mission.discodeit.dto.request.channel.ChannelPatchRequestDto;
import com.sprint.mission.discodeit.dto.request.channel.ChannelPrivateCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.channel.ChannelPublicCreateRequestDto;
import com.sprint.mission.discodeit.dto.response.ChannelReadResponseDto;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.entityElement.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.sprint.mission.discodeit.service.util.StaticString.USER_NOT_EXIST;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;

    @Override
    public List<ChannelReadResponseDto> readAllChannel() {
        return channelRepository.findAll().stream().map(x->readChannel(x.getId())).toList();
    }



    @Override
    public ChannelReadResponseDto createPrivateChannel(ChannelPrivateCreateRequestDto channelPrivateCreateRequestDto) {

        Channel channel = channelRepository.save(Channel.builder()
            .name(channelPrivateCreateRequestDto.getName())
            .channelType(ChannelType.PRIVATE)
            .description(channelPrivateCreateRequestDto.getDescription())
            .build());
       channelPrivateCreateRequestDto.getParticipantIds().forEach(
                x ->
                {
                    User tempUser = userRepository.findById(x).orElseThrow(()->new IllegalArgumentException(USER_NOT_EXIST));
                   readStatusRepository.save(ReadStatus.builder()
                           .user(tempUser)
                           .channel(channel)
                           .build());
                }
        );
        return ChannelReadResponseDto.from(channel);
    }

    @Override
    public ChannelReadResponseDto createPublicChannel(ChannelPublicCreateRequestDto channelPublicCreateRequestDto) {
    Channel channel = channelRepository.save(Channel.builder()
            .name(channelPublicCreateRequestDto.getName())
            .channelType(ChannelType.PUBLIC)
            .description(channelPublicCreateRequestDto.getDescription())
            .build());
    return ChannelReadResponseDto.from(channel);
    }


    @Override
    public void createChannel(Channel channel) {
        channelRepository.save(channel);

    }

    @Override
    public ChannelReadResponseDto readChannel(UUID channelId) {
        Channel expectedChannel = channelRepository.findById(channelId).orElseThrow(()->new IllegalArgumentException("Channel not found"));
        Instant max = lastPostTime(expectedChannel);
        ChannelReadResponseDto channelReadResponseDto = ChannelReadResponseDto.from(expectedChannel);
        return channelReadResponseDto;
    }

    private Instant lastPostTime(Channel channel){
        List<Message> messageList = messageRepository.findAll();
        if(messageList.stream().noneMatch(x->x.getChannel().getId().equals(channel.getId()))) return Instant.MIN;

        return messageList.stream().
                filter(x -> x.getChannel().getId().equals(channel.getId()))
                .map(BaseUpdatableEntity::getUpdatedAt)
                .max(Comparator.naturalOrder()).orElseThrow(()->new IllegalArgumentException("Message not found"));
    }

    @Override
    public List<ChannelReadResponseDto> findAllByUserId(UUID userId) {
        List<Channel> channelList = channelRepository.findAll();
        List<ReadStatus> readStatusList = readStatusRepository.findAll();
       User targetUser = userRepository.findById(userId).orElseThrow(()->new IllegalArgumentException("User not found"));

        List<Channel> publicChannelList = channelList.stream().filter(x->
                x.getChannelType()== ChannelType.PUBLIC).collect(Collectors.toList());

        List<Channel> userContainPrivateChannel = readStatusList.stream().filter(
                x->x.getUser().getId().equals(userId) && x.getChannel().getChannelType()== ChannelType.PRIVATE
        ).map(ReadStatus::getChannel).collect(Collectors.toList());

        return Stream.concat(publicChannelList.stream(),userContainPrivateChannel.stream())
                .map(ChannelReadResponseDto::from
                        ).toList();
    }

    @Override
    public void deleteChannel(UUID channelID) {
        channelRepository.deleteById(channelID);
    }

    @Override
    public ChannelReadResponseDto patchChannel(ChannelPatchRequestDto dto, UUID channelId) {
        Channel channel = channelRepository.findById(channelId).orElseThrow(()->new IllegalArgumentException("존재하지 않는 Channel 입니다."));
        channel.setDescription(dto.newDescription()==null?channel.getDescription():dto.newDescription());
        channel.setName(dto.newName()==null?channel.getName():dto.newName());
        channelRepository.save(channel);
        return ChannelReadResponseDto.from(channel);
    }

    @Override
    public void resetChannelRepository() {
        channelRepository.deleteAll();
    }
}
