package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class FileMessageRepository extends FileBaseRepository<Message> implements MessageRepository {

    private static final String MESSAGE_DATA_FILE = "messageData.ser";

    public FileMessageRepository(String basePath) {
        super(basePath + "/" + MESSAGE_DATA_FILE);
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return data.values().stream()
                .filter(m -> channelId.equals(m.getChannelId()))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAllByChannelId(UUID channelId) {
        if (data.values().removeIf(m -> channelId.equals(m.getChannelId())))
            saveData();
    }

    @Override
    public Optional<Message> findTopByChannelId(UUID channelId) {
        return data.values().stream()
                .filter(m -> channelId.equals(m.getChannelId()))
                .max(Comparator.comparing(Message::getCreatedAt));
    }
}
