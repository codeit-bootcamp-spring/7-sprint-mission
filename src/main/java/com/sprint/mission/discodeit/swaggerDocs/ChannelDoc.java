package com.sprint.mission.discodeit.swaggerDocs;

import com.sprint.mission.discodeit.dto.ChannelDto_Update;
import com.sprint.mission.discodeit.dto.Dto_CreateChannelPrivate;
import com.sprint.mission.discodeit.dto.Dto_CreateChannelPublic;
import com.sprint.mission.discodeit.dto.Res_Channel;
import com.sprint.mission.discodeit.dto.Res_ChannelFind;
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

@Tag(name="Channel", description="Channel API")
public interface ChannelDoc {

    /**
     * GET /api/channels - User가 참여 중인 Channel 목록 조회
     */
    @Operation(summary = "User가 참여 중인 Channel 목록 조회", description = "특정 User ID가 참여하고 있는 채널 목록을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Channel 목록 조회 성공",
            content = @Content(
                schema = @Schema(implementation = Res_ChannelFind.class, type = "array")
            )
        )
    })
    ResponseEntity<List<Res_ChannelFind>> findAllByUserId(
        @Parameter(description = "조회할 User ID") @RequestParam("userId") UUID userId);

    /**
     * POST /api/channels/public - Public Channel 생성
     */
    @Operation(summary = "Public Channel 생성")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Public Channel이 성공적으로 생성됨",
            content = @Content(
                schema = @Schema(implementation = Res_Channel.class)
            )
        )
    })
    ResponseEntity<Res_Channel> createPublic(
        @RequestBody Dto_CreateChannelPublic dtoCreateChannel);

    /**
     * POST /api/channels/private - Private Channel 생성
     */
    @Operation(summary = "Private Channel 생성")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Private Channel이 성공적으로 생성됨",
            content = @Content(
                schema = @Schema(implementation = Res_Channel.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "참여자 중 존재하지 않는 User가 있음",
            content = @Content(
                examples = @ExampleObject(value = "User with id {userId} not found")
            )
        )
    })
    ResponseEntity<Res_Channel> createPrivate(
        @RequestBody Dto_CreateChannelPrivate dtoCreateChannel);

    /**
     * DELETE /api/channels/{channelId} - Channel 삭제
     */
    @Operation(summary = "Channel 삭제")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Channel이 성공적으로 삭제됨"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Channel을 찾을 수 없음",
            content = @Content(
                examples = @ExampleObject(value = "Channel with id {channelId} not found")
            )
        )
    })
    ResponseEntity<Object> delete(
        @Parameter(description = "삭제할 Channel ID") @PathVariable("channelId") UUID channelId);

    /**
     * PATCH /api/channels/{channelId} - Channel 정보 수정
     */
    @Operation(summary = "Channel 정보 수정")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Channel 정보가 성공적으로 수정됨",
            content = @Content(
                schema = @Schema(implementation = Res_Channel.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Channel을 찾을 수 없음",
            content = @Content(
                examples = @ExampleObject(value = "Channel with id {channelId} not found")
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Private Channel은 수정할 수 없음",
            content = @Content(
                examples = @ExampleObject(value = "Private channel cannot be updated")
            )
        )
    })
    ResponseEntity<Res_Channel> update(
        @Parameter(description = "수정할 Channel ID") @PathVariable("channelId") UUID channelId,
        @RequestBody ChannelDto_Update channelDtoUpdate);
}