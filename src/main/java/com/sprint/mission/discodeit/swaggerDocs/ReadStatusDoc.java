package com.sprint.mission.discodeit.swaggerDocs;

import com.sprint.mission.discodeit.entity.dto.Dto_ReadStatus;
import com.sprint.mission.discodeit.entity.dto.Dto_ReadStatusUpdate;
import com.sprint.mission.discodeit.entity.dto.Res_ReadStatus;
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

@Tag(name="ReadStatus", description="Message 읽음 상태 API")
public interface ReadStatusDoc {
    @Operation(summary = "User의 Message 읽음 상태 목록 조회")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Message 읽음 상태 목록 조회 성공",
            content = @Content(
                schema = @Schema(implementation = Res_ReadStatus.class)
            )
        )
    })
    ResponseEntity<List<Res_ReadStatus>> findAllByUserId(
        @Parameter(description = "조회할 User ID")
        @RequestParam UUID userId);

    //===================================================
    @Operation(summary = "Message 읽음 상태 생성")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Message 읽음 상태가 성공적으로 생성됨",
            content = @Content(
                schema = @Schema(implementation = Res_ReadStatus.class)
            )
        ),

        @ApiResponse(
            responseCode = "400",
            description = "이미 읽음 상태가 존재함",
            content = @Content(
                examples = @ExampleObject(value = "ReadStatus with userId {userId} and channelId {channelId} already exists")
            )
        ),

        @ApiResponse(
            responseCode = "404",
            description = "Channel 또는 User를 찾을 수 없음",
            content = @Content(
                examples = @ExampleObject(value = "Channel | User with id {channelId | userId} not found")
            )
        )
    })
    ResponseEntity<Res_ReadStatus> create(
//        @Parameter(description = "조회할 User ID")
        @RequestBody Dto_ReadStatus dtoReadStatus);

    //===================================================
    @Operation(summary = "Message 읽음 상태 수정")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Message 읽음 상태가 성공적으로 수정됨",
            content = @Content(
                schema = @Schema(implementation = Res_ReadStatus.class)
            )
        ),

        @ApiResponse(
            responseCode = "404",
            description = "Message 읽음 상태를 찾을 수 없음",
            content = @Content(
                examples = @ExampleObject(value = "ReadStatus with id {readStatusId} not found")
            )
        )
    })
    ResponseEntity<Res_ReadStatus> update(
        @Parameter(description = "수정할 읽음 상태 ID")
        @PathVariable("readStatusId") UUID readStatusId,
        @RequestBody Dto_ReadStatusUpdate requestDto);
}
