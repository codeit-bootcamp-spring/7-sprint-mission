package com.sprint.mission.discodeit.service.binarycontent;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.binarycontent.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class BinaryContentManager {

    private final BinaryContentStorage binaryContentStorage;
    private final BinaryContentRepository binaryContentRepository;

    public BinaryContent saveFileAndMeta(MultipartFile file) {
        UUID id = UUID.randomUUID();
        try {
            binaryContentStorage.put(id, file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        BinaryContent binaryContent = new BinaryContent(
                id.toString(),
                file.getContentType(),
                file.getSize());
        binaryContentRepository.save(binaryContent);
        return binaryContentRepository.save(binaryContent);
    }

    private void validateContentType(String contentType) {
        if (contentType == null ||
                !(contentType.equals("image/png")
                        || contentType.equals("image/jpeg")
                        || contentType.equals("image/gif"))) {
            throw new IllegalArgumentException("허용되지 않는 파일 형식입니다: " + contentType);
        }
    }


    public void deleteFile(BinaryContent binaryContent) {
        String fileName = binaryContent.getFileName();
        binaryContentStorage.deleteFile(fileName);
        binaryContentRepository.delete(binaryContent);
    }

    public BinaryContent getBinaryContent(UUID binaryContentId){
        return binaryContentRepository.findById(binaryContentId).orElseThrow(() -> new BinaryContentNotFoundException(ErrorCode.BINARY_CONTENT_NOT_FOUND, new HashMap<>()));
    }


    public List<BinaryContent> getBinaryContents(List<UUID> ids){
        return binaryContentRepository.findAllById(ids);
    }

    public ResponseEntity<UrlResource> getUrl(UUID binaryContentId) {
        BinaryContent binaryContent = binaryContentRepository.findById(binaryContentId).orElseThrow(() -> new BinaryContentNotFoundException(ErrorCode.BINARY_CONTENT_NOT_FOUND, new HashMap<>()));
        UrlResource urlResource = binaryContentStorage.getUrlResource(binaryContent.getFileName());

        String contentType = binaryContent.getFileType();
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + binaryContent.getFileName() + "\"")
                //attachment->inline
                .body(urlResource);
    }
}
