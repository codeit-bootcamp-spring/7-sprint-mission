package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.response.binarycontent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/binaryContents")
@RequiredArgsConstructor
@Slf4j
public class BinaryContentController {
    private final BinaryContentService binaryContentService;
    private final BinaryContentStorage binaryContentStorage;

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BinaryContentResponseDto create(
            @Valid @RequestBody BinaryContentCreateRequestDto binaryContentCreateRequestDto) {
        log.debug("Received request to create binaryContent.");
        return binaryContentService.create(binaryContentCreateRequestDto);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BinaryContentResponseDto upload(@RequestPart MultipartFile file) {
        log.debug("Received request to upload binaryContent.");
        BinaryContentCreateRequestDto dto = BinaryContentCreateRequestDto.from(file);
        return binaryContentService.create(dto);
    }

    @RequestMapping(value = "/{binaryContentId}", method = RequestMethod.GET)
    public BinaryContentResponseDto get(@PathVariable("binaryContentId") UUID binaryContentId) {
        log.debug("Received request to get binaryContent.");
        return binaryContentService.findById(binaryContentId);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<BinaryContentResponseDto> getAllById(
            @RequestParam("binaryContentIds") List<UUID> binaryContentIds) {
        log.debug("Received request to get all binaryContents.");
        return binaryContentService.findAllByIdIn(binaryContentIds);
    }

    @RequestMapping(value = "/{binaryContentId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("binaryContentId") UUID binaryContentId) {
        log.debug("Received request to delete binaryContent.");
        boolean delete = binaryContentService.delete(binaryContentId);
    }

    @RequestMapping(value = "/{binaryContentId}/download", method = RequestMethod.GET)
    public ResponseEntity<Resource> download(@PathVariable("binaryContentId") UUID binaryContentId) {
        log.debug("Received request to download binaryContent.");
        BinaryContentResponseDto binaryContentDto = binaryContentService.findById(binaryContentId);
        return binaryContentStorage.download(binaryContentDto);
    }
    /*
    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public ResponseEntity<BinaryContentResponseDto> find(
            @RequestParam("binaryContentId") UUID binaryContentId) {
        BinaryContent binaryContent = binaryContentService.findById(binaryContentId)
                .orElseThrow(() -> new NoSuchElementException("BinaryContent not found"));
        return ResponseEntity.ok(BinaryContentResponseDto.from(binaryContent));
    }

     */
}
