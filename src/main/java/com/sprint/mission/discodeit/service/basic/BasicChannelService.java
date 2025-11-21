package com.sprint.mission.discodeit.service.basic;


import com.sprint.mission.discodeit.dto.channel.request.ChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.request.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channel.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.response.*;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.status.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@RequiredArgsConstructor
@Service
public class BasicChannelService implements ChannelService {


    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;


    @Override
    public Channel create(PublicChannelCreateRequest request) {
        String name = request.name();
        String description = request.description();
        Channel channel = new Channel(ChannelType.PUBLIC, name, description);

        return channelRepository.save(channel);
    }

    @Override
    public Channel create(PrivateChannelCreateRequest request) {
        Channel channel = new Channel(ChannelType.PRIVATE, null, null);
        Channel createdChannel = channelRepository.save(channel);

        request.participantIds().stream()
                .map(userId ->
                        new ReadStatus(userId,
                                createdChannel.getId(),
                                channel.getCreatedAt())).
                forEach(readStatusRepository::save);


        return createdChannel;
    }

    @Override
    public ChannelDto find(UUID channelId) {
        return channelRepository.findById(channelId)
                .map(this::toDto)
                .orElseThrow(
                        () -> new NoSuchElementException("채널없어: " + channelId));

    }


    @Override
    public List<ChannelDto> findAllByUserId(UUID userId) {

        List<UUID> mySubscribedChannelIds = readStatusRepository.findAllByUserId(userId).stream()
                .map(ReadStatus::getChannelId)
                .toList();

        List<ChannelDto> list = channelRepository.findAll().stream()
                .filter(channel ->
                        channel.getType().equals(ChannelType.PUBLIC)
                                || mySubscribedChannelIds.contains(channel.getId())
                )
                .map(this::toDto)
                .toList();
        System.out.println("리스트들이다" + list);
        return list;
    }

    @Override
    public Channel update(UUID channelId, ChannelUpdateRequest request) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("채널을 찾을수가 없어 " + channelId));

        //채널이 퍼블릭이면 수정해서 넣어주고
        if (channel.getType() != ChannelType.PRIVATE) {
            //수정해서
            channel.update(request.newName(), request.newDescription());
            //저장동시 리턴값 수정한 채널
            return channelRepository.save(channel);
        }
        //아니야 프라이빗이야 그냥 수정전채널
        System.out.println("수정불가 그대로 반환");
        return channel;
    }

    @Override
    public void delete(UUID channelId) {
        if (!channelRepository.existsById(channelId)) {
            throw new NoSuchElementException("채널이거맞아요? :" + channelId);
        }
        //매시지삭제
        messageRepository.findAll().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .forEach(message -> messageRepository.deleteById(message.getId()));
        //리드상태 삭제
        readStatusRepository.findAllByUserId(channelId).stream()
                .filter(readStatus -> readStatus.getChannelId().equals(channelId))
                .forEach(message -> readStatusRepository.deleteById(message.getId()));
        //채널 삭제
        channelRepository.deleteById(channelId);
    }


    //dto로보낼려고 최신메시지 시간
    //
    private ChannelDto toDto(Channel channel) {
        System.out.println(channel.getId());
        Instant lastMessageAt = messageRepository.findAllByChannelId(channel.getId())
                .stream()
                .sorted(Comparator.comparing(Message::getCreatedAt).reversed())
                .map(Message::getCreatedAt)
                .limit(1)
                .findFirst()
                .orElse(Instant.MIN);

        List<UUID> participantIds = new ArrayList<>();

        if (channel.getType().equals(ChannelType.PRIVATE)) {
            readStatusRepository.findAllByChannelId(channel.getId())
                    .stream()
                    .map(ReadStatus::getUserId)
                    .forEach(participantIds::add);
        }
        return new ChannelDto(
                channel.getId(),
                channel.getType(),
                channel.getName(),
                channel.getDescription(),
                participantIds,
                lastMessageAt
        );
    }

}
