package com.sprint.mission.discodeit.service.binarycontent;

import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

public interface BinaryContentStorage {

    UUID put(MultipartFile file);
    InputStream get(UUID id);
    UrlResource getUrlResource(String fileName);

    void deleteFile(String fileName);
    
}
