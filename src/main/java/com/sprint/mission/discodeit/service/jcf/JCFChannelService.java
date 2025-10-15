package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;

/**
 * ✅ JCFChannelService
 * - ChannelService 인터페이스의 실제 구현체
 * - Java Collections Framework(Map)을 이용해 메모리에 Channel 데이터를 저장한다.
 *
 * 🔹 역할:
 *   - Channel 생성(create)
 *   - 조회(read, readAll)
 *   - 수정(updateChannelName, updateChannelTopic)
 *   - 삭제(delete)
 *
 * 🔹 구조:
 *   - key: UUID (Channel의 고유 식별자)
 *   - value: Channel 객체
 */
public class JCFChannelService implements ChannelService {

    /**
     * Channel 데이터를 메모리에 저장할 저장소
     * final → 재할당 불가능 (데이터 안정성 보장)
     */
    private final Map<UUID, Channel> data;

    /**
     * 기본 생성자
     * - LinkedHashMap을 사용해 입력 순서를 보장함
     *   (readAll() 호출 시, 생성된 순서대로 출력됨)
     */
    public JCFChannelService() {
        this.data = new LinkedHashMap<>();
    }

    /**
     * CREATE
     * - 새로운 Channel을 생성하고 저장소(Map)에 등록한다.
     * @param channelName 생성할 채널 이름 (필수값)
     * @return 생성된 Channel 객체
     */
    @Override
    public Channel create(String channelName) {
        // 예외 처리: 이름이 비어있는 경우 예외 발생
        if (channelName == null || channelName.isBlank()) {
            throw new IllegalArgumentException("channelName은 비어있을 수 없습니다.");
        }

        // Channel 객체 생성 → 엔티티에서 name만 받는 생성자 사용
        Channel channel = new Channel(channelName);

        // Map에 저장 (key: id, value: Channel)
        data.put(channel.getId(), channel);

        // 생성된 Channel 반환
        return channel;
    }

    /**
     * READ (단건 조회)
     * - id(UUID)로 특정 Channel을 조회한다.
     * @param id Channel의 고유 UUID
     * @return 해당 Channel (없으면 null)
     */
    @Override
    public Channel read(UUID id) {
        return data.get(id);
    }

    /**
     * READ ALL (전체 조회)
     * - 저장된 모든 Channel을 리스트 형태로 반환한다.
     * @return 전체 Channel 목록
     */
    @Override
    public List<Channel> readAll() {
        return new ArrayList<>(data.values());
    }

    /**
     * UPDATE (채널 이름 변경)
     * - id로 Channel을 찾아 이름을 변경한다.
     * @param id 변경할 Channel의 UUID
     * @param newName 새 채널 이름
     * @return 수정된 Channel 객체 (없으면 null)
     */
    @Override
    public Channel updateChannelName(UUID id, String newName) {
        if (newName == null || newName.isBlank()) {
            throw new IllegalArgumentException("newName은 비어있을 수 없습니다.");
        }

        // 저장소에서 Channel 찾기
        Channel ch = data.get(id);

        // 존재한다면 엔티티 내부 메서드로 업데이트
        if (ch != null) {
            ch.updateChannelName(newName);
        }

        return ch;
    }

    /**
     * UPDATE (채널 토픽 변경)
     * - id로 Channel을 찾아 토픽을 수정한다.
     * @param id 변경할 Channel의 UUID
     * @param newTopic 새 토픽 내용
     * @return 수정된 Channel 객체 (없으면 null)
     */
    @Override
    public Channel updateChannelTopic(UUID id, String newTopic) {
        Channel ch = data.get(id);
        if (ch != null) {
            ch.updateChannelTopic(newTopic);
        }
        return ch;
    }

    /**
     * DELETE
     * - id로 Channel을 찾아 삭제한다.
     * @param id 삭제할 Channel의 UUID
     * @return 삭제 성공 여부 (true: 성공, false: 실패)
     */
    @Override
    public boolean delete(UUID id) {
        return data.remove(id) != null;
    }
}