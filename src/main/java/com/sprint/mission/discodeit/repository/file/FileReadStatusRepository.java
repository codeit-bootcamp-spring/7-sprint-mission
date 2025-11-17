package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.config.RepositoryProperties;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileReadStatusRepository extends BaseFileRepository<ReadStatus>
    implements ReadStatusRepository {

  public FileReadStatusRepository(RepositoryProperties repositoryProperties) {
    super(ReadStatus.class, repositoryProperties);
  }

  //저장
  @Override
  public ReadStatus save(ReadStatus readStatus) {
    saveToFile(readStatus.getId(), readStatus);
    return readStatus;
  }

  //단일조회
  @Override
  public Optional<ReadStatus> findById(UUID statusId) {
    return loadFromFile(statusId);
  }

  @Override
  public List<ReadStatus> findAllByChannelId(UUID channelId) {
    return findAllFiles().stream()
        .filter(readStatus -> readStatus.getChannelId().equals(channelId))
        .toList();
  }

  @Override
  public List<ReadStatus> findAllByUserId(UUID userId) {
    return findAllFiles().stream()
        .filter(readStatus -> readStatus.getUserId().equals(userId))
        .toList();
  }

  @Override
  public void update(UUID statusId) {
    loadFromFile(statusId).ifPresent(readStatus -> {
      readStatus.updateReadAt();
      saveToFile(statusId, readStatus);
    });
  }

  //삭제
  @Override
  public void delete(UUID statusId) {
    deleteFile(statusId);
  }

  @Override
  public boolean existsById(UUID statusId) {
    return fileExistsById(statusId);
  }

  @Override
  public boolean existsByUserIdAndChannelId(UUID userId, UUID channelId) {
    return findAllFiles().stream().anyMatch(readStatus ->
        userId.equals(readStatus.getUserId()) && channelId.equals(readStatus.getChannelId())
    );
  }
}
