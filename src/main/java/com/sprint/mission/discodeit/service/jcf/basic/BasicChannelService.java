package com.sprint.mission.discodeit.service.jcf.basic;

import com.sprint.mission.discodeit.dto.channel.request.ChannelCreatePrivateRequest;
import com.sprint.mission.discodeit.dto.channel.request.ChannelCreatePublicRequest;
import com.sprint.mission.discodeit.dto.channel.request.ChannelUpdateRequest;
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


    private final ChannelRepository channelRepository;
    private final ReadStatusRepository readStatusRepository;
    private final MessageRepository messageRepository;



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
        //모든채널보고
        return channelRepository.findAll().stream()
                //일단public은전체 private는 user만조회한거니 유저id에맞거나 public이거나
                //둘중하나보여주니 필터로 (유저가속한채널,퍼블릭채널)->전부다
                .filter(channel ->
                        channel.getType() == ChannelType.PUBLIC || channel.getUsers().contains(userId)
                )
                .map(channel -> {
                    //채널달 최근메시지 시간은 각각해주고
                    Instant latest = messageRepository.findAll().stream()
                            .filter(msg -> msg.getChannelId().equals(channel.getId()))
                            .map(Message::getTime)
                            .max(Comparator.naturalOrder())
                            .orElse(null);
                    //프라이빗이냐 이건 유저가 참여한 프라이빗만 있다
                    if (channel.getType() == ChannelType.PRIVATE) {
                        return ChannelFindResponse.from(channel, latest).isPrivate(channel);
                    }
                    //퍼블릭은 죄다 있다
                    return ChannelFindResponse.from(channel, latest);
                })
                .toList();
    }

    @Override
    public Channel update(ChannelUpdateRequest request) {
        Channel channel = channelRepository.findById(request.channelId())
                .orElseThrow(() -> new NoSuchElementException("채널을 찾을수가 없어 " + request.channelId() ));

        //채널이 퍼블릭이면 수정해서 넣어주고
        if(channel.getType() != ChannelType.PRIVATE) {
            //수정해서
            channel.update(request.newChannelName(), request.newBose(),request.newUsers(),request.newDescription());
            //저장동시 리턴값 수정한 채널
            return channelRepository.save(channel);
        }
        //아니야 프라이빗이야 그냥 수정전채널
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




}
