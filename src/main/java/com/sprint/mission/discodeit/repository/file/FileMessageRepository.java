package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.BaseInterfaceRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class FileMessageRepository implements BaseInterfaceRepository<Message> {
    private final FileUtil fileUtil;

    public FileMessageRepository(@Qualifier("messageFileUtil") FileUtil fileUtil) {
        this.fileUtil = fileUtil;
    }

    @Override
    public void save(Message message) {
        fileUtil.saveRepository(message);
    }

    @Override
    public boolean deleteById(UUID id) {
        return fileUtil.deleteRepository(id);
    }

    @Override
    public Optional<Message> findById(UUID id) {
        Optional<Message> message = fileUtil.findModel(id).map(model -> (Message)model); //.orElseThrow(() -> new NoSuchElementException("🚨MESSAGE readStatusID = [" + readStatusID + "] 오류"));
        return message;
    }

    @Override
    public List<Message> findAll() {
        return fileUtil.findAll().stream().map(model -> (Message) model).toList();
    }

    @Override
    public Optional<List<Message>> findAllMessageInChannel(UUID channelID) {
        List<Message> list = fileUtil.findAll().stream().map(model -> (Message)model)
                .filter(message -> message.getChannelId().equals(channelID)).toList();
        return Optional.of(list);
    }

    @Override
    public Set<UUID> findAllUsersInChannel(List<Message> allMessageInChannel) {
        Set<UUID> userIDs = new HashSet<>();
        for (Message message : allMessageInChannel) {
            userIDs.add(message.getAuthorId());
        }
        return userIDs;
    }
}
