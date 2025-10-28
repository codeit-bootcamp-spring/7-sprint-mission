package com.sprint.mission.discodeit.service.jcf.basic;

import com.sprint.mission.discodeit.dto.channel.request.ChannelCreatePrivateRequest;
import com.sprint.mission.discodeit.dto.channel.request.ChannelCreatePublicRequest;
import com.sprint.mission.discodeit.dto.channel.response.ChannelCreatePrivateResponse;
import com.sprint.mission.discodeit.dto.channel.response.ChannelCreatePublicResponse;
import com.sprint.mission.discodeit.dto.channel.response.ChannelFindResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
@RequiredArgsConstructor
@Service
public class BasicChannelService implements ChannelService {

    @Qualifier("JCFCannel")
    private final ChannelRepository channelRepository;
    @Qualifier("JCFReadStatus")
    private final ReadStatusRepository readStatusRepository;
    @Qualifier("JCFMessage")
    private final MessageRepository messageRepository;

/*    public BasicChannelService(@Qualifier("JCFCannel") ChannelRepository channelRepository
                              , @Qualifier("JCFReadStatus") ReadStatusRepository readStatusRepository
                               ,@Qualifier("JCFMessage") MessageRepository messageRepository
    ) {
        this.channelRepository = channelRepository;
        this.readStatusRepository = readStatusRepository;
        this.messageRepository = messageRepository;
    }*/


    @Override
    public ChannelCreatePrivateResponse createPrivateChannel(ChannelCreatePrivateRequest request) {
        if(request.channelType() != ChannelType.PRIVATE) {
            throw  new IllegalArgumentException("아니 타입이 다르잖아요");
        }
        //channel 생성
        Channel channel = new Channel(request.bose(),request.chennalName(),request.channelType(),request.description());
        //readtatus 생성
        readStatusRepository.save(request.bose(),channel.getId());
        //채널저장
        channelRepository.save(channel);
        return ChannelCreatePrivateResponse.from(channel);
    }

    @Override
    public ChannelCreatePublicResponse createPublicChannel(ChannelCreatePublicRequest request) {
        if(request.channelType() != ChannelType.PUBLIC) {
            throw  new IllegalArgumentException("아니 타입이 다르잖아요");
        }
        Channel channel = new Channel(request.bose(),request.chennalName(),request.channelType(),request.description());
        channelRepository.save(channel);
        return ChannelCreatePublicResponse.from(channel);
    }

    @Override
    public ChannelFindResponse find(UUID channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("채널을 찾을수가 없어" + channelId));

        //최신시간 가지고올꺼야
        Instant instant = messageRepository.findAll().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .map(Message::getTime)
                .max(Comparator.naturalOrder())
                .orElseThrow(() -> new NoSuchElementException("아니 시간이없네"));
       //너 private니?
        if(channel.getType() != ChannelType.PRIVATE) {
            return ChannelFindResponse.from(channel, instant);
        }
        //아니구나
        return ChannelFindResponse.from(channel, instant).isPrivate(channel);

    }

    @Override
    public List<ChannelFindResponse> findAll() {
        return channelRepository.findAll().stream()
                .filter(channel -> channel.getType() == ChannelType.PUBLIC)
                .map(channel -> {
                    Instant latestTime = messageRepository.findAll().stream()
                            .filter(msg -> msg.getChannelId().equals(channel.getId()))
                            .map(Message::getTime)
                            .max(Comparator.naturalOrder())
                            .orElseThrow(() -> new NoSuchElementException("매새지없는거아니야?"));
                    return ChannelFindResponse.from(channel, latestTime);
                })
                .toList();
    }

    @Override
    public List<ChannelFindResponse> findAllByUserId(UUID userId) {
        return channelRepository.findAll().stream()
                .filter(channel ->
                        channel.getType() == ChannelType.PUBLIC || channel.getUsers().contains(userId)
                )
                .map(channel -> {
                    Instant latest = messageRepository.findAll().stream()
                            .filter(msg -> msg.getChannelId().equals(channel.getId()))
                            .map(Message::getTime)
                            .max(Comparator.naturalOrder())
                            .orElse(null);

                    if (channel.getType() == ChannelType.PRIVATE) {
                        return ChannelFindResponse.from(channel, latest).isPrivate(channel);
                    }
                    return ChannelFindResponse.from(channel, latest);
                })
                .toList();
    }

    @Override
    public Channel update(UUID channelId,String newChannelName, UUID newBose, List<UUID> newUsers) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new NoSuchElementException("채널을 찾을수가 없어 " + channelId ));
        channel.update(newChannelName, newBose,newUsers);
        return channelRepository.save(channel);
    }

    @Override
    public void delete(UUID channelId) {
        if (!channelRepository.existsById(channelId)) {
            throw new NoSuchElementException("Channel with id " + channelId + " not found");
        }
        channelRepository.deleteById(channelId);
    }




}
