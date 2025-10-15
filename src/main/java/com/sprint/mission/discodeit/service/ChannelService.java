package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    // C: 채널 생성 (필수값인 이름만 받음)
    Channel create(String channelName);

    // R: 단건 조회
    Channel read(UUID id);
    // R: 전체 조회
    List<Channel> readAll();

    // U: 채널 이름 수정
    Channel updateChannelName(UUID id, String newName);

    // U: 채널 토픽 수정/설정
    Channel updateChannelTopic(UUID id, String newTopic);

    // D  ← CRUD 충족을 위해 반드시 선언
    boolean delete(UUID id);
}
