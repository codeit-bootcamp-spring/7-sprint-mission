package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.request.CreateBinaryContentDto;
import com.sprint.mission.discodeit.dto.binaryContent.response.BinaryContentResponseDto;
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
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public BinaryContentResponseDto createBinaryContent(CreateBinaryContentDto createBinaryContentDto) {
        BinaryContent binaryContent = new BinaryContent(
                createBinaryContentDto.fileName(),
                createBinaryContentDto.contentType(),
                createBinaryContentDto.bytes()
        );

        binaryContentRepository.save(binaryContent);

        return BinaryContentResponseDto.from(binaryContent);
    }

    @Override
    public BinaryContentResponseDto getBinaryContent(UUID binaryContentId) {
        BinaryContent binaryContent = binaryContentRepository.findById(binaryContentId)
                .orElseThrow(() -> new NoSuchElementException("찾을 수 없는 BinaryContent입니다." + binaryContentId));

        return BinaryContentResponseDto.from(binaryContent);
    }

    @Override
    public List<BinaryContentResponseDto> getAllBinaryContentByIdIn(List<UUID> binaryContentIds) {
        return binaryContentRepository.findAllByIdIn(binaryContentIds).stream()
                .map(BinaryContentResponseDto::from)
                .toList();
    }

    @Override
    public void deleteBinaryContent(UUID binaryContentId) {
        if(!binaryContentRepository.existsById(binaryContentId)) {
            throw new NoSuchElementException("존재하지 않는 BinaryContent입니다." + binaryContentId);
        }

        binaryContentRepository.deleteById(binaryContentId);
    }
}
