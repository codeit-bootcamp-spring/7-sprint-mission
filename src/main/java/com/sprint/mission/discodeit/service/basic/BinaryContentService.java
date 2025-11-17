package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.dto.Dto_BinaryContent;
import com.sprint.mission.discodeit.entity.dto.Res_BinaryContent;
import com.sprint.mission.discodeit.repository.BaseInterfaceRepository;
import com.sprint.mission.discodeit.repository.file.FileBinaryContentRepository;
import com.sprint.mission.discodeit.service.InterfaceBinaryContentService;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BinaryContentService implements InterfaceBinaryContentService {
    private final BaseInterfaceRepository<BinaryContent> binaryContentRepository;

    public Res_BinaryContent create(Dto_BinaryContent dtoBinaryContent) {
//    [ ] DTO를 활용해 파라미터를 그룹화합니다.
        BinaryContent binaryContent = new BinaryContent(dtoBinaryContent);

        binaryContentRepository.save(binaryContent);

        log.info("✅ BinaryContentService.create = [" + binaryContent.getId() + "]");
        return Res_BinaryContent.from(binaryContent);
    }

    public  Res_BinaryContent find(UUID binaryContentId) {
//    [ ] id로 조회합니다.
        BinaryContent binaryContent = binaryContentRepository.findById(binaryContentId)
            .orElseThrow(() -> new NoSuchElementException("🚨첨부파일[" + binaryContentId.toString() + "]을 찾을 수 없음"));

        log.info("✅ BinaryContentService.find.binaryContentId = [" + binaryContentId.toString() + "]");
        return Res_BinaryContent.from(binaryContent);
    }

    public List<Res_BinaryContent> findAllByIdIn(UUID[] binaryContentIds) {
        List<Res_BinaryContent> list = binaryContentRepository.findAll().stream()
            .filter(content -> List.of(binaryContentIds).contains(content.getId()))
            .map(Res_BinaryContent::from)
            .toList();

        log.info("✅ BinaryContentService.findAllByIdIn");
        return list;
    }

    public void delete(UUID binaryContentId) {
//    [ ] id로 삭제합니다.
        binaryContentRepository.deleteById(binaryContentId);
        log.info("✅ BinaryContentService.delete.readStatusID = [" + binaryContentId.toString() + "]");
    }
}