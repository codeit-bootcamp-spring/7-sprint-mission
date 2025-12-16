package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.response.binarycontent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class BasicBinaryContentService implements BinaryContentService {
    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentMapper binaryContentMapper;
    private final BinaryContentStorage binaryContentStorage;

    @Transactional
    @Override
    public BinaryContentResponseDto create(BinaryContentCreateRequestDto binaryContentCreateRequestDto) {
        if(binaryContentCreateRequestDto.contentType() == null) {
            log.warn("Create binary content rejected: contentType is null. fileName = {}",
                    binaryContentCreateRequestDto.contentType());
            throw new IllegalArgumentException("contentType must not be null");
        }
        byte[] data = Objects.requireNonNull(binaryContentCreateRequestDto.data());

        log.debug("Creating binary content: filName = {}, contentType = {}, size = {}",
                binaryContentCreateRequestDto.fileName(),
                binaryContentCreateRequestDto.contentType(), data.length);

        BinaryContent bc = new BinaryContent(
                binaryContentCreateRequestDto.fileName(),
                binaryContentCreateRequestDto.contentType(),
                data.length
        );

        BinaryContent save = binaryContentRepository.save(bc);
        try {
            binaryContentStorage.put(save.getId(), data);
        } catch (RuntimeException e) {
            log.error("Binary content storage put failed. binaryContentId = {}", save.getId(), e);
            binaryContentRepository.deleteById(save.getId());
            throw e;
        }

        log.info("첨부파일이 생성되었습니다.");
        return binaryContentMapper.toDto(save);
    }

    @Override
    public BinaryContentResponseDto findById(UUID id) {
        log.debug("Finding binary content by id: binaryContentId = {}", id);
        BinaryContent bc = binaryContentRepository.findById(Objects.requireNonNull(id))
                .orElseThrow(() -> new NoSuchElementException("BinaryContent with id: " + id + " not found"));
        return binaryContentMapper.toDto(bc);
    }

    @Override
    public List<BinaryContentResponseDto> findAllByIdIn(List<UUID> id) {
        log.debug("Finding all binary content by ids: binaryContentId = {}", id);
        return binaryContentRepository.findAllByIdIn(Objects.requireNonNull(id))
                .stream()
                .map(binaryContent -> binaryContentMapper.toDto(binaryContent))
                .toList();
    }

    @Transactional
    @Override
    public boolean delete(UUID id) {
        log.debug("Deleting binary content by id: binaryContentId = {}", id);
        BinaryContent binaryContent = binaryContentRepository.findById(Objects.requireNonNull(id))
                .orElseThrow(() -> new NoSuchElementException("BinaryContent with id: " + id + " not found"));

        binaryContentRepository.delete(binaryContent);
        try {
            binaryContentStorage.delete(binaryContent.getId());
        } catch (RuntimeException e) {
            log.warn("Binary content storage delete failed. binaryContentId = {}", binaryContent.getId(), e);
        }

        log.info("첨부파일이 제거되었습니다.");
        return true;
    }
}
