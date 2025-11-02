package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.request.CreateChannelRequestDto;
import com.sprint.mission.discodeit.dto.channel.request.UpdateChannelRequestDto;
import com.sprint.mission.discodeit.dto.channel.request.UpdateChannelNameRequestDto;
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
    ChannelResponseDto createPublic(CreateChannelRequestDto request);
    PrivateChannelResponseDto createPrivate(CreateChannelRequestDto request);

    /**
     * 채널에 멤버 추가
     */
    void addMember(UpdateChannelRequestDto request);

    /**
     * UUID로 채널 조회
     */
    ChannelResponseDto find(UUID channelId);

    /**
     * 사용자가 속한 채널 조회
     */
    List<ChannelResponseDto> findPrivateByUserId(UUID userId);

    /**
     * 채널 타입으로 조회
     */
    List<ChannelResponseDto> findByType(ChannelType channelType);

    /**
     * 전체 채널 조회
     */
    List<ChannelResponseDto> findAllByUserId(UUID userId);

    /**
     * 채널 관리자 변경
     */
    void updateAdmin(UpdateChannelRequestDto request);

    /**
     * 채널 이름 변경
     */
    void updateName(UpdateChannelNameRequestDto request);

    /**
     * 채널 삭제 (관리자 권한 필요)
     */
    void delete(UUID channelId, UUID userId);

    /**
     * 채널 멤버 삭제
     */
    void deleteChannelMember(UUID channelId, UUID requesterId, UUID targetId);

    boolean isChannelUnavailableForUser(UUID userId, UUID channelId);
}
