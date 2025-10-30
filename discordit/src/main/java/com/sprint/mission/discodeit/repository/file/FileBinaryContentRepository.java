package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exceptions.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@Primary
public class FileBinaryContentRepository implements BinaryContentRepository {

    private final Map<UUID, BinaryContent> data = new HashMap<>();

    @Override
    public void save(BinaryContent content) {
        data.put(content.getId(), content);
        write();
    }

    @Override
    public void saveAll(List<BinaryContent> contents) {
        contents.forEach(c -> data.put(c.getId(), c));
        write();
    }

    @Override
    public void delete(BinaryContent content) {
        data.remove(content.getId());
        write();
    }

    @Override
    public void deleteAll(List<BinaryContent> attachments) {
        attachments.forEach(c -> data.remove(c.getId()));
        write();
    }

    @Override
    public BinaryContent findById(UUID uuid) {
        if (!data.containsKey(uuid)) {
            throw new BinaryContentNotFoundException(uuid);
        }
        return data.get(uuid);
    }

    @Override
    public List<BinaryContent> findAllByUser(User user) {
        return data.values().stream()
                .filter(b -> b.getUploadUser().equals(user))
                .toList();
    }

    @Override
    public void deleteByID(UUID uuid) {
        data.remove(uuid);
        write();
    }

    private void write() {
        DataWriter.writeBinaryContent(List.copyOf(data.values()));
    }
}
