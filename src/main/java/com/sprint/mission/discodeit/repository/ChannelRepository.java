package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * ChannelRepository
 * -----------------
 * 채널 데이터의 저장/조회/삭제를 담당하는 저장소 계층 인터페이스입니다.
 * (실제 데이터 저장소는 메모리, DB 등 다양하게 구현될 수 있습니다.)
 */
public interface ChannelRepository {
    /**
     * 채널 저장
     * @param channel 저장할 Channel 객체
     */
    void save(Channel channel);

    /**
     * UUID로 채널 조회
     */
    Optional<Channel> findById(UUID id);

    /**
     * 사용자로 채널 조회
     */
    List<Channel> findByUser(UUID userId);

    /**
     * 채널 타입으로 조회
     */
    List<Channel> findByType(ChannelType type);

    /**
     * 전체 채널 조회
     */
    List<Channel> findAll();

    void update(Channel channel);

    /**
     * 채널 삭제
     */
    void deleteById(UUID channelId);

    /**
     * 사용자가 속한 채널 UUID 추가
     */
    void addChannelIdForUser(UUID channelId, UUID userId);

    /**
     * 사용자가 속한 채널 UUID 삭제
     */
    void deleteChannelIdForUser(UUID channelId, UUID userId);

    /**
     * 채널에서 멤버 삭제
     */
    void deleteMember(Channel channel, UUID targetId);

    /**
     * 채널 이름 중복 확인
     */
    boolean existsByName(String name);

    boolean existsById(UUID channelId);

    boolean isUserJoinedChannel(UUID userId, UUID channelId);

    boolean isExist(UUID receiverId);
}
