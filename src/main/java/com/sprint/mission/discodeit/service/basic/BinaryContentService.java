package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.dto.Dto_BinaryContent;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.mapper.dto.BinaryContentDto;
import com.sprint.mission.discodeit.repository.jpa.BinaryContentsRepository;
import com.sprint.mission.discodeit.service.InterfaceBinaryContentService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional // 영속성 컨텍스트
@RequiredArgsConstructor
public class BinaryContentService implements InterfaceBinaryContentService {
    private final BinaryContentsRepository binaryContentRepository;
    private final BinaryContentMapper binaryContentMapper;

    //?? 안쓰남??
//    public BinaryContentDto create(Dto_BinaryContent dtoBinaryContent) {
////    [ ] DTO를 활용해 파라미터를 그룹화합니다.
//        BinaryContent binaryContent = new BinaryContent(dtoBinaryContent.);
//
//        binaryContentRepository.save(binaryContent);
//
//        log.info("✅ BinaryContentService.create = [" + binaryContent.getId() + "]");
//        return binaryContentMapper.toDto(binaryContent);
//    }

    public  BinaryContentDto find(UUID binaryContentId) {
//    [ ] id로 조회합니다.
        BinaryContent binaryContent = binaryContentRepository.findById(binaryContentId)
            .orElseThrow(() -> new NoSuchElementException("🚨첨부파일[" + binaryContentId.toString() + "]을 찾을 수 없음"));

        log.info("✅ BinaryContentService.find.binaryContentId = [" + binaryContentId.toString() + "]");
        return binaryContentMapper.toDto(binaryContent);
    }

    public List<BinaryContentDto> findAllByIdIn(UUID[] binaryContentIds) {
        List<BinaryContentDto> dtoList = binaryContentRepository.findAll().stream()
            .filter(content -> List.of(binaryContentIds).contains(content.getId()))
            .map(binaryContentMapper::toDto)
            .toList();

        log.info("✅ BinaryContentService.findAllByIdIn");
        return dtoList;
    }

    //?? 안쓰남??
//    public void delete(UUID binaryContentId) {
////    [ ] id로 삭제합니다.
//        binaryContentRepository.deleteById(binaryContentId);
//        log.info("✅ BinaryContentService.delete.readStatusID = [" + binaryContentId.toString() + "]");
//    }

    public void download(UUID binaryContentId) {
        //💎🌱 파일 다운로드
    }
}