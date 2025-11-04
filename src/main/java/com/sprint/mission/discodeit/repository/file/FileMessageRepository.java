package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;


public class FileMessageRepository extends AbstractFileRepository<Message, UUID> implements MessageRepository {

    private static final String FILE_PATH = "data" + File.separator + "messages.ser";

    @Override
    protected String getFilePath() {
        return FILE_PATH;
    }

    @Override
    protected UUID getId(Message message) {
        return message.getId();
    }

    @Override
    public List<Message> findByChannelId(UUID channelId) {
        return findAll().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .toList();
    }

    @Override
    public void deleteByChannelId(UUID channelId) {
        Map<UUID, Message> data = loadData();

        List<UUID> list = data.values().stream()
                .filter(message -> message.getChannelId().equals(channelId))
                .map(Message::getId)
                .toList();

        list.forEach(data::remove);
        saveData(data);
    }
}
