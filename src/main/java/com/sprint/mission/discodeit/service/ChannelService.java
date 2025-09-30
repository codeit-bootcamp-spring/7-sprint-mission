package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.List;
import java.util.UUID;

/**
 * 채널(Channel) 도메인의 비즈니스 로직을 처리하는 서비스 인터페이스입니다.
 * 공통 CRUD 기능은 BaseService로부터 상속받습니다.
 */
public interface ChannelService extends BaseService<Channel, UUID> {

    /**
     * 새로운 채널을 생성합니다.
     *
     * @param channelName 채널의 이름 (필수, 고유해야 함)
     * @param channelType 채널의 타입 (DIRECT, GROUP)
     * @param topic       채널의 주제 (선택)
     * @param isPrivate   채널의 공개 여부
     * @return 생성된 Channel 객체
     */
    Channel create(String channelName, ChannelType channelType, String topic, boolean isPrivate);

    /**
     * 채널의 주요 설정(이름, 타입, 주제, 공개여부)을 변경합니다.
     * 변경을 원하지 않는 필드는 null로 전달합니다.
     *
     * @param channelId   변경할 채널의 ID
     * @param channelName 새로운 채널 이름 (변경 시에만 값 전달)
     * @param channelType 새로운 채널 타입 (변경 시에만 값 전달)
     * @param topic       새로운 주제 (변경 시에만 값 전달)
     * @param isPrivate   새로운 공개 여부 (변경 시에만 값 전달)
     */
    void changeSettings(UUID channelId, String channelName, ChannelType channelType, String topic, Boolean isPrivate);

    /**
     * 채널 이름이 시스템 내에서 존재하는지 확인합니다.
     *
     * @param channelName 확인할 채널 이름
     * @return 존재하면 true, 그렇지 않으면 false
     */
    boolean isChannelNameExist(String channelName);

    /**
     * 채널 이름(channelName)으로 특정 채널을 조회합니다.
     * <p>
     * 이 메서드는 채널이 반드시 존재한다고 가정하며, 찾지 못할 경우 예외를 발생시킵니다.
     *
     * @param channelName 조회할 채널의 고유한 이름 (필수)
     * @return 조회된 Channel 객체
     * @throws java.util.NoSuchElementException 해당 이름의 채널이 존재하지 않을 경우
     */
    Channel findByChannelName(String channelName);

    /**
     * 여러 검색 조건(설정)에 맞는 채널 목록을 조회합니다.
     * <p>
     * 모든 파라미터는 선택적(optional)이며, null이 아닌 값만 검색 조건으로 사용됩니다.
     * 모든 파라미터가 null일 경우, 모든 채널이 반환될 수 있습니다.
     *
     * @param channelName 검색할 채널 이름 (부분 또는 전체 일치, 선택 사항)
     * @param channelType 검색할 채널 타입 (선택 사항)
     * @param topic       검색할 채널 주제 (부분 또는 전체 일치, 선택 사항)
     * @return 검색 조건에 맞는 채널의 List. 결과가 없으면 빈 리스트를 반환합니다.
     */
    List<Channel> findAllChannelsBySettings(String channelName, ChannelType channelType, String topic);
}