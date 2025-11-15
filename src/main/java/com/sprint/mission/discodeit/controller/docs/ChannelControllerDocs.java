package com.sprint.mission.discodeit.controller.docs;

import com.sprint.mission.discodeit.service.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.service.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.service.dto.request.ChannelUpdateRequest;
import com.sprint.mission.discodeit.service.dto.response.ChannelResponse;
import com.sprint.mission.discodeit.service.dto.response.ChannelListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.UUID;

@Tag(name = "Channel")
public interface ChannelControllerDocs {


    @Operation(summary = "Public Channel 생성")
    @ApiResponse(responseCode = "201", description = "Public Channel 생성 성공",
            content = @Content(
                    schema = @Schema(implementation = ChannelResponse.class)))
    ChannelResponse createPublicChannel(PublicChannelCreateRequest publicChannelCreateRequest);


    @Operation(summary = "Private Channel 생성")
    @ApiResponse(responseCode = "201", description = "Private Channel 생성 성공",
    content = @Content(schema = @Schema(implementation = ChannelResponse.class)))
    ChannelResponse createPrivateChannel(PrivateChannelCreateRequest publicChannelCreateRequest);


    @Operation(summary = "채널 목록 조회", description = "Public 채널은 모두 조회, Private 채널은 채널 멤버만 조회가능")
    @ApiResponse(responseCode = "200", description = "채널 조회 성공"
            )
    List<ChannelListResponse> getAllChannelByUserId(UUID userId);


    @Operation(summary = "채널 삭제")
    String removeChannel(UUID channelId);


    @Operation(summary = "채널 정보 수정")
    ChannelResponse updateChannel(UUID channelId, ChannelUpdateRequest request);
}
