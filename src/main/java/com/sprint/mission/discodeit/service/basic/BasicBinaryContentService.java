package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.CreateBinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {
    BinaryContentRepository binaryContentRepository;

    @Override
    public BinaryContent createBinaryContent(CreateBinaryContentDto createBinaryContentDto) {
        BinaryContent binaryContent = new BinaryContent(
                createBinaryContentDto.getFileName(),
                createBinaryContentDto.getContentType(),
                createBinaryContentDto.getBytes()
        );

        binaryContentRepository.save(binaryContent);

        return binaryContent;
    }

    @Override
    public BinaryContent getBinaryContent(UUID binaryContentId) {
        return binaryContentRepository.findById(binaryContentId)
                .orElseThrow(() -> new NoSuchElementException("찾을 수 없는 BinaryContent입니다." + binaryContentId));
    }

    @Override
    public List<BinaryContent> getAllBinaryContentByIdIn(List<UUID> binaryContentIds) {
        return binaryContentRepository.findAllByIdIn(binaryContentIds);
    }

    @Override
    public void deleteBinaryContent(UUID binaryContentId) {
        if(!binaryContentRepository.existsById(binaryContentId)) {
            throw new NoSuchElementException("존재하지 않는 BinaryContent입니다." + binaryContentId);
        }

        binaryContentRepository.deleteById(binaryContentId);
    }
}
