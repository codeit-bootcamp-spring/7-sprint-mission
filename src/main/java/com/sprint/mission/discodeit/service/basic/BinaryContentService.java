package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.Util;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.dto.Dto_BinaryContent;
import com.sprint.mission.discodeit.entity.dto.Res_BinaryContent;
import com.sprint.mission.discodeit.repository.file.FileBinaryContentRepository;
import com.sprint.mission.discodeit.service.InterfaceBinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BinaryContentService implements InterfaceBinaryContentService {
    private final FileBinaryContentRepository binaryContentRepository;

    public Res_BinaryContent create(Dto_BinaryContent dtoBinaryContent) {
//    [ ] DTO를 활용해 파라미터를 그룹화합니다.
        BinaryContent binaryContent = new BinaryContent(dtoBinaryContent);
        binaryContentRepository.save(binaryContent);
        Util.okMessage("BinaryContentService.create = [" + binaryContent.getId() + "]");
        return Res_BinaryContent.from(binaryContent);
    }

    public  Res_BinaryContent find(UUID binaryContentId) {
//    [ ] id로 조회합니다.
        BinaryContent binaryContent = binaryContentRepository.findById(binaryContentId).orElseThrow(() -> new IllegalArgumentException("🚨BinaryContentService.find.binaryContentId = [" + binaryContentId + "] 오류"));
        Util.okMessage("BinaryContentService.find.binaryContentId = [" + binaryContentId + "]");
        return Res_BinaryContent.from(binaryContent);
    }

    public List<Res_BinaryContent> findAllByIdIn() {
//    [ ] readStatusID 목록으로 조회합니다.
        Optional<List<BinaryContent>> contents = binaryContentRepository.findAll();
        if (contents.isEmpty()) {
            throw new IllegalArgumentException("🚨BinaryContentService.findAllByIdIn.isEmpty");
        }
        else {
            Util.okMessage("BinaryContentService.findAllByIdIn");
            return contents.get().stream().map(Res_BinaryContent::from).toList();
        }
    }

    public void delete(UUID binaryContentId) {
//    [ ] id로 삭제합니다.
        binaryContentRepository.deleteById(binaryContentId);
        Util.okMessage("BinaryContentService.delete.readStatusID = [" + binaryContentId + "]");
    }
}