package com.sprint.mission.discodeit.controller.docs;

import com.sprint.mission.discodeit.dto.entity.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.entity.channel.request.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.entity.channel.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.entity.channel.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.entity.channel.response.ChannelParticipantIdsResponse;
import io.swagger.v3.oas.annotations.Operation;
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

@Tag(name = "채널 관리", description = "채널 생성, 삭제, 수정, 조회 등을 담당하는 API입니다.")
public interface ChannelControllerDocs {
    @Operation(
            summary = "Public 채널 생성",
            description = "새로운 공개 채널을 생성합니다"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "채널 생성 성공")
    })
    ResponseEntity<ChannelDto> createPublic(@Valid @RequestBody PublicChannelCreateRequest request);

    @Operation(
            summary = "Private 채널 생성",
            description = "새로운 비공개 채널을 생성합니다. 참여자 ID 목록을 반환합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "채널 생성 성공")
    })
    ResponseEntity<ChannelParticipantIdsResponse> createPrivate(@Valid @RequestBody PrivateChannelCreateRequest request);

    @Operation(
            summary = "채널 수정",
            description = "기존 채널의 정보를 수정합니다"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "채널 수정 성공")
    })
    ResponseEntity<ChannelDto> updatePublic(@PathVariable UUID channelId, @Valid @RequestBody ChannelUpdateRequest request);

    @Operation(
            summary = "채널 삭제",
            description = "채널을 삭제합니다"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "채널 삭제 성공")
    })
    ResponseEntity<Void> removeChannel(@PathVariable UUID channelId);

    @Operation(
            summary = "사용자가 볼 수 있는 채널 조회",
            description = "특정 사용자가 접근 가능한 모든 채널을 조회합니다"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    ResponseEntity<List<ChannelDto>> getVisibleChannel(@RequestParam UUID userId);
}
