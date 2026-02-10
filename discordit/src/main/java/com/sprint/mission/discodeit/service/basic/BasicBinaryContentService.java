package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.exceptions.binaryContent.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.dto.entity.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.dto.entity.binaryContent.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BasicBinaryContentService implements BinaryContentService {

    private final BinaryContentRepository contentRepository;
    private final BinaryContentStorage binaryContentStorage;
    private final BinaryContentMapper binaryContentMapper;

    @Transactional
    public BinaryContentDto create(BinaryContentCreateRequest dto) {
        log.info("파일 생성 요청 들어옴 : {} {}", dto.fileName(), dto.type());
        BinaryContent content = new BinaryContent(
                dto.fileName(),
                (long) dto.bytes().length,
                dto.type()
        );
        binaryContentStorage.put(content.getId(), dto.bytes());
        contentRepository.save(content);
        log.info("파일 저장 완료 : {}", content.getId());
        return binaryContentMapper.toDto(content);
    }

    @Override
    public BinaryContentDto get(UUID id) {
        return binaryContentMapper.toDto(
                contentRepository.findById(id)
                        .orElseThrow(() -> new BinaryContentNotFoundException(id)));
    }

    @Override
    public List<BinaryContentDto> getByIds(List<UUID> binaryContentIds) {
        return binaryContentIds.stream()
                .map(id -> contentRepository.findById(id)
                        .orElseThrow(() -> new BinaryContentNotFoundException(id)))
                .map(binaryContentMapper::toDto)
                .toList();
    }
}
