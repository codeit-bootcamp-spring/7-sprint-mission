package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.entity.dto.binaryContentDto.BinaryContentRequestDto;
import com.sprint.mission.discodeit.entity.dto.binaryContentDto.BinaryContentResponseDto;
import com.sprint.mission.discodeit.exception.FileSizeLimitExceededException;
import com.sprint.mission.discodeit.exception.InvalidInputException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

    private final BinaryContentRepository binaryContentRepository;

    @Override
    public BinaryContentResponseDto createBinaryContent(BinaryContentRequestDto requestDto) {

        BinaryContent newContent = BinaryContent.builder()
                .binaryData(requestDto.data())
                .dataName(requestDto.dataName())
                .dataType(requestDto.dataType())
                .build();

        binaryContentRepository.save(newContent);
        return BinaryContentResponseDto.from(newContent);
    }

    @Override
    public BinaryContentResponseDto findBinaryContentById(UUID id) {
        BinaryContent content=  binaryContentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("찾을 수 없음"));
        return BinaryContentResponseDto.from(content);
    }

    @Override
    public List<BinaryContentResponseDto> findAllBinaryContentByIdIn(List<UUID> ids) {
        return binaryContentRepository.findAllByIdIn(ids).stream()
                .map(BinaryContentResponseDto::from)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteBinaryContentById(UUID id) {
        binaryContentRepository.deleteById(id);
    }
}
