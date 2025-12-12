package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.binarycontent.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.dto.response.BinaryContentDto;
import com.sprint.mission.discodeit.service.mapper.BinaryContentMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BinaryContentService {


    private final FileManager fileManager;
    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentMapper mapper;


    @Transactional
    public BinaryContent put(UUID userId, MultipartFile profile) {

        log.info("BinaryService.put");
        BinaryContent content =
                new BinaryContent(
                        "temp",
                        "temp",
                        profile.getContentType(),
                        profile.getSize()
                );

        BinaryContent saved = binaryContentRepository.save(content);

        Path filePath = fileManager.put(userId, profile, saved.getId().toString());
        saved.updateFileName(saved.getId().toString());
        saved.updateFilePath(filePath.toString());


        return saved;
    }


    public void deleteFile(UUID binaryContentId) {
        BinaryContent binaryContent = binaryContentRepository.findById(binaryContentId).orElseThrow(() -> new BinaryContentNotFoundException(ErrorCode.BINARY_CONTENT_NOT_FOUND, new HashMap<>()));
        binaryContentRepository.delete(binaryContent);
        fileManager.delete(Paths.get(binaryContent.getFilePath()));
    }


    @Transactional
    public List<BinaryContentDto> getBinaryContents(List<UUID> ids) {

        log.info("BinaryService.getBinaryContents");
        return binaryContentRepository.findAllById(ids)
                .stream()
                .map(mapper::toDto)
                .toList();

    }

    public BinaryContentDto getBinaryContent(UUID binaryContentId) {
        BinaryContent binaryContent = binaryContentRepository.findById(binaryContentId).orElseThrow(() -> new BinaryContentNotFoundException(ErrorCode.BINARY_CONTENT_NOT_FOUND, new HashMap<>()));


        return mapper.toDto(binaryContent);
    }

    public ResponseEntity<UrlResource> getUrl(UUID binaryContentId) {
        return fileManager.getUrl(binaryContentId);
    }

}



