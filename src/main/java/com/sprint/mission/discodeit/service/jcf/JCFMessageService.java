package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.*;

/**
 * ✅ JCFMessageService
 * - MessageService 인터페이스의 구현체
 * - Java Collections Framework(Map)를 사용해 메시지를 메모리에 저장/조회/수정/삭제한다.
 *
 * 🔹 저장 구조
 *   - key: UUID (Message의 고유 식별자)
 *   - value: Message 객체
 *
 * 🔹 책임
 *   - create(senderId, channelId, content)
 *   - read(id), readAll()
 *   - updateContent(id, newContent)
 *   - delete(id)
 *
 * ⚠️ 주의
 *   - 이 기본 구현은 "연관 엔티티 존재 여부 검증"을 하지 않는다.
 *   - 검증이 필요하다면 아래 3) 확장 버전(의존성 주입) 구현을 사용하자.
 */
public class JCFMessageService implements MessageService {

    /**
     * 메모리 저장소: id → Message
     * final: 다른 Map 인스턴스로 교체하지 못하게(안정성)
     */
    private final Map<UUID, Message> data;

    /**
     * 기본 생성자
     * - LinkedHashMap: 입력(생성) 순서를 유지하여 readAll() 결과가 보기 좋다.
     */
    public JCFMessageService() {
        this.data = new LinkedHashMap<>();
    }

    /**
     * CREATE
     * - 새 Message를 생성해서 저장소에 넣는다.
     * @param senderId  메시지 작성자(User)의 UUID
     * @param channelId 메시지가 속한 Channel의 UUID
     * @param content   메시지 내용(빈 문자열 금지 권장)
     * @return 생성된 Message
     */
    @Override
    public Message create(UUID senderId, UUID channelId, String content) {
        if (senderId == null) {
            throw new IllegalArgumentException("senderId는 null일 수 없습니다.");
        }
        if (channelId == null) {
            throw new IllegalArgumentException("channelId는 null일 수 없습니다.");
        }
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("content는 비어있을 수 없습니다.");
        }

        Message msg = new Message(senderId, channelId, content);
        data.put(msg.getId(), msg);
        return msg;
    }

    /**
     * READ (단건)
     * @param id Message의 UUID
     * @return 해당 메시지 (없으면 null)
     */
    @Override
    public Message read(UUID id) {
        return data.get(id);
    }

    /**
     * READ ALL (전체)
     * @return 저장된 모든 메시지 목록
     */
    @Override
    public List<Message> readAll() {
        return new ArrayList<>(data.values());
    }

    /**
     * UPDATE (내용 수정)
     * - Message 엔티티의 도메인 메서드(updateContent)를 호출한다.
     *   내부에서 DefEntity.touch()가 호출되어 updatedAt이 갱신된다.
     * @param id         수정할 메시지의 UUID
     * @param newContent 새 내용
     * @return 수정된 메시지 (없으면 null)
     */
    @Override
    public Message updateContent(UUID id, String newContent) {
        if (newContent == null || newContent.isBlank()) {
            throw new IllegalArgumentException("newContent는 비어있을 수 없습니다.");
        }
        Message m = data.get(id);
        if (m != null) {
            m.updateContent(newContent);
        }
        return m;
    }

    /**
     * DELETE
     * @param id 삭제할 메시지의 UUID
     * @return 삭제 성공 여부 (true: 삭제됨, false: 존재하지 않음)
     */
    @Override
    public boolean delete(UUID id) {
        return data.remove(id) != null;
    }
}