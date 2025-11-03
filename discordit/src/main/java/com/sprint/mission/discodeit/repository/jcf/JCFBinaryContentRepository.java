package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exceptions.ContentNotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class JCFBinaryContentRepository implements BinaryContentRepository {
    private final Map<UUID, BinaryContent> data = new HashMap<>();

    @Override
    public void save(BinaryContent content) {
        data.put(content.getId(), content);
    }

    @Override
    public void saveAll(List<BinaryContent> contents) {
        contents.forEach(c -> data.put(c.getId(), c));
    }

    @Override
    public void delete(BinaryContent content) {
        if (!data.containsKey(content.getId())) {
            throw new ContentNotFoundException(content);
        }
        data.remove(content.getId());
    }

    @Override
    public void deleteAll(List<BinaryContent> attachments) {
        attachments.forEach(c -> data.remove(c.getId()));
    }

    @Override
    public BinaryContent findById(UUID uuid) {
        return data.get(uuid);
    }

    @Override
    public List<BinaryContent> findAll() {
        return List.copyOf(data.values());
    }

    @Override
    public List<BinaryContent> findAllByUser(User user) {
        return data.values().stream()
                .filter(c -> c.getUploadUser().equals(user))
                .toList();
    }

    @Override
    public void deleteByID(UUID uuid) {
        data.remove(uuid);
    }
}
