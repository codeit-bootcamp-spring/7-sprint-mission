package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.exception.binarycontent.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.common.exception.binarycontent.BinaryContentStorageException;
import com.sprint.mission.discodeit.common.exception.binarycontent.InvalidBinaryContentRequestException;
import com.sprint.mission.discodeit.common.exception.ErrorCode;
import com.sprint.mission.discodeit.dto.request.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.response.binarycontent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
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
    if (binaryContentCreateRequestDto == null) {
        throw new InvalidBinaryContentRequestException("binaryContentCreateRequestDto is null");
    }
    if (binaryContentCreateRequestDto.contentType() == null) {
        throw new InvalidBinaryContentRequestException("content type is invalid.");
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
    } catch (Exception e) {
        log.error("Binary content storage put failed. binaryContentId = {}", save.getId(), e);
        binaryContentRepository.deleteById(save.getId());
        throw new BinaryContentStorageException(
                ErrorCode.BINARY_CONTENT_STORAGE_PUT_FAILED,
                save.getId(),
                e
        );
    }

    log.info("첨부파일이 생성되었습니다. binaryContentId = {}", save.getId());
    return binaryContentMapper.toDto(save);
}

@Override
public BinaryContentResponseDto findById(UUID id) {
    log.debug("Finding binary content by id: binaryContentId = {}", id);
    BinaryContent bc = binaryContentRepository.findById(Objects.requireNonNull(id))
            .orElseThrow(() -> new BinaryContentNotFoundException(id));
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
            .orElseThrow(() -> new BinaryContentNotFoundException(id));

    try {
        binaryContentStorage.delete(binaryContent.getId());
    } catch (Exception e) {
        log.error("Binary content storage delete failed. binaryContentId = {}", binaryContent.getId(), e);
        throw new BinaryContentStorageException(
                ErrorCode.BINARY_CONTENT_STORAGE_DELETE_FAILED,
                binaryContent.getId(),
                e
        );
    }
    binaryContentRepository.delete(binaryContent);

    log.info("첨부파일이 제거되었습니다.  binaryContentId = {}", binaryContent.getId());
    return true;
}

    @Override
    public ResponseEntity<Resource> download(UUID binaryContentId) {
        log.debug("Downloading binary content by id: binaryContentId = {}", binaryContentId);
        BinaryContentResponseDto dto = findById(Objects.requireNonNull(binaryContentId));

        try {
        log.info("BinaryContent download. binaryContentId = {}, contentType = {}, size = {}",
                dto.id(), dto.contentType(), dto.size());
            return binaryContentStorage.download(dto);
        } catch (Exception e) {
            log.error("Binary content download failed. binaryContentId = {}", binaryContentId, e);
            throw new BinaryContentStorageException(
                    ErrorCode.BINARY_CONTENT_STORAGE_DOWNLOAD_FAILED,
                    binaryContentId,
                    e
            );
        }
    }
}
