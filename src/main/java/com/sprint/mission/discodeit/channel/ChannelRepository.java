package com.sprint.mission.discodeit.channel;

import com.sprint.mission.discodeit.common.repository.BaseRepository;
import com.sprint.mission.discodeit.config.enums.ChannelType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Channel 엔티티의 영속성을 처리하기 위한 Repository 인터페이스입니다.
 * 공통 CRUD 기능은 BaseRepository로부터 상속받고, Channel 고유의 조회 기능을 추가로 정의합니다.
 */
public interface ChannelRepository extends BaseRepository<Channel, UUID> {

    /**
     * 채널 이름(channelName)으로 채널을 조회합니다.
     * 채널 이름은 고유한 값으로 간주됩니다.
     *
     * @param name 조회할 채널의 이름
     * @return 채널이 존재하면 Optional<Channel>로 감싸서 반환하고, 없으면 Optional.empty()를 반환합니다.
     */
    Optional<Channel> findByChannelName(String name);

    /**
     * 해당 채널 이름(channelName)이 이미 존재하는지 확인합니다.
     *
     * @param name 확인할 채널의 이름
     * @return 채널 이름이 존재하면 true, 아니면 false
     */
    boolean existsByChannelName(String name);

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


    /**
     * 채널 이름(channelName)으로 채널을 조회합니다.
     * 채널 이름은 고유한 값으로 간주됩니다.
     *
     * @param name 조회할 채널의 이름
     * @return 채널이 존재하면 Optional<Channel>로 감싸서 반환하고, 없으면 Optional.empty()를 반환합니다.
     * **위 조건에서 논리적(!isDelete) 삭제가 되지않은 데이터만 반환합니다.
     */
    Optional<Channel> findByChannelNameNonDel(String name);

    /**
     * 해당 채널 이름(channelName)이 이미 존재하는지 확인합니다.
     *
     * @param name 확인할 채널의 이름
     * @return 채널 이름이 존재하면 true, 아니면 false
     * **위 조건에서 논리적(!isDelete) 삭제가 되지않은 데이터만 반환합니다.
     */
    boolean existsByChannelNameNonDel(String name);

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
     *  **위 조건에서 논리적(!isDelete) 삭제가 되지않은 데이터만 반환합니다.
     */
    List<Channel> findAllChannelsBySettingsNonDel(String channelName, ChannelType channelType, String topic);
}