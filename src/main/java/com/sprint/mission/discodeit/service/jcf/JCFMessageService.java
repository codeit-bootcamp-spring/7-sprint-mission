package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.VerifiedUtils;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.*;

public class JCFMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;

    public JCFMessageService(MessageRepository messageRepository,  ChannelRepository channelRepository,  UserRepository userRepository) {
        this.messageRepository = VerifiedUtils.verifyNull(messageRepository);
        this.channelRepository = VerifiedUtils.verifyNull(channelRepository);
        this.userRepository = VerifiedUtils.verifyNull(userRepository);
    }

    @Override
    public Message create(Message message) {
        Message msg = VerifiedUtils.verifyNull(message);
        UUID id = VerifiedUtils.verifyNull(msg.getId());

        if(messageRepository.findById(id).isPresent()) {
            throw new IllegalStateException("Message already exists: " + id);
        }
        userRepository.findById(msg.getAuthorId()).orElseThrow(() -> new NoSuchElementException("User not found" + msg.getAuthorId()));
        Channel ch =  channelRepository.findById(msg.getChannelId()).orElseThrow(()-> new NoSuchElementException("Channel not found" + msg.getChannelId()));
        if(!ch.getMembers().containsKey(msg.getAuthorId())) {
            throw new NoSuchElementException("Member not found" +  msg.getAuthorId());
        }
        int slow = ch.getSlowModeSeconds();
        if ( slow < 0 ) {
            throw new IllegalStateException("SlowModeSeconds must be >= 0");
        }

        if ( slow > 0 ) {
            long timeNow = System.currentTimeMillis();
            long windowTime = slow * 1000L;

            OptionalLong last = messageRepository.findByChannelIdAndAuthorId(msg.getChannelId(),msg.getAuthorId())
                    .stream()
                    .filter(m -> !m.isDeleted())
                    .mapToLong(m -> m.getCreatedAt())
                    .max();

            if (last.isPresent() && (timeNow - last.getAsLong()) < windowTime) {
                long leftTime = windowTime - (timeNow - last.getAsLong());
                long seconds = (leftTime + 999) / 1000;
                throw new IllegalStateException("SlowModeSeconds wait : " + seconds + "s");
            }
        }
        return messageRepository.save(msg);
    }

    @Override
    public Message get(UUID uuid) {
        UUID id = VerifiedUtils.verifyNull(uuid);
        return messageRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Message not found: " + id));
    }

    @Override
    public List<Message> getAll() {
        return messageRepository.findAll();
    }

    @Override
    public Message update(Message message) {
        Message msg = VerifiedUtils.verifyNull(message);
        UUID id = VerifiedUtils.verifyNull(msg.getId());
        messageRepository.findById(id).orElseThrow(()-> new NoSuchElementException("Message not found: " + id));
        return messageRepository.save(msg);
    }

    @Override
    public boolean delete(UUID uuid) {
        UUID id = VerifiedUtils.verifyNull(uuid);
        return messageRepository.deleteById(id);
    }

    // 특정 채널별 메세지 조회
    @Override
    public List<Message> getMessagesByChannel(UUID channelId) {
        UUID id = VerifiedUtils.verifyNull(channelId);
        return messageRepository.findByChannel(id);
    }
    // 특정 작성자별 메세지 조회
    @Override
    public List<Message> getMessagesByAuthor(UUID authorId) {
        UUID id = VerifiedUtils.verifyNull(authorId);
        return messageRepository.findByAuthor(id);
    }
    // 특정 채널의 특정 작성자 메세지 조회
    @Override
    public List<Message> getMessagesByChannelAndAuthor(UUID channelId, UUID authorId) {
        UUID cid = VerifiedUtils.verifyNull(channelId);
        UUID aid = VerifiedUtils.verifyNull(authorId);
        return messageRepository.findByChannelIdAndAuthorId(cid,aid);
    }

    // 모든 채널의 메세지 조회
    @Override
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    // 특정 키워드 검색
    @Override
    public List<Message> searchByKeyword(String keyword) {
        String s = VerifiedUtils.verifyNullOrBlank(keyword);
        return messageRepository.searchByKeyword(s);
    }
}
