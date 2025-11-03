package com.sprint.mission.discodeit.repository.JCF;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.InterfaceMessageRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class JCFMessageRepository implements InterfaceMessageRepository {
    private final Map<UUID, Message> data;

    public JCFMessageRepository() {
        this.data = new HashMap<>();
    }

    @Override
    public Optional<List<Message>> findAllMessageInChannel(UUID channelID) {
        return Optional.empty();
    }

    @Override
    public Set<UUID> findAllUsersInChannel(List<Message> allMessageInChannel) {
        return Set.of();
    }

    @Override
    public void save(Message message) {
        this.data.put(message.getId(), message);
    }

    @Override
    public void deleteById(UUID id) {
        this.data.remove(id);
    }

    @Override
    public Optional<Message> findById(UUID id) {
        return Optional.of(this.data.get(id));
    }

    @Override
    public Optional<List<Message>> findAll() {
        List<Message> list = this.data.values().stream().toList();
        return Optional.of(list);
    }

    @Override
    public boolean existsById(UUID id) {
        return this.data.containsKey(id);
    }

    @Override
    public boolean existsByName(String name) {
//        this.data.values().stream().anyMatch(message -> message.get)
        return false;
    }
}
