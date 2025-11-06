package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.response.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.request.CreatePrivateChannelDto;
import com.sprint.mission.discodeit.dto.request.CreatePublicChannelDto;
import com.sprint.mission.discodeit.dto.update.UpdateChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.enum_.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;


    @Override
    public ChannelResponseDto createPrivateChannel(CreatePrivateChannelDto dto) {
        Channel channel = new Channel(
                null,
                null,
                ChannelType.PRIVATE
        );

        Channel saved = channelRepository.save(channel);

        List<UUID> members = dto.members();
        for (UUID member : members) {
            saved.getMembers().add(member);
            ReadStatus readStatus = new ReadStatus(member, saved.getId());
            readStatusRepository.save(readStatus);
        }

        return ChannelResponseDto.from(saved, null, members);
    }

    @Override
   public ChannelResponseDto createPublicChannel(CreatePublicChannelDto dto) {
       if(channelRepository.findByName(dto.channelName()).isPresent()){
           throw new IllegalArgumentException("채널이 이미 존재합니다.");
       }
       Channel channel = new Channel(
               dto.channelName(),
               dto.description(),
               ChannelType.PUBLIC
       );

        Channel saved = channelRepository.save(channel);
        return ChannelResponseDto.from(saved, null, saved.getMembers());
   }

    @Override
    public ChannelResponseDto find(UUID channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new IllegalArgumentException("해당 하는 채널을 찾을 수 없습니다."));

        List<Message> messages = messageRepository.findByChannelId(channel.getId());

        Instant lastMassageTime = null;
        for (Message message : messages) {
            if (lastMassageTime == null || message.getCreatedAt().isAfter(lastMassageTime)) {
                lastMassageTime = message.getCreatedAt();
            }
        }

        List<UUID> members = null;
        if (channel.getType() == ChannelType.PRIVATE) {
            members = channel.getMembers();
        }

        return ChannelResponseDto.from(channel, lastMassageTime, members);
    }

    @Override
    public List<ChannelResponseDto> findAllByUserId(UUID userId) {
        List<Channel> channels = channelRepository.findAll();
        List<ChannelResponseDto> dtoList = new ArrayList<>();

        List<Channel> needChannels = new ArrayList<>();
        for(Channel channel : channels){
            if(channel.getType() == ChannelType.PUBLIC){
                needChannels.add(channel);
            } else if (channel.getType() == ChannelType.PRIVATE && channel.getMembers().contains(userId)){
                needChannels.add(channel);
            }
        }

        for(Channel channel : needChannels){
            List<Message> messages = messageRepository.findByChannelId(channel.getId());

            Instant readMassage = messages.stream()
                    .map(Message::getCreatedAt)
                    .max(Instant::compareTo)
                    .orElse(null);

            List<UUID> members = null;
            if(channel.getType() == ChannelType.PRIVATE) {
                members = channel.getMembers();
            }
                ChannelResponseDto response = ChannelResponseDto.from(channel, readMassage, members);
                dtoList.add(response);
            }
        return dtoList;
    }

    @Override
    public ChannelResponseDto updateChannel(UUID id, UpdateChannelDto updateChannelDto) {
        Channel channel = channelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("채널을 찾을 수 없습니다."));

        if(channel.getType() == ChannelType.PRIVATE){
            throw new IllegalArgumentException("Private 채널은 수정할 수 없습니다.");
        }

        channel.updateInfo(
                updateChannelDto.channelName(),
                updateChannelDto.description(),
                updateChannelDto.channelType()
        );

        Channel updated = channelRepository.save(channel);

        return ChannelResponseDto.from(updated, updated.getUpdatedAt(), updated.getMembers());
    }

    @Override
    public void deleteChannel(UUID channelId) {
        channelRepository.findById(channelId)
                .orElseThrow(() -> new IllegalArgumentException("채널을 찾을 수 없습니다."));

        messageRepository.deleteByChannelId(channelId);
        readStatusRepository.deleteChannelById(channelId);
        channelRepository.delete(channelId);
    }

    // 요구사항 진행 해본 뒤 구현하기
//    @Override
//    public Channel addMember(UUID channelId, UUID userId) {
//        Channel byId = channelRepository.findById(channelId);
//        byId.addMember(userId);
//        return channelRepository.save(byId);
//    }
//
//    @Override
//    public Channel removeMember(UUID channelId, UUID userId) {
//        Channel byId = channelRepository.findById(channelId);
//        byId.removeMember(userId);
//        return channelRepository.save(byId);
//    }
}
