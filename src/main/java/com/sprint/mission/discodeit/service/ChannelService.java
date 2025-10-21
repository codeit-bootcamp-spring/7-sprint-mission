package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

/**
 * ChannelService 인터페이스
 * - 채널 생성, 조회, 업데이트, 삭제 기능 제공
 */
public interface ChannelService {

    /**
     * 새로운 채널 생성
     */
    Channel createChannel(ChannelType channelType, String channelName, User admin);

    /**
     * 채널에 멤버 추가
     */
    void addMember(UUID id, User member);

    /**
     * UUID로 채널 조회
     */
    Channel getChannel(UUID id);

    /**
     * 사용자가 속한 채널 조회
     */
    List<Channel> getChannelByUser(User user);

    /**
     * 채널 타입으로 조회
     */
    List<Channel> getChannelByType(ChannelType channelType);

    /**
     * 전체 채널 조회
     */
    List<Channel> getAllChannels();

    /**
     * 채널 관리자 변경
     */
    void updateAdmin(UUID id, User user);

    /**
     * 채널 이름 변경
     */
    void updateName(UUID id, String name);

    /**
     * 채널 삭제 (관리자 권한 필요)
     */
    void deleteChannel(UUID id, User user);

    /**
     * 채널 멤버 삭제
     */
    void deleteChannelMember(UUID id, User requester, User target);
}
