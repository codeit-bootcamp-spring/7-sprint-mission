package com.sprint.mission.discodeit.channel;

import com.sprint.mission.discodeit.common.service.impl.BaseServiceImpl;
import com.sprint.mission.discodeit.config.enums.ChannelType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class ChannelServiceImpl extends BaseServiceImpl<Channel, UUID, ChannelRepository> implements ChannelService {


    /**
     * 생성자를 통해 의존성 주입(DI)을 받습니다.
     *
     * @param channelRepository     채널 데이터 처리를 위한 리포지토리
     */
    public ChannelServiceImpl(ChannelRepository channelRepository) {
        super(channelRepository);
    }

    @Override
    public Channel create(String channelName, ChannelType channelType, String topic, boolean isPrivate) {
        // 서비스의 핵심 책임: 비즈니스 규칙(채널 이름 중복 불가) 검증
        if (isChannelNameExist(channelName)) {
            throw new IllegalStateException("이미 존재하는 채널 이름입니다: " + channelName);
        }
        // 실제 객체 생성 책임은 Channel 엔티티의 정적 팩토리 메서드에 위임 (가정)
        Channel newChannel = Channel.create(channelName, channelType, topic, isPrivate);
        save(newChannel);
        return newChannel;
    }

    @Override
    public void changeSettings(UUID channelId, String channelName, ChannelType channelType, String topic, Boolean isPrivate) {
        Channel channel = findById(channelId);

        // 만약 이름이 변경되는 경우라면, 이름 중복 체크를 수행합니다.
        if (channelName != null && !channel.getChannelName().equals(channelName) && isChannelNameExist(channelName)) {
            throw new IllegalStateException("이미 존재하는 채널 이름입니다: " + channelName);
        }
        if (channel.isDeleted()){
            throw new IllegalStateException("삭제된 채널입니다.");
        }

        // 실제 변경 로직은 엔티티에게 위임합니다.
        channel.changeSettings(channelName, channelType, topic, isPrivate);

        save(channel);
    }

    @Override
    public boolean isChannelNameExist(String channelName) {
        return repository.existsByChannelName(channelName);
    }

    @Override
    public Channel findByChannelName(String channelName) {
        // Repository의 조회 결과를 받아 처리합니다.
        // Optional<Channel>을 받습니다.
        return repository.findByChannelName(channelName)
                // 결과가 비어있을 경우(채널이 없을 경우), 어떤 채널을 찾지 못했는지
                // 명확한 메시지와 함께 NoSuchElementException을 발생시킵니다.
                .orElseThrow(() -> new NoSuchElementException("해당 이름의 채널을 찾을 수 없습니다: " + channelName));
    }

    @Override
    public List<Channel> findAllChannelsBySettings(String channelName, ChannelType channelType, String topic) {
        // Repository의 조회 결과를 받아 처리합니다.
        // List<Channel>을 받고 비어있는 상태는 다음 계층에서 관리합니다.
        return repository.findAllChannelsBySettings(channelName, channelType, topic);
    }

    @Override
    public boolean isChannelNameExistNonDel(String channelName) {
        return repository.existsByChannelNameNonDel(channelName);
    }

    @Override
    public Channel findByChannelNameNonDel(String channelName) {
        // Repository의 조회 결과를 받아 처리합니다.
        // Optional<Channel>을 받습니다.
        return repository.findByChannelNameNonDel(channelName)
                // 결과가 비어있을 경우(채널이 없을 경우), 어떤 채널을 찾지 못했는지
                // 명확한 메시지와 함께 NoSuchElementException을 발생시킵니다.
                .orElseThrow(() -> new NoSuchElementException("해당 이름의 채널을 찾을 수 없습니다: " + channelName));
    }

    @Override
    public List<Channel> findAllChannelsBySettingsNonDel(String channelName, ChannelType channelType, String topic) {
        // Repository의 조회 결과를 받아 처리합니다.
        // List<Channel>을 받고 비어있는 상태는 다음 계층에서 관리합니다.
        return repository.findAllChannelsBySettingsNonDel(channelName, channelType, topic);
    }
}