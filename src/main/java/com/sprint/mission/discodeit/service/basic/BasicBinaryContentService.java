package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicBinaryContentService implements BinaryContentService {
    private final BinaryContentRepository binaryContentRepository;

    // ===== 🏗️ Domain Logic (Facade 용)  =====
    @Override
    public BinaryContent create(BinaryContent binaryContent) {
        return binaryContentRepository.save(binaryContent);
    }

    @Override
    public BinaryContent findById(UUID id) {
        return binaryContentRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.BINARYCONTENT_NOT_FOUNT));
    }

    @Override
    public List<BinaryContent> findAll() {
        return binaryContentRepository.findAll();
    }

    @Override
    public void delete(UUID id) {
        if(!binaryContentRepository.existsById(id)){
            throw new CustomException(ErrorCode.BINARYCONTENT_NOT_FOUNT);
        }
        binaryContentRepository.delete(id);
    }
}