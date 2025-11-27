package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, UUID> {
    List<ReadStatus> findByChannel(Channel channel);

    ReadStatus findReadStatusByChannelAndUser(Channel channel, User user);
//    public ReadStatus createReadStatus(ReadStatus readStatus);
//    public void deleteReadStatus(UUID readStatusID);
//    public void updateReadStatus(ReadStatus readStatus);
//    public Optional<ReadStatus> readReadStatus(UUID readStatusID);
//    public List<ReadStatus> readAllReadStatus();
//    public boolean isReadStatusExist(UUID userId, UUID channelId);
//    public void resetRepository();
}
