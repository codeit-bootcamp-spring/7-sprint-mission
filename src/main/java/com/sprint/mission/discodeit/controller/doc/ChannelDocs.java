package com.sprint.mission.discodeit.controller.doc;

import com.sprint.mission.discodeit.dto.channel.request.CreatePrivateChannelDto;
import com.sprint.mission.discodeit.dto.channel.request.CreatePublicChannelDto;
import com.sprint.mission.discodeit.dto.channel.request.UpdateChannelDto;
import com.sprint.mission.discodeit.dto.channel.response.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.response.CreateChannelResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Tag(name = "Channel", description = "Channel API")
public interface ChannelDocs {

    @Operation(summary = "Public Channel 생성")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Public Channel이 성공적으로 생성됨",
                    content = @Content(
                            mediaType = "*/*",
                            schema = @Schema(implementation = CreateChannelResponseDto.class)
                    )
            ),
    })
    ResponseEntity<CreateChannelResponseDto> createChannel(@Parameter(description = "Public Channel 생성 정보")
                                                           @Valid @RequestBody CreatePublicChannelDto createPublicChannelDto);

    @Operation(summary = "Private Channel 생성")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Public Channel이 성공적으로 생성됨",
                    content = @Content(
                            mediaType = "*/*",
                            schema = @Schema(implementation = CreateChannelResponseDto.class)
                    )
            ),
    })
    ResponseEntity<CreateChannelResponseDto> createChannel(@Parameter(description = "Private Channel 생성 정보")
                                                           @Valid @RequestBody CreatePrivateChannelDto createPrivateChannelDto);

    @Operation(summary = "Channel 삭제")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Channel이 성공적으로 삭제됨",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(type = "string", example = "string")
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Channel을 찾을 수 없음",
                    content = @Content(
                            examples = @ExampleObject(value = "Channel with id {channelId} not found")
                    )
            )
    })

    ResponseEntity<Void> deleteChannel(@Parameter(description = "삭제할 Channel ID") @PathVariable UUID channelId);

    @Operation(summary = "Channel 정보 수정")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Channel 정보가 성공적으로 수정됨",
                    content = @Content(
                            mediaType = "*/*",
                            schema = @Schema(implementation = CreateChannelResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Private Channel은 수정할 수 없음",
                    content = @Content(
                            mediaType = "*/*",
                            examples = @ExampleObject(value = "Private channel cannot be updated")
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Channel을 찾을 수 없음",
                    content = @Content(
                            mediaType = "*/*",
                            examples = @ExampleObject(value = "Channel with id {channelId} not found")
                    )
            )
    })
    ResponseEntity<CreateChannelResponseDto> updateChannel(@Parameter(description = "수정할 채널 ID") @PathVariable UUID channelId,
                                                           @Parameter(description = "수정할 Channel 정보") @RequestBody UpdateChannelDto updateChannelDto);


    @Operation(summary = "Channel 목록 조회 성공")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Channel 목록 조회 성공",
                    content = @Content(
                            mediaType = "*/*",
                            schema = @Schema(implementation = ChannelResponseDto.class)
                    )
            ),
    })
    ResponseEntity<List<ChannelResponseDto>> getAllChannelByUser(@RequestParam UUID userId);

}
