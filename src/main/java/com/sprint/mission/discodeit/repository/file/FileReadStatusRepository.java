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
    public void deleteById(UUID readStatusID) {
        fileUtil.deleteRepository(readStatusID);
    }

    @Override
    public Optional<ReadStatus> findById(UUID readStatusID) {
        ReadStatus readStatus = (ReadStatus) fileUtil.findModel(readStatusID).orElseThrow(() -> new IllegalArgumentException("🚨channel id = [" + readStatusID.toString()+ "] 오류"));
        return Optional.of(readStatus);
    }


    @Override
    public Optional<List<ReadStatus>> findAll() {
        return Optional.ofNullable(fileUtil.findAll().stream().map(readStatusID -> (ReadStatus)readStatusID).toList());
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
//    public Optional<List<Channel>> findallByChannleId() {
//        List<Channel> channels = fileUtil.findallByChannleId().stream().map(model -> (Channel)model).toList();
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
//        return fileUtil.findallByChannleId().stream().map(channel -> (Channel)channel).anyMatch(channel -> channel.getChannelName().equals(name));
//    }
}
