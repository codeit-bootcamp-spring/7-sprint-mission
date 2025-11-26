package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.request.*;
import com.sprint.mission.discodeit.dto.channel.response.ChannelDto;
import com.sprint.mission.discodeit.dto.archive.response.ChannelResponseDto;
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
    ChannelDto create(CreatePublicChannelRequestDto request);
    ChannelDto create(CreatePrivateChannelRequestDto request);

    ChannelDto update(UUID channelId, UpdatePublicChannelRequestDto request);

    /**
     * UUID로 채널 조회
     */
    ChannelDto find(UUID channelId);

    /**
     * 전체 채널 조회
     */
    List<ChannelDto>  findAllByUserId(UUID userId);

    /**
     * 채널 삭제 (관리자 권한 필요)
     */
    void delete(UUID channelId);
}
