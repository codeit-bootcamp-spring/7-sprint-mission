package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.VerifiedUtils;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.*;
/*
    의존성 주입 해야해!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 */
public class JCFMessageService implements MessageService {
    private final Map<UUID, Message> data;

    private JCFMessageService() {
        this.data = new HashMap<>();
    }

    private static final JCFMessageService jcfMessageService = new JCFMessageService();
    public static JCFMessageService getInstance() {
        return jcfMessageService;
    }

    @Override
    public Message create(Message message) {
        Message msg = VerifiedUtils.verifyNull(message);
        UUID id = VerifiedUtils.verifyNull(msg.getId());
        if(data.containsKey(id)) {
            throw new IllegalStateException("Message already exists: " + id);
        }
        data.put(id, msg);
        return msg;
    }

    @Override
    public Message get(UUID uuid) {
        UUID id = VerifiedUtils.verifyNull(uuid);
        Message msg = data.get(id);
        if(msg == null) {
            throw new NoSuchElementException("Message not found: " + id);
        }
        return msg;
    }

    @Override
    public List<Message> getAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Message update(Message message) {
        Message msg = VerifiedUtils.verifyNull(message);
        UUID id = VerifiedUtils.verifyNull(msg.getId());
        if(!data.containsKey(id)) {
            throw new NoSuchElementException("Message not found: " + id);
        }
        data.put(id,msg);
        return msg;
    }

    @Override
    public boolean delete(UUID uuid) {
        UUID id = VerifiedUtils.verifyNull(uuid);
        return data.remove(id) != null;
    }

    // 특정 채널별 메세지 조회
    @Override
    public List<Message> getMessagesByChannel(UUID channelId) {
        UUID id = VerifiedUtils.verifyNull(channelId);
        return data.values()
                .stream()
                .filter(m -> m.getChannelId().equals(id))
                .filter(m -> !m.isDeleted())
                .toList();
    }
    // 특정 작성자별 메세지 조회
    @Override
    public List<Message> getMessagesByAuthor(UUID authorId) {
        UUID id = VerifiedUtils.verifyNull(authorId);
        return data.values()
                .stream()
                .filter(m -> m.getAuthorId().equals(id))
                .filter(m -> !m.isDeleted())
                .toList();
    }
    // 특정 채널의 특정 작성자 메세지 조회
    @Override
    public List<Message> getMessagesByChannelAndAuthor(UUID channelId, UUID authorId) {
        UUID cid = VerifiedUtils.verifyNull(channelId);
        UUID aid = VerifiedUtils.verifyNull(authorId);
        return data.values()
                .stream()
                .filter(m -> m.getChannelId().equals(cid))
                .filter(m -> m.getAuthorId().equals(aid))
                .filter(m -> !m.isDeleted())
                .toList();
    }

    // 모든 채널의 메세지 조회
    @Override
    public List<Message> getAllMessages() {
        return data.values()
                .stream()
                .filter(m -> !m.isDeleted())
                .toList();
    }

    // 특정 키워드 검색
    @Override
    public List<Message> searchByKeyword(String keyword) {
        String s = VerifiedUtils.verifyNullOrBlank(keyword).toLowerCase();
        return data.values()
                .stream()
                .filter(m -> !m.isDeleted())
                .filter(m -> m.getContent().toLowerCase().contains(s))
                .toList();
    }
}
