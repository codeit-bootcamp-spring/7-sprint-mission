package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.BaseRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class FileMessageRepository extends FileBaseRepository<Message> implements MessageRepository {

    public FileMessageRepository(String rootPath) {
        super(rootPath + "Data.ser");
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return data.values().stream()
                .filter(m -> m.getType() == Message.MessageType.CHANNEL &&
                        m.getChannel().getId().equals(channelId)).collect(Collectors.toList());
    }

    @Override
    public List<Message> findAllByBetweenUserIds(UUID userId1, UUID userId2) {
        return data.values().stream()
                .filter(m -> m.getType() == Message.MessageType.DIRECT)
                .filter(m -> (m.getAuthor().getId().equals(userId1)
                        && m.getReceiver().getId().equals(userId2))
                        || m.getAuthor().getId().equals(userId2) &&  m.getReceiver().getId().equals(userId1))
                .collect(Collectors.toList());
    }
}
