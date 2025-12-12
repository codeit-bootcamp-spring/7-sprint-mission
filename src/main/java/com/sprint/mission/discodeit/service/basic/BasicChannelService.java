package com.sprint.mission.discodeit.service.basic;


import com.sprint.mission.discodeit.dto.request.channel.ChannelPatchRequestDto;
import com.sprint.mission.discodeit.dto.request.channel.ChannelPrivateCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.channel.ChannelPublicCreateRequestDto;
import com.sprint.mission.discodeit.dto.response.channel.ChannelDto;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.entityElement.ChannelType;
import com.sprint.mission.discodeit.exception.domain.channel.ChannelNotExistException;
import com.sprint.mission.discodeit.exception.domain.user.UserNotExistException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.sprint.mission.discodeit.service.util.StaticString.USER_NOT_EXIST;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final ReadStatusRepository readStatusRepository;
    private final ChannelMapper channelMapper;


    @Override
    public List<ChannelDto> readAllChannel() {
        return channelRepository.findAll().stream().map(x->channelMapper.toDto(x)).toList();
    }



    @Override
    @Transactional
    public ChannelDto createPrivateChannel(ChannelPrivateCreateRequestDto channelPrivateCreateRequestDto) {

        Channel channel = channelRepository.save(Channel.privateChannelFactory(channelPrivateCreateRequestDto.name(),channelPrivateCreateRequestDto.description()));
       channelPrivateCreateRequestDto.participantIds().forEach(
                x ->
                {
                    User tempUser = userRepository.findById(x).orElseThrow(()->new UserNotExistException(x));
                   readStatusRepository.save(ReadStatus.createReadStatusFactory(tempUser,channel));
                }
        );
        return channelMapper.toDto(channel);
    }

    @Override
    @Transactional
    public ChannelDto createPublicChannel(ChannelPublicCreateRequestDto channelPublicCreateRequestDto) {

    Channel channel = channelRepository.save(Channel.publicChannelFactory(
            channelPublicCreateRequestDto.name(),
            channelPublicCreateRequestDto.description()
    ));
        List<User> users = userRepository.findAll();
        users.forEach(x->
            readStatusRepository.save(ReadStatus.createReadStatusFactory(x,channel)
        )
        );
        log.info("channel : {} ",channel);
    return channelMapper.toDto(channel);
    }


    @Override
    public void createChannel(Channel channel) {
        channelRepository.save(channel);

    }

    @Override
    @Transactional
    public ChannelDto readChannel(UUID channelId) {
        Channel expectedChannel = channelRepository.findById(channelId).orElseThrow(()->new UserNotExistException(channelId));
        return channelMapper.toDto(expectedChannel);
    }



    @Override
    @Transactional(readOnly = true)
    public List<ChannelDto> findAllByUserId(UUID userId) {
        List<Channel> channelList = channelRepository.findAll();
        List<ReadStatus> readStatusList = readStatusRepository.findAll();
        List<Channel> publicChannelList = channelList.stream().filter(x->
                x.getType()== ChannelType.PUBLIC).collect(Collectors.toList());

        List<Channel> userContainPrivateChannel = readStatusList.stream().filter(
                x->x.getUser().getId().equals(userId) && x.getChannel().getType()== ChannelType.PRIVATE
        ).map(ReadStatus::getChannel).collect(Collectors.toList());

        return Stream.concat(publicChannelList.stream(),userContainPrivateChannel.stream())
                .map(channelMapper::toDto
                        ).toList();
    }

    @Override
    public void deleteChannel(UUID channelID) {

        log.warn("delete channel : {}",channelID);
        channelRepository.deleteById(channelID);
    }

    @Override
    @Transactional
    public ChannelDto patchChannel(ChannelPatchRequestDto dto, UUID channelId) {
        Channel channel = channelRepository.findById(channelId).orElseThrow(()->new ChannelNotExistException(channelId));
        channel.setDescription(dto.newDescription()==null?channel.getDescription():dto.newDescription());
        channel.setName(dto.newName()==null?channel.getName():dto.newName());
        channelRepository.save(channel);
        log.info("updated channel : {}",channel);
        return channelMapper.toDto(channel);
    }

    @Override
    public void resetChannelRepository() {
        channelRepository.deleteAll();
    }
}
