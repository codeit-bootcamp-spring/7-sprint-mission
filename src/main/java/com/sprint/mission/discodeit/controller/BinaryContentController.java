package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.api.BinaryContentApi;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentUploadCommand;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/binaryContents")
@RequiredArgsConstructor
public class BinaryContentController implements BinaryContentApi {

    private final BinaryContentService binaryContentService;
    private final BinaryContentStorage binaryContentStorage;

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<BinaryContentResponseDto> getBinaryContent(
            @PathVariable UUID id
    ) {
        log.info("binary content 조회 요청 id={}", id);
        BinaryContentResponseDto binaryContent = binaryContentService.getBinaryContent(id);
        log.info("binary content 조회 성공 binaryId={}", binaryContent.id());

        log.debug("binary content 메타정보 id={} fileName={} contentType={} size={}",
                binaryContent.id(),
                binaryContent.fileName(),
                binaryContent.contentType(),
                binaryContent.size()
        );
        return ResponseEntity.ok(binaryContent);
    }

    @Override
    @GetMapping
    public ResponseEntity<List<BinaryContentResponseDto>> getAllBinaryContentsByIds(
            @RequestParam(required = false) List<UUID> ids
    ) {
        log.debug("binary content 리스트 조회 요청 ids={}", ids);
        List<BinaryContentResponseDto> binaryContents;
        if (ids == null || ids.isEmpty()) {
            binaryContents = binaryContentService.getAllBinaryContents();
        } else {
            binaryContents = binaryContentService.getBinaryContentsByIds(ids);
        }
        log.info("binary content 리스트 조회 성공 count={}", binaryContents.size());
        log.debug("binary content 리스트 반환 binaryIds={}", binaryContents.stream().map(BinaryContentResponseDto::id).toList());  // NOTE: ids가 너무많아 log 터질수있어 Debug 설정
        return ResponseEntity.ok(binaryContents);
    }

    @Override
    @GetMapping("/{binaryContentId}/download")
    public ResponseEntity<?> downloadBinaryContent(@PathVariable("binaryContentId") UUID id) {
        log.info("binary content 다운로드 요청 id={}", id);
        BinaryContentResponseDto responseDto = binaryContentService.getBinaryContent(id);
        log.info("binary content 다운로드 처리 시작 id={}", responseDto.id());
        log.debug("binary content 응답 메타정보 binaryId={} fileName={} contentType={} size={}",
                responseDto.id(),
                responseDto.fileName(),
                responseDto.contentType(),
                responseDto.size()
        );
        return binaryContentStorage.download(responseDto);
    }

    @Override
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UUID> createBinaryContent(
            @RequestPart MultipartFile file,
            @AuthenticationPrincipal DiscodeitUserDetails userDetails
    ) {
        log.debug("binary content 생성 요청 fileName={} size={} contentType={}",
                file.getOriginalFilename(),
                file.getSize(),
                file.getContentType()
        );
        UUID userId = userDetails.getUserResponseDto().id();
        BinaryContentUploadCommand fileCommand = BinaryContentUploadCommand.from(file, userId);
        UUID binaryId = binaryContentService.uploadBinaryContent(fileCommand);
        log.debug("binary content 생성 성공 binaryId={}", binaryId);
        return ResponseEntity.ok(binaryId);
    }
}
