package com.sprint.mission.discodeit.service.basic;


import com.sprint.mission.discodeit.dto.channel.request.ChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.request.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channel.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.response.*;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.status.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@RequiredArgsConstructor
@Service
public class BasicChannelService implements ChannelService {


    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public Channel create(PublicChannelCreateRequest request) {
        String name = request.name();
        String description = request.description();
        Channel channel = new Channel(ChannelType.PUBLIC, name, description);

        return channelRepository.save(channel);
    }

    @Transactional
    @Override
    public Channel create(PrivateChannelCreateRequest request) {
        Channel channel = new Channel(ChannelType.PRIVATE, null, null);
        Channel createdChannel = channelRepository.save(channel);


        request.participantIds().stream()
                .map(userId ->
                        new ReadStatus(
                                userRepository.getReferenceById(userId),
                                createdChannel,
                                channel.getCreatedAt())).
                forEach(readStatusRepository::save);


        return createdChannel;
    }

    @Transactional(readOnly = true)
    @Override
    public ChannelDto find(UUID channelId) {
        return channelRepository.findById(channelId)
                .map(this::toDto)
                .orElseThrow(
                        () -> new NoSuchElementException("채널없어: " + channelId));

    }

    @Transactional(readOnly = true)
    @Override
    public List<ChannelDto> findAllByUserId(UUID userId) {
        List<ChannelDto> list = new ArrayList<>();
    /*    List<UUID> mySubscribedChannelIds = readStatusRepository.findChannelIdsByUserId(userId);

        List<ChannelDto> list = channelRepository.findAll().stream()
                .filter(channel ->
                        channel.getType().equals(ChannelType.PUBLIC)
                                || mySubscribedChannelIds.contains(channel.getId())
                )
                .map(this::toDto)
                .toList();*/
        List<Channel> allVisibleForUser = channelRepository.findAllVisibleForUser(userId);
        for (Channel channel : allVisibleForUser) {
            list.add(toDto(channel));
        }
        return list;
    }

    @Override
    @Transactional
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
    @Transactional
    public void delete(UUID channelId) {
        if (!channelRepository.existsById(channelId)) {
            throw new NoSuchElementException("채널이거맞아요? :" + channelId);
        }
        //매시지삭제
        messageRepository.deleteAllByChannelId(channelId);

        //리드상태 삭제
        readStatusRepository.deleteAllByChannelId(channelId);

        //채널 삭제
        channelRepository.deleteById(channelId);
    }


    //dto로보낼려고 최신메시지 시간
    //
    private ChannelDto toDto(Channel channel) {


        Instant lastMessageAt = messageRepository
                .findTopByChannelIdOrderByCreatedAtDesc(channel.getId())
                .map(Message::getCreatedAt)
                .orElse(Instant.MIN);

        List<UUID> participantIds = new ArrayList<>();

        if (channel.getType().equals(ChannelType.PRIVATE)) {
            participantIds.addAll(readStatusRepository.findUserIdsByChannelId(channel.getId()));
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
