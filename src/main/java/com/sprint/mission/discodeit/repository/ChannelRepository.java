package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
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
    List<Channel> findByUser(User user);

    /**
     * 채널 타입으로 조회
     */
    List<Channel> findByType(ChannelType type);

    /**
     * 전체 채널 조회
     */
    List<Channel> findAll();

    /**
     * 채널 이름 업데이트
     */
    void updateName(UUID id, String name);

    /**
     * 채널 관리자 업데이트
     */
    void updateAdmin(UUID id, User admin);

    /**
     * 채널 삭제
     */
    void deleteById(UUID id);

    /**
     * 사용자가 속한 채널 UUID 추가
     */
    void addChannelIdForUser(UUID channelId, User user);

    /**
     * 사용자가 속한 채널 UUID 삭제
     */
    void deleteChannelIdForUser(UUID channelId, User user);

    /**
     * 채널에서 멤버 삭제
     */
    void deleteMember(Channel channel, User target);
}
