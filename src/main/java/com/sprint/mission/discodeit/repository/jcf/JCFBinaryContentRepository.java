package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JCFBinaryContentRepository implements BinaryContentRepository {
    // 바이너리 컨텐츠 데이터
    private final Map<UUID, BinaryContent> data = new HashMap<>();
    
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

    //id 로 찾기
    @Override
    public BinaryContent findById(UUID id) {
        return data.get(id);
    }

    //삭제
    @Override
    public BinaryContent delete(UUID id) {
        BinaryContent binaryContent = findById(id);
        data.remove(id);
        return binaryContent;
    }
}
