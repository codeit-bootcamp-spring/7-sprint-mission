package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicBinaryContentService implements BinaryContentService {
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public BinaryContent create(BinaryContent binaryContent) {
        return null;
    }

    @Override
    public BinaryContent findById(UUID id) {
        return null;
    }

    @Override
    public BinaryContent findAll() {
        return null;
    }

    @Override
    public BinaryContent delete(UUID id) {
        return null;
    }
}
