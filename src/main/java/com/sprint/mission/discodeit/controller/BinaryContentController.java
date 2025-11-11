package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.response.binarycontent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.service.BinaryContentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("/api/binaryContents")
@RequiredArgsConstructor
public class BinaryContentController {
    private final BinaryContentService binaryContentService;

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BinaryContentResponseDto create(
            @Valid @RequestBody BinaryContentCreateRequestDto binaryContentCreateRequestDto) {
        return binaryContentService.create(binaryContentCreateRequestDto);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BinaryContentResponseDto upload(@RequestPart MultipartFile file) {

        BinaryContentCreateRequestDto dto = BinaryContentCreateRequestDto.from(file);
        return binaryContentService.create(dto);
    }

    @RequestMapping(value = "/{binaryContentId}", method = RequestMethod.GET)
    public BinaryContentResponseDto get(@PathVariable("binaryContentId") UUID binaryContentId) {
        return binaryContentService.findById(binaryContentId)
                .orElseThrow(() -> new NoSuchElementException("binaryContent with id " + binaryContentId + " not found"));
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<BinaryContentResponseDto> getAllById(@RequestParam("binaryContentIds") List<UUID> binaryContentIds) {
        return binaryContentService.findAllByIdIn(binaryContentIds);
    }

    @RequestMapping(value = "/{binaryContentId}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("binaryContentId") UUID binaryContentId) {
        boolean delete = binaryContentService.delete(binaryContentId);
        if (!delete) {
            throw new NoSuchElementException("binaryContent with id " + binaryContentId + " not found");
        }
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
