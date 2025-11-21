package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.entity.dto.binaryContentDto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.dto.binaryContentDto.BinaryContentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BasicBinaryContentService implements BinaryContentService {

    private final BinaryContentRepository binaryContentRepository;

    @Override
    public BinaryContentDto createBinaryContent(BinaryContentCreateRequest requestDto) {

        BinaryContent newContent = BinaryContent.builder()
                .bytes(requestDto.data())
                .fileName(requestDto.dataName())
                .contentType(requestDto.dataType())
                .build();

        binaryContentRepository.save(newContent);
        return BinaryContentDto.from(newContent);
    }

    @Override
    public BinaryContentDto findBinaryContentById(UUID id) {
        BinaryContent content=  binaryContentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("찾을 수 없음"));
        return BinaryContentDto.from(content);
    }

    @Override
    public List<BinaryContentDto> findAllBinaryContentByIdIn(List<UUID> ids) {
        return binaryContentRepository.findAllByIdIn(ids).stream()
                .map(BinaryContentDto::from)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteBinaryContentById(UUID id) {
        binaryContentRepository.deleteById(id);
    }
}
