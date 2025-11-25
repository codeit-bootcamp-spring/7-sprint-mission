package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.response.binarycontent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasicBinaryContentService implements BinaryContentService {
    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentMapper binaryContentMapper;

    @Transactional
    @Override
    public BinaryContentResponseDto create(BinaryContentCreateRequestDto binaryContentCreateRequestDto) {
        if(binaryContentCreateRequestDto.contentType() == null) {
            throw new IllegalArgumentException("contentType must not be null");
        }
        BinaryContent bc = new BinaryContent(
                binaryContentCreateRequestDto.fileName(),
                binaryContentCreateRequestDto.contentType(),
                binaryContentCreateRequestDto.data()
        );

        BinaryContent save = binaryContentRepository.save(bc);

        return binaryContentMapper.toDto(save);
    }

    @Transactional
    @Override
    public BinaryContentResponseDto findById(UUID id) {
        BinaryContent bc = binaryContentRepository.findById(Objects.requireNonNull(id))
                .orElseThrow(() -> new NoSuchElementException("BinaryContent with id: " + id + " not found"));
        return binaryContentMapper.toDto(bc);
    }

    @Transactional
    @Override
    public List<BinaryContentResponseDto> findAllByIdIn(List<UUID> id) {
        return binaryContentRepository.findAllByIdIn(Objects.requireNonNull(id))
                .stream()
                .map(binaryContent -> binaryContentMapper.toDto(binaryContent))
                .toList();
    }

    @Override
    public boolean delete(UUID id) {
        if(!binaryContentRepository.existsById(Objects.requireNonNull(id))) {
            return false;
        }
        binaryContentRepository.deleteById(id);
        return true;
    }
}
