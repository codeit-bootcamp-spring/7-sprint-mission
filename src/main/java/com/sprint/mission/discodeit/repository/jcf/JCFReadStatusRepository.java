package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JCFReadStatusRepository extends BaseJCFRepository<ReadStatus> implements
    ReadStatusRepository {

  @Override
  public ReadStatus save(ReadStatus readStatus) {
    beforeModify();
    data.put(readStatus.getId(), readStatus);
    return readStatus;
  }

  @Override
  public Optional<ReadStatus> findById(UUID statusId) {
    return Optional.ofNullable(data.get(statusId));
  }

  @Override
  public List<ReadStatus> findAllByChannelId(UUID channelId) {
    return data.values().stream()
        .filter(readStatus -> readStatus.getChannelId().equals(channelId))
        .toList();
  }

  @Override
  public List<ReadStatus> findAllByUserId(UUID userId) {
    return data.values().stream()
        .filter(readStatus -> readStatus.getUserId().equals(userId))
        .toList();
  }

  @Override
  public void update(UUID statusId) {
    beforeModify();
    data.get(statusId).updateReadAt();
  }

  @Override
  public void delete(UUID statusId) {
    beforeModify();
    data.remove(statusId);
  }

  @Override
  public boolean existsById(UUID statusId) {
    return data.containsKey(statusId);
  }

  @Override
  public boolean existsByUserIdAndChannelId(UUID userId, UUID channelId) {
    return data.values().stream().anyMatch(readStatus ->
        readStatus.getUserId().equals(userId) && readStatus.getChannelId().equals(channelId)
    );
  }
}
