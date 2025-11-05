package com.sprint.mission.discodeit.channel;

import com.sprint.mission.discodeit.channel.dto.ChannelRequestDTO;
import com.sprint.mission.discodeit.channel.dto.ChannelResponseDTO;
import com.sprint.mission.discodeit.common.service.BaseService;
import com.sprint.mission.discodeit.config.enums.ChannelType;

import java.util.List;
import java.util.UUID;

/**
 * 채널(Channel) 도메인의 비즈니스 로직을 처리하는 서비스 인터페이스입니다.
 * 공통 CRUD 기능은 BaseService로부터 상속받습니다.
 */
public interface ChannelService extends BaseService<Channel, UUID> {

    ChannelResponseDTO create(ChannelRequestDTO requestDTO);

    void changeSettings(UUID channelId,ChannelRequestDTO requestDTO);

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
    ChannelResponseDTO findByChannelName(String channelName);

    List<ChannelResponseDTO> findAllChannelsBySettings(ChannelRequestDTO requestDTO);

    /**
     * 채널 이름이 시스템 내에서 존재하는지 확인합니다.
     *
     * @param channelName 확인할 채널 이름
     * @return 존재하면 true, 그렇지 않으면 false
     *
     * **위 조건에서 논리적(!isDelete) 삭제가 되지않은 데이터만 반환합니다.
     */
    boolean isChannelNameExistNonDel(String channelName);

    /**
     * 채널 이름(channelName)으로 특정 채널을 조회합니다.
     * <p>
     * 이 메서드는 채널이 반드시 존재한다고 가정하며, 찾지 못할 경우 예외를 발생시킵니다.
     *
     * @param channelName 조회할 채널의 고유한 이름 (필수)
     * @return 조회된 Channel 객체
     * @throws java.util.NoSuchElementException 해당 이름의 채널이 존재하지 않을 경우
     *                                          <p>
     *                                          **위 조건에서 논리적(!isDelete) 삭제가 되지않은 데이터만 반환합니다.
     */
    ChannelResponseDTO findByChannelNameNonDel(String channelName);

    List<ChannelResponseDTO> findAllChannelsBySettingsNonDel(ChannelRequestDTO requestDTO);
}