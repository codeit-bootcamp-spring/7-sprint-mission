package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.request.*;
import com.sprint.mission.discodeit.dto.channel.response.ChannelDto;
import com.sprint.mission.discodeit.dto.archive.response.ChannelResponseDto;
import com.sprint.mission.discodeit.entity.Channel;
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
    Channel create(CreatePublicChannelRequestDto request);
    Channel create(CreatePrivateChannelRequestDto request);

    Channel update(UUID channelId, UpdatePublicChannelRequestDto request);

    /**
     * 채널에 멤버 추가
     */
    void addMember(UUID channelId, UpdateChannelRequestDto request);

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
    List<ChannelDto>  findAllByUserId(UUID userId);

    /**
     * 채널 삭제 (관리자 권한 필요)
     */
    void delete(UUID channelId);

    /**
     * 채널 멤버 삭제
     */
    void deleteChannelMember(UUID channelId, UUID requesterId, UUID targetId);

    boolean isChannelUnavailableForUser(UUID userId, UUID channelId);
}
