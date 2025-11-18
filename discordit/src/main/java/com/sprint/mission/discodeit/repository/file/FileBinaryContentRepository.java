package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@Primary
public class FileBinaryContentRepository implements BinaryContentRepository {

    private final Map<UUID, BinaryContent> data = new HashMap<>();
    private final DataWriter dataWriter;

    public FileBinaryContentRepository(DataWriter dataWriter) {
        this.dataWriter = dataWriter;
    }

    @Override
    public void save(BinaryContent content) {
        data.put(content.getUuid(), content);
        write();
    }

    @Override
    public void saveAll(List<BinaryContent> contents) {
        contents.forEach(c -> data.put(c.getUuid(), c));
        write();
    }

    @Override
    public void delete(BinaryContent content) {
        data.remove(content.getUuid());
        write();
    }

    @Override
    public void deleteAll(List<BinaryContent> attachments) {
        attachments.forEach(c -> data.remove(c.getUuid()));
        write();
    }

    @Override
    public Optional<BinaryContent> findById(UUID uuid) {
        return Optional.ofNullable(data.get(uuid));
    }

    @Override
    public List<BinaryContent> findAll() {
        return List.copyOf(data.values());
    }

    @Override
    public void deleteByID(UUID uuid) {
        data.remove(uuid);
        write();
    }

    private void write() {
        dataWriter.writeBinaryContent(List.copyOf(data.values()));
    }
}
