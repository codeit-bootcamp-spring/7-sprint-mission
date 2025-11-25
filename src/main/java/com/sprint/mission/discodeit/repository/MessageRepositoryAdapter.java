package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.domain.Message;
import com.sprint.mission.discodeit.domain.repository.MessageRepository;

import com.sprint.mission.discodeit.repository.jpainterface.JpaMessageRepository;
import com.sprint.mission.discodeit.service.mapper.MessageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class MessageRepositoryAdapter implements MessageRepository {

    private final JpaMessageRepository jpa;
    private final MessageMapper mapper;

    @Override
    public void save(Message message) {
        jpa.save(mapper.toMessageEntity(message));
    }

    @Override
    public void delete(Message message) {
        jpa.delete(mapper.toMessageEntity(message));
    }

    @Override
    public Optional<Message> findById(String id) {
        return jpa.findById(id).map(mapper::toMessage);
    }

    @Override
    public List<Message> findAll() {
        return jpa.findAll().stream().map(mapper::toMessage).toList();
    }

    @Override
    public List<Message> findByChannelId(String channelId) {
        return jpa.findAllByChannelId(UUID.fromString(channelId)).stream().map(mapper::toMessage).toList();
    }
}
