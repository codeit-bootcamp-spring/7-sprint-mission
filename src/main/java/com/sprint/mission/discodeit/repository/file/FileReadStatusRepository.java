package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.InterfaceReadStatusRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class FileReadStatusRepository  implements InterfaceReadStatusRepository {
    private final FileUtil fileUtil;
    public FileReadStatusRepository(@Qualifier("readStatusFileUtil") FileUtil fileUtil) {
        this.fileUtil = fileUtil;
    }

//    @Override
    @Override
    public void save(ReadStatus readStatus) {
        fileUtil.saveRepository(readStatus);
    }

    @Override
    public boolean deleteById(UUID readStatusID) {
        return fileUtil.deleteRepository(readStatusID);
    }

    @Override
    public Optional<ReadStatus> findById(UUID readStatusID) {
      return fileUtil.findModel(readStatusID).map(model -> (ReadStatus)model);
    }

    @Override
    public List<ReadStatus> findAll() {
        return fileUtil.findAll().stream().map(readStatusID -> (ReadStatus)readStatusID).toList();
    }

    @Override
    public boolean existsById(UUID readStatusID) {
        return false;
    }

    @Override
    public boolean existsByName(String name) {
        return false;
    }

    //    @Override
//    public Optional<List<Channel>> findAllByChannleId() {
//        List<Channel> channels = fileUtil.findAllByChannleId().stream().map(model -> (Channel)model).toList();
//        return Optional.ofNullable(channels);
//    }
//
//    @Override
//    public boolean existsById(UUID channelID) {
//        return fileUtil.existsRepository(channelID);
//    }
//
//    @Override
//    public boolean existsByName(String name) {
//        return fileUtil.findAllByChannleId().stream().map(channel -> (Channel)channel).anyMatch(channel -> channel.getChannelName().equals(name));
//    }
    @Override
    public Optional<ReadStatus> findByUserAndChannelId(UUID userID, UUID channelID) {
        return fileUtil.findAll().stream()
            .map(readStatus -> (ReadStatus)readStatus)
            .filter(readStatus -> readStatus.getUserId().equals(userID) && readStatus.getChannelId().equals(channelID))
            .findFirst();
    }
}
