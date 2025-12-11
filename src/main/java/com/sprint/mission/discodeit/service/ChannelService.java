package com.sprint.mission.discodeit.service;


import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.service.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.service.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.service.dto.response.ChannelDto;
import com.sprint.mission.discodeit.service.mapper.ChannelMapper;
import com.sprint.mission.discodeit.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class ChannelService {

    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final ReadStatusRepository readStatusRepository;

    private final ChannelMapper mapper;
    private final UserMapper userMapper;

    @Transactional
    public ChannelDto createPublicChannel(PublicChannelCreateRequest request) {
        log.info("ChannelService.createPublicChannel");
        Channel channel = new Channel(
                request.name(),
                request.description(),
                ChannelType.PUBLIC);

        Channel save = channelRepository.save(channel);

        List<ReadStatus> readList = new ArrayList<>();
        userRepository.findAll()
                .forEach(user -> {
                    ReadStatus readStatus = new ReadStatus(user, save, Instant.now());
                    readList.add(readStatus);
                });
        readStatusRepository.saveAll(readList);
        return mapper.toDto(save);
    }

    @Transactional
    public ChannelDto createPrivateChannel(PrivateChannelCreateRequest request) {
        log.info("ChannelService.createPrivateChannel");
        Channel channel = new Channel("dm","description",ChannelType.PRIVATE);
        Channel save = channelRepository.save(channel);

        List<User> users = userRepository.findAllById(request.participantIds());
        List<ReadStatus> result = new ArrayList<>();
        for (User user : users) {
            ReadStatus readStatus = new ReadStatus(user, save, Instant.now());
            result.add(readStatus);
        }

        readStatusRepository.saveAll(result);

        return mapper.toDto(save);
    }

    @Transactional
    public ChannelDto updateChannel(UUID channelId, PublicChannelUpdateRequest requestDto) {
        log.info("ChannelService.updateChannel");
        Channel channel = channelRepository.findById(channelId).orElseThrow(() -> new NoSuchElementException("해당 채널이 없습니다"));
        if (requestDto.newName() != null) {
            channel.updateName(requestDto.newName());
        }
        if(requestDto.newDescription()!=null){
            channel.updateDescription(requestDto.newDescription());
        }
        return mapper.toDto(channel);
    }

    @Transactional
    public void deleteChannel(UUID id) {
        log.info("ChannelService.deleteChannel");
        Channel channel = channelRepository.findById(id).orElseThrow(() -> new NoSuchElementException("해당 채널이 없습니다"));
        channelRepository.delete(channel);
    }

    @Transactional(readOnly = true)
    public List<ChannelDto> getAllByUser(UUID userId) {

        List<ChannelDto> list = readStatusRepository.findAllByUser_Id(userId)
                .stream()
                .map(readStatus -> {
                    if(readStatus.getChannel().getType()==ChannelType.PUBLIC){
                        return mapper.toDto(readStatus.getChannel());
                    } else {
                        List<ReadStatus> allByChannelId = readStatusRepository.findAllByChannel_Id(readStatus.getChannel().getId());
                        ChannelDto dto = mapper.toDto(readStatus.getChannel());
                        for (ReadStatus status : allByChannelId) {
                            dto.getParticipants().add(userMapper.toDto(status.getUser()));
                        }
                        return dto;
                    }

                }).toList();

        return list;

    }


}
