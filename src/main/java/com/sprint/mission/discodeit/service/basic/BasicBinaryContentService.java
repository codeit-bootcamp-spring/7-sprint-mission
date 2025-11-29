package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.response.binarycontent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
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
    private final BinaryContentStorage binaryContentStorage;

    @Transactional
    @Override
    public BinaryContentResponseDto create(BinaryContentCreateRequestDto binaryContentCreateRequestDto) {
        if(binaryContentCreateRequestDto.contentType() == null) {
            throw new IllegalArgumentException("contentType must not be null");
        }
        byte[] data = Objects.requireNonNull(binaryContentCreateRequestDto.data());

        BinaryContent bc = new BinaryContent(
                binaryContentCreateRequestDto.fileName(),
                binaryContentCreateRequestDto.contentType(),
                data.length
        );

        BinaryContent save = binaryContentRepository.save(bc);
        binaryContentStorage.put(save.getId(), data);


        return binaryContentMapper.toDto(save);
    }

    @Override
    public BinaryContentResponseDto findById(UUID id) {
        BinaryContent bc = binaryContentRepository.findById(Objects.requireNonNull(id))
                .orElseThrow(() -> new NoSuchElementException("BinaryContent with id: " + id + " not found"));
        return binaryContentMapper.toDto(bc);
    }

    @Override
    public List<BinaryContentResponseDto> findAllByIdIn(List<UUID> id) {
        return binaryContentRepository.findAllByIdIn(Objects.requireNonNull(id))
                .stream()
                .map(binaryContent -> binaryContentMapper.toDto(binaryContent))
                .toList();
    }

    @Transactional
    @Override
    public boolean delete(UUID id) {
        BinaryContent binaryContent = binaryContentRepository.findById(Objects.requireNonNull(id))
                .orElseThrow(() -> new NoSuchElementException("BinaryContent with id: " + id + " not found"));

        binaryContentStorage.delete(binaryContent.getId());
        binaryContentRepository.delete(binaryContent);

        return true;
    }
}
