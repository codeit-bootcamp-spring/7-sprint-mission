package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileBinaryContentRepository extends BaseFileRepository<BinaryContent> implements BinaryContentRepository {
    public FileBinaryContentRepository() {
        super(BinaryContent.class);
    }

    //저장
    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        saveToFile(binaryContent.getId(), binaryContent);
        return binaryContent;
    }

    //바이너리 컨텐츠 목록
    @Override
    public List<BinaryContent> findAll() {
        return findAllFiles();
    }

    //id 로 찾기
    @Override
    public Optional<BinaryContent> findById(UUID binaryId) {
        return Optional.ofNullable(loadFromFile(binaryId));
    }

    //삭제
    @Override
    public void delete(UUID binaryId) {
        deleteFile(binaryId);
    }

    @Override
    public boolean existsById(UUID id) {
        return fileExistsById(id);
    }
}
