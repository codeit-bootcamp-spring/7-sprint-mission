package com.sprint.mission.discodeit.service.binarycontent;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.binarycontent.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.service.dto.response.BinaryContentDto;
import com.sprint.mission.discodeit.service.mapper.BinaryContentMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BinaryContentService {

    private final BinaryContentManager binaryContentManager;
    private final BinaryContentMapper mapper;

    public List<BinaryContentDto> getBinaryContents(List<UUID> ids) {
        log.info("BinaryService.getBinaryContents");
        return binaryContentManager.getBinaryContents(ids)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public BinaryContentDto getBinaryContent(UUID binaryContentId) {
        BinaryContent binaryContent = binaryContentManager.getBinaryContent(binaryContentId);
        return mapper.toDto(binaryContent);
    }

    public ResponseEntity<UrlResource> getUrl(UUID binaryContentId) {
        return binaryContentManager.getUrl(binaryContentId);
    }

}



