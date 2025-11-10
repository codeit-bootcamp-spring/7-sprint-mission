package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.application.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/api/binaryContents")
@RequiredArgsConstructor
public class BinaryContentController {

    private final FileService fileService;

    @GetMapping("/{id}")
    public Resource getFile(@PathVariable UUID id) throws MalformedURLException {
        return fileService.getImageFile(id);
    }

    @GetMapping
    public ResponseEntity<List<Resource>> getFiles(@RequestParam List<UUID> ids) throws MalformedURLException {
        List<Resource> result = new ArrayList<>();
        for (UUID id : ids) {
            result.add(fileService.getImageFile(id));
        }
        return ResponseEntity.ok(result);
    }

}
