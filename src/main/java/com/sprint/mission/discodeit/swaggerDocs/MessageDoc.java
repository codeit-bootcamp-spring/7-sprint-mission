package com.sprint.mission.discodeit.swaggerDocs;

import com.sprint.mission.discodeit.dto.Dto_MessageUpdate;
import com.sprint.mission.discodeit.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.Res_Message;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Tag(name="Message", description="Message API")
public interface MessageDoc {

    /**
     * GET /api/messages - Channel의 Message 목록 조회
     */
    @Operation(summary = "Channel의 Message 목록 조회", description = "특정 Channel에 속한 Message들을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Message 목록 조회 성공",
            content = @Content(
                schema = @Schema(implementation = Res_Message.class, type = "array")
            )
        )
    })

    ResponseEntity<List<Res_Message>> findAllByChannelId(
        @Parameter(description = "조회할 Channel ID") @RequestParam("channelId") UUID channelID);
    /**
     * POST /api/messages - Message 생성 (첨부 파일 포함)
     * Multipart 요청을 위한 문서화입니다.
     */
    @Operation(
        summary = "Message 생성 (첨부 파일 포함)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Message가 성공적으로 생성됨",
            content = @Content(
                schema = @Schema(implementation = Res_Message.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Channel 또는 Author를 찾을 수 없음",
            content = @Content(
                examples = @ExampleObject(value = "Channel | Author with id {channelId | authorId} not found")
            )
        )
    })
    ResponseEntity<Res_Message> create(
        @Parameter(description = "Message 생성 정보 (JSON)") @RequestPart(value = "messageCreateRequest") MessageCreateRequest dtoMessage,
        @Parameter(description = "첨부 파일 목록 (선택 사항)") @RequestPart(value = "attachments", required = false) List<MultipartFile> fileList);


    /**
     * DELETE /api/messages/{messageId} - Message 삭제
     */
    @Operation(summary = "Message 삭제")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Message가 성공적으로 삭제됨"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Message를 찾을 수 없음",
            content = @Content(
                examples = @ExampleObject(value = "Message with id {messageId} not found")
            )
        )
    })
    ResponseEntity<Object> deleteMessage(
        @Parameter(description = "삭제할 Message ID") @PathVariable("messageId") UUID messageId);


    /**
     * PATCH /api/messages/{messageId} - Message 내용 수정
     */
    @Operation(summary = "Message 내용 수정")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Message가 성공적으로 수정됨",
            content = @Content(
                schema = @Schema(implementation = Res_Message.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Message를 찾을 수 없음",
            content = @Content(
                examples = @ExampleObject(value = "Message with id {messageId} not found")
            )
        )
    })
    ResponseEntity<Res_Message> updateMessage(
        @Parameter(description = "수정할 Message ID") @PathVariable("messageId") UUID messageId,
        @RequestBody Dto_MessageUpdate requestDto);
}