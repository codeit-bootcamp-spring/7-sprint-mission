package com.sprint.mission.discodeit.service;

import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

public interface BinaryContentStorage {

    UUID put(UUID id, MultipartFile file);
    InputStream get(UUID id);
    ResponseEntity<UrlResource> getUrl(UUID id);
    
}
