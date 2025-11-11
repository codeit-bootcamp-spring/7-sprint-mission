package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public BinaryContent create(BinaryContentCreateRequestDto binaryContentCreateRequestDto) {
        if(binaryContentCreateRequestDto.contentType() == null) {
            throw new IllegalArgumentException("contentType must not be null");
        }
        BinaryContent bc = new BinaryContent(
                binaryContentCreateRequestDto.fileName(),
                binaryContentCreateRequestDto.contentType(),
                binaryContentCreateRequestDto.data()
        );

        return binaryContentRepository.save(bc);
    }

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        return binaryContentRepository.findById(Objects.requireNonNull(id));
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> id) {
        return binaryContentRepository.findAllByIdIn(Objects.requireNonNull(id));
    }

    @Override
    public boolean delete(UUID id) {
        return binaryContentRepository.deleteById(Objects.requireNonNull(id));
    }
}
