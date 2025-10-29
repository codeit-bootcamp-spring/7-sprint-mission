package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.request.CreateChannelRequestDto;
import com.sprint.mission.discodeit.dto.channel.request.UpdateChannelRequestDto;
import com.sprint.mission.discodeit.dto.channel.response.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.response.PrivateChannelResponseDto;
import com.sprint.mission.discodeit.entity.ChannelType;

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
    ChannelResponseDto createPublicChannel(CreateChannelRequestDto request);
    PrivateChannelResponseDto createPrivateChannel(CreateChannelRequestDto request);

    /**
     * 채널에 멤버 추가
     */
    void addMember(UpdateChannelRequestDto request);

    /**
     * UUID로 채널 조회
     */
    ChannelResponseDto getChannel(UUID id);

    /**
     * 사용자가 속한 채널 조회
     */
    List<ChannelResponseDto> getChannelByUser(UUID userId);

    /**
     * 채널 타입으로 조회
     */
    List<ChannelResponseDto> getChannelByType(ChannelType channelType);

    /**
     * 전체 채널 조회
     */
    List<ChannelResponseDto> getAllChannels(UUID userId);

    /**
     * 채널 관리자 변경
     */
    void updateAdmin(UpdateChannelRequestDto request);

    /**
     * 채널 이름 변경
     */
    void updateName(UUID channelId, String name);

    /**
     * 채널 삭제 (관리자 권한 필요)
     */
    void deleteChannel(UUID channelId, UUID userId);

    /**
     * 채널 멤버 삭제
     */
    void deleteChannelMember(UUID channelId, UUID requesterId, UUID targetId);

    /**
     * 유저가 채널에 속해있는지 확인
     */
    boolean isUserJoinedChannel(UUID userId, UUID channelId);

    /**
     * 채널 존재 여부 확인
     */
    boolean existsById(UUID channelId);

    /**
     * 채널 이름 중복 확인
     */
    void existsByName(String name);
}
