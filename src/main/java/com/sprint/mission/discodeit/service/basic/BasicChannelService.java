package com.sprint.mission.discodeit.service.basic;


import com.sprint.mission.discodeit.dto.channel.request.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channel.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.response.*;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.status.ReadStatus;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@RequiredArgsConstructor
@Service
public class BasicChannelService implements ChannelService {


    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChannelMapper channelMapper;

    @Transactional
    @Override
    public ChannelDto create(PublicChannelCreateRequest request) {
        String name = request.name();
        String description = request.description();
        Channel channel = new Channel(ChannelType.PUBLIC, name, description);

        channelRepository.save(channel);

        return channelMapper.toDto(channel);
    }

    @Transactional
    @Override
    public ChannelDto create(PrivateChannelCreateRequest request) {
        Channel channel = new Channel(ChannelType.PRIVATE, null, null);
        Channel createdChannel = channelRepository.save(channel);


        request.participantIds().stream()
                .map(userId ->
                        new ReadStatus(
                                userRepository.getReferenceById(userId),
                                createdChannel,
                                channel.getCreatedAt())).
                forEach(readStatusRepository::save);


        return channelMapper.toDto(createdChannel);
    }

    @Transactional(readOnly = true)
    @Override
    public ChannelDto find(UUID channelId) {
        return channelRepository.findById(channelId)
                .map(channelMapper::toDto)
                .orElseThrow(
                        () -> new NoSuchElementException("채널없어: " + channelId));

    }

    @Transactional(readOnly = true)
    @Override
    public List<ChannelDto> findAllByUserId(UUID userId) {
        List<ChannelDto> list = new ArrayList<>();

        List<Channel> allVisibleForUser = channelRepository.findAllVisibleForUser(userId);
        for (Channel channel : allVisibleForUser) {
            list.add(channelMapper.toDto(channel));
        }
        return list;
    }

    @Override
    @Transactional
    public ChannelDto update(UUID channelId, ChannelUpdateRequest request) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("채널을 찾을수가 없어 " + channelId));

        //채널이 퍼블릭이면 수정해서 넣어주고
        if (channel.getType() != ChannelType.PRIVATE) {
            //수정해서
            channel.update(request.newName(), request.newDescription());
            //저장동시 리턴값 수정한 채널
            channelRepository.save(channel);
            return channelMapper.toDto(channel);
        }
        //아니야 프라이빗이야 그냥 수정전채널
        System.out.println("수정불가 그대로 반환");
        return channelMapper.toDto(channel);
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


}
