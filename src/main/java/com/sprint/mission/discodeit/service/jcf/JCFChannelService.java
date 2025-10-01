package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

/**
 * ChannelService 인터페이스의 메모리(JCF) 기반 구현체입니다.
 */
public class JCFChannelService extends JCFBaseService<Channel, UUID, ChannelRepository> implements ChannelService {

    private final ChannelRepository channelRepository;

    /**
     * 생성자를 통해 의존성 주입(DI)을 받습니다.
     *
     * @param channelRepository     채널 데이터 처리를 위한 리포지토리
     */
    public JCFChannelService(ChannelRepository channelRepository) {
        super(channelRepository);
        this.channelRepository = channelRepository;
    }

    @Override
    public Channel create(String channelName, ChannelType channelType, String topic, boolean isPrivate) {
        // 서비스의 핵심 책임: 비즈니스 규칙(채널 이름 중복 불가) 검증
        if (isChannelNameExist(channelName)) {
            throw new IllegalStateException("이미 존재하는 채널 이름입니다: " + channelName);
        }
        // 실제 객체 생성 책임은 Channel 엔티티의 정적 팩토리 메서드에 위임 (가정)
        Channel newChannel = Channel.create(channelName, channelType, topic, isPrivate);
        channelRepository.save(newChannel);
        return newChannel;
    }

    @Override
    public void changeSettings(UUID channelId, String channelName, ChannelType channelType, String topic, Boolean isPrivate) {
        Channel channel = findById(channelId);

        // 만약 이름이 변경되는 경우라면, 이름 중복 체크를 수행합니다.
        if (channelName != null && !channel.getChannelName().equals(channelName) && isChannelNameExist(channelName)) {
            throw new IllegalStateException("이미 존재하는 채널 이름입니다: " + channelName);
        }

        // 실제 변경 로직은 엔티티에게 위임합니다.
        channel.changeSettings(channelName, channelType, topic, isPrivate);

        channelRepository.save(channel);
    }

    @Override
    public boolean isChannelNameExist(String channelName) {
        return channelRepository.existsByChannelName(channelName);
    }

    @Override
    public Channel findByChannelName(String channelName) {
        // Repository의 조회 결과를 받아 처리합니다.
        // Optional<Channel>을 받습니다.
        return channelRepository.findByChannelName(channelName)
                // 결과가 비어있을 경우(채널이 없을 경우), 어떤 채널을 찾지 못했는지
                // 명확한 메시지와 함께 NoSuchElementException을 발생시킵니다.
                .orElseThrow(() -> new NoSuchElementException("해당 이름의 채널을 찾을 수 없습니다: " + channelName));
    }

    @Override
    public List<Channel> findAllChannelsBySettings(String channelName, ChannelType channelType, String topic) {
        // Repository의 조회 결과를 받아 처리합니다.
        // List<Channel>을 받고 비어있는 상태는 다음 계층에서 관리합니다.
        return channelRepository.findAllChannelsBySettings(channelName, channelType, topic);
    }
}