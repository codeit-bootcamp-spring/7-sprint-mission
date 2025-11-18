package com.sprint.mission.discodeit.controller.docs;

import com.sprint.mission.discodeit.dto.message.request.MessageCreateRequestV2;
import com.sprint.mission.discodeit.dto.message.request.MessageEditRequest;
import com.sprint.mission.discodeit.dto.message.response.MessageResponseV2;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Tag(name = "메시지 관리", description = "메시지 전송, 조회, 수정, 삭제 등을 담당하는 API입니다.")
public interface MessageControllerDocs {
    @Operation(
            summary = "모든 메시지 조회",
            description = "시스템의 모든 메시지를 조회합니다"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    ResponseEntity<List<MessageResponseV2>> getAll();

    @Operation(
            summary = "채널별 메시지 조회",
            description = "특정 채널의 모든 메시지를 조회합니다"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    ResponseEntity<List<MessageResponseV2>> getByChannelId(@Valid @RequestParam UUID channelId);

    @Operation(
            summary = "메시지 전송",
            description = "새로운 메시지를 전송합니다. 첨부파일은 선택사항입니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "메시지 전송 성공")
    })
    ResponseEntity<MessageResponseV2> send(@Valid @RequestPart MessageCreateRequestV2 messageCreateRequest, @RequestPart(required = false) List<MultipartFile> attachments);

    @Operation(
            summary = "메시지 삭제",
            description = "메시지를 삭제합니다"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "메시지 삭제 성공")
    })
    ResponseEntity<Void> remove(@RequestParam UUID messageId);

    @Operation(
            summary = "메시지 수정",
            description = "기존 메시지의 내용을 수정합니다"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "메시지 수정 성공")
    })
    ResponseEntity<MessageResponseV2> edit(@RequestParam UUID messageId, @Valid @RequestBody MessageEditRequest request);
}