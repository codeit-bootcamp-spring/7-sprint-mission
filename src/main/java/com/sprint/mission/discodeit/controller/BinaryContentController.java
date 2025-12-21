package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.common.exception.binarycontent.InvalidBinaryContentRequestException;
import com.sprint.mission.discodeit.dto.request.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.response.binarycontent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
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

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BinaryContentResponseDto create(
            @Valid @RequestBody BinaryContentCreateRequestDto binaryContentCreateRequestDto) {
        log.debug("Binary Content create(json) request received. fileName = {}, contentType = {}, size = {}",
                binaryContentCreateRequestDto.fileName(), binaryContentCreateRequestDto.contentType(),
                binaryContentCreateRequestDto.data() == null ? 0 : binaryContentCreateRequestDto.data().length);
        return binaryContentService.create(binaryContentCreateRequestDto);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BinaryContentResponseDto upload(@RequestPart MultipartFile file) {
        log.debug("File upload received. OriginalFileName = {}, contentType = {}, size = {}",
                file.getOriginalFilename(), file.getContentType(), file.getSize());
        BinaryContentCreateRequestDto dto = BinaryContentCreateRequestDto.from(file);
        return binaryContentService.create(dto);
    }

    @RequestMapping(value = "/{binaryContentId}", method = RequestMethod.GET)
    public BinaryContentResponseDto get(@PathVariable("binaryContentId") UUID binaryContentId) {
        return binaryContentService.findById(binaryContentId);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<BinaryContentResponseDto> getAllById(
            @RequestParam("binaryContentIds") List<UUID> binaryContentIds) {
        return binaryContentService.findAllByIdIn(binaryContentIds);
    }

    @RequestMapping(value = "/{binaryContentId}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID binaryContentId) {
        binaryContentService.delete(binaryContentId);
    }

    @RequestMapping(value = "/{binaryContentId}/download", method = RequestMethod.GET)
    public ResponseEntity<Resource> download(@PathVariable("binaryContentId") UUID binaryContentId) {
        return binaryContentService.download(binaryContentId);
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
