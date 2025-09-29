package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.service.*;

import java.util.*;

/**
 * MessageService의 JCF(Java Collections Framework) 기반 구현체
 * - Message를 메모리에 저장
 * - Message 생성 시 User/Channel의 존재 여부 검증
 */
public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> data;        // Message 저장소
    private final UserService userService;        // UserService 참조
    private final ChannelService channelService;  // ChannelService 참조

    public JCFMessageService(UserService userService, ChannelService channelService) {
        this.data = new HashMap<>();
        this.userService = userService;
        this.channelService = channelService;
    }

    /**
     * Message 생성 (등록)
     * - User, Channel이 실제 존재하는지 검증
     */
    @Override
    public Message create(Message message) {
        if (userService.read(message.getSender().getId()) == null) {
            throw new IllegalArgumentException("Message 생성 실패: 유효하지 않은 User");
        }
        if (channelService.read(message.getChannel().getId()) == null) {
            throw new IllegalArgumentException("Message 생성 실패: 유효하지 않은 Channel");
        }

        data.put(message.getId(), message);
        return message;
    }

    /**
     * Message 단건 조회
     */
    @Override
    public Message read(UUID id) {
        return data.get(id);
    }

    /**
     * 전체 Message 조회
     */
    @Override
    public List<Message> readAll() {
        return new ArrayList<>(data.values());
    }

    /**
     * Message 수정
     */
    @Override
    public Message update(UUID id, Message message) {
        if (data.containsKey(id)) {
            data.put(id, message);
            return message;
        }
        return null;
    }

    /**
     * Message 삭제
     */
    @Override
    public boolean delete(UUID id) {
        return data.remove(id) != null;
    }
}