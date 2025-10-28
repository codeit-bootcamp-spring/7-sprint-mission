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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;

    public BasicChannelService(@Qualifier("JCFCannel") ChannelRepository channelRepository
                              , @Qualifier("JCFReadStatus") ReadStatusRepository readStatusRepository
                               ,@Qualifier("JCFReadStatus") MessageRepository messageRepository
    ) {
        this.channelRepository = channelRepository;
        this.readStatusRepository = readStatusRepository;
    }


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
        List<Channel> channels = channelRepository.findAll();
        // 2) 모든 메시지 → 채널별 최신 시간 맵
        Map<UUID, Instant> latestTimeByChannel = messageRepository.findAll().stream()
                .collect(Collectors.toMap(
                        Message::getChannelId,
                        Message::getTime,
                        // 같은 채널이면 더 최신(큰) 시간으로 병합
                        (t1, t2) -> t1.isAfter(t2) ? t1 : t2
                ));
    }

    @Override
    public List<ChannelFindResponse> findAllByUserId(UUID userId) {
        return List.of();
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
