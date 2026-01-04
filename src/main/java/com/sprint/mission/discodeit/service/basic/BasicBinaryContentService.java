package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.request.CreateBinaryContentDto;
import com.sprint.mission.discodeit.dto.binaryContent.response.BinaryContentResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.global.exception.discodietException.binaryContent.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentMapper binaryContentMapper;
    private final BinaryContentStorage binaryContentStorage;

    @Transactional
    @Override
    public BinaryContentResponseDto createBinaryContent(CreateBinaryContentDto createBinaryContentDto) {
        BinaryContent binaryContent = BinaryContent.builder()
                .fileName(createBinaryContentDto.fileName())
                .size(createBinaryContentDto.size())
                .contentType(createBinaryContentDto.contentType())
                .build();

        binaryContentRepository.save(binaryContent);
        binaryContentStorage.put(binaryContent.getId(), createBinaryContentDto.bytes());

        return binaryContentMapper.toResponseDto(binaryContent);
    }

    @Transactional(readOnly = true)
    @Override
    public BinaryContentResponseDto getBinaryContent(UUID binaryContentId) {
        BinaryContent binaryContent = binaryContentRepository.findById(binaryContentId)
                .orElseThrow(() -> BinaryContentNotFoundException.byId(binaryContentId));

        return binaryContentMapper.toResponseDto(binaryContent);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BinaryContentResponseDto> getAllBinaryContentByIdIn(List<UUID> binaryContentIds) {
        return binaryContentRepository.findAllById(binaryContentIds)
                .stream()
                .map(binaryContentMapper::toResponseDto)
                .toList();
    }

    @Transactional
    @Override
    public void deleteBinaryContent(UUID binaryContentId) {
        if (!binaryContentRepository.existsById(binaryContentId)) {
            throw BinaryContentNotFoundException.byId(binaryContentId);
        }

        binaryContentRepository.deleteById(binaryContentId);
    }
}
