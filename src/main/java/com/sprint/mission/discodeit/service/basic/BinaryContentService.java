package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.BinaryContentStatus;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.dto.dto_Neo.BinaryContentDto;
import com.sprint.mission.discodeit.repository.jpa.BinaryContentsRepository;
import com.sprint.mission.discodeit.service.InterfaceBinaryContentService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
//@Transactional // 영속성 컨텍스트
@RequiredArgsConstructor
public class BinaryContentService implements InterfaceBinaryContentService {
    private final BinaryContentsRepository binaryContentRepository;
    private final BinaryContentMapper binaryContentMapper;

    @Override
    @Transactional(readOnly = true)
    public  BinaryContentDto find(UUID binaryContentId) {
//    [ ] id로 조회합니다.
        BinaryContent binaryContent = binaryContentRepository.findById(binaryContentId)
            .orElseThrow(() -> new NoSuchElementException("🚨첨부파일[" + binaryContentId.toString() + "]을 찾을 수 없음"));

        log.info("✅binaryContent.find ok! - binaryContentId = {}", binaryContentId);
        return binaryContentMapper.toDto(binaryContent);
    }


    @Override
    @Transactional(readOnly = true)
    public List<BinaryContentDto> findAllByIdIn(UUID[] binaryContentIds) {
        List<BinaryContentDto> dtoList = binaryContentRepository.findAll().stream()
            .filter(content -> List.of(binaryContentIds).contains(content.getId()))
            .map(binaryContentMapper::toDto)
            .peek(dto -> log.info("✅BinaryContentService.findAllByIdIn ok! - binaryContentIds = {}",  dto.id()))
            .toList();

        return dtoList;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public BinaryContentDto updateStatus(UUID binaryContentId, BinaryContentStatus sataus) {
        BinaryContent binaryContent = binaryContentRepository.findById(binaryContentId)
            .orElseThrow(() -> new NoSuchElementException("🚨첨부파일[" + binaryContentId.toString() + "]을 찾을 수 없음"));

        binaryContent.setStatus(sataus);

        return binaryContentMapper.toDto(binaryContent);
    }
}