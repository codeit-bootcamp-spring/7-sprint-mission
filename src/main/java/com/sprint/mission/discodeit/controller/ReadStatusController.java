package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.swaggerDocs.ReadStatusDoc;
import com.sprint.mission.discodeit.entity.dto.Dto_ReadStatus;
import com.sprint.mission.discodeit.entity.dto.Dto_ReadStatusUpdate;
import com.sprint.mission.discodeit.entity.dto.Res_ReadStatus;
import com.sprint.mission.discodeit.service.basic.ReadStatusService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController //👍 @controller + @responsebody
@RequiredArgsConstructor
@RequestMapping("/api/readStatuses")
public class ReadStatusController implements ReadStatusDoc {
    private final ReadStatusService readStatusService;

    @GetMapping
    public ResponseEntity<List<Res_ReadStatus>> findAllByUserId(
        @RequestParam UUID userId) {

        //💎User의 Message 읽음 상태 목록 조회
        List<Res_ReadStatus> allByUserId
            = readStatusService.findAllByUserId(userId);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(allByUserId);
    }

    @PostMapping
    public ResponseEntity<Res_ReadStatus> create(
        @RequestBody Dto_ReadStatus dtoReadStatus) {

        //💎Message 읽음 상태 생성
        Res_ReadStatus resReadStatus
            = readStatusService.create(dtoReadStatus);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(resReadStatus);
    }

    @PatchMapping("/{readStatusId}")
    public ResponseEntity<Res_ReadStatus> update(
        @PathVariable("readStatusId") UUID readStatusId,
        @RequestBody Dto_ReadStatusUpdate requestDto) {

        //💎Message 읽음 상태 수정
        Res_ReadStatus resReadStatus
            = readStatusService.update(readStatusId, requestDto);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(resReadStatus);
    }
}
