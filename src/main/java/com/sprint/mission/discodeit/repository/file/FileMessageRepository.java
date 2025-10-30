package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.MessageType;
import com.sprint.mission.discodeit.repository.BaseRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class FileMessageRepository extends FileBaseRepository<Message> implements MessageRepository {

    private static final String MESSAGE_DATA_FILE = "messageData.ser";

    public FileMessageRepository() {
        super(MESSAGE_DATA_FILE);
    }

    @Override
    public List<Message> findAllByChannelId(UUID channelId) {
        return data.values().stream()
                .filter(m -> m.getType() == MessageType.CHANNEL &&
                        m.getChannel().getId().equals(channelId)).collect(Collectors.toList());
    }

    @Override
    public List<Message> findAllByBetweenUserIds(UUID userId1, UUID userId2) {
        return data.values().stream()
                .filter(m -> m.getType() == MessageType.DIRECT)
                .filter(m -> (m.getAuthor().getId().equals(userId1)
                        && m.getReceiver().getId().equals(userId2))
                        || m.getAuthor().getId().equals(userId2) && m.getReceiver().getId().equals(userId1))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAllByChannelId(UUID channelId) {
        data.values().removeIf(message -> message.getChannel().getId().equals(channelId));
    }

    @Override
    public Optional<Message> findTopByChannelId(UUID channelId) {
        return data.values().stream()
                .filter(m ->
                        m.getType() == MessageType.CHANNEL &&   // DM은 channelId == null
                                m.getChannel() != null &&
                                m.getChannel().getId().equals(channelId)
                )
                .max(Comparator.comparing(Message::getCreatedAt));
    }
}
