package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class JCFBinaryContentRepository implements BinaryContentRepository {
    // 바이너리 컨텐츠 데이터
    public final Map<UUID, BinaryContent> data = new ConcurrentHashMap<>();
    
    //저장
    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        data.put(binaryContent.getId(), binaryContent);
        return binaryContent;
    }

    //바이너리 컨텐츠 목록
    @Override
    public List<BinaryContent> findAll() {
        return data.values().stream().toList();
    }

    //id로 찾기
    @Override
    public Optional<BinaryContent> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    //삭제
    @Override
    public void delete(UUID id) {data.remove(id);}

    @Override
    public boolean existsById(UUID id) {
        return data.containsKey(id);
    }
}
