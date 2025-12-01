package com.sprint.mission.discodeit.controller.Docs;

import com.sprint.mission.discodeit.dto.channel.request.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channel.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.response.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@Tag(name = "채널 관리 (ChannelController)", description = "채널 생성(공개/비공개), 사용자별 채널 조회, 채널 수정/삭제")
public interface ChannelControllerDocs {

    // ---------- 공개 채널 생성 ----------
    @Operation(
            summary = "공개 채널 생성",
            description = """
                    공개(PUBLIC) 채널을 생성합니다.
                    
                    **요청 데이터**
                    - name (채널명)
                    - description (설명, 선택)
                    - ownerId (채널 소유자 UUID)
                    
                    **응답**
                    - 생성된 채널 정보가 반환됩니다.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "공개 채널 생성 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Channel.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "id": "bfa2c8d0-9c6f-4a7a-8b0b-2f6d3e9f12ab",
                                              "createdAt": "2025-11-12T10:15:00.000Z",
                                              "updatedAt": null,
                                              "name": "public-lounge",
                                              "type": "PUBLIC",
                                              "ownerId": "6f01be3c-e8f9-4597-a5b1-02e348db7d36",
                                              "userCount": 1,
                                              "topic": "자유 대화방"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 (필드 누락/형식 오류/중복 이름 등)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            [
                                              "name 은(는) 필수입니다.",
                                              "ownerId 형식이 올바르지 않습니다.",
                                              "이미 존재하는 채널명입니다."
                                            ]
                                            """
                            )
                    )
            )
    })
    ResponseEntity<ChannelDto> create(PublicChannelCreateRequest request);


    // ---------- 비공개 채널 생성 ----------
    @Operation(
            summary = "비공개 채널 생성",
            description = """
                    비공개(PRIVATE) 채널을 생성합니다.
                    
                    **요청 데이터**
                    - name (채널명)
                    - ownerId (소유자 UUID)
                    - participantIds (초대할 사용자 UUID 배열)
                    
                    **응답**
                    - 생성된 채널 정보가 반환됩니다.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "비공개 채널 생성 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Channel.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "id": "0f7f8b3e-1c4d-4e2a-83f5-2a7f9d0e1c2b",
                                              "createdAt": "2025-11-12T10:18:42.000Z",
                                              "updatedAt": null,
                                              "name": "dm-je-won-and-jin",
                                              "type": "PRIVATE",
                                              "ownerId": "6f01be3c-e8f9-4597-a5b1-02e348db7d36",
                                              "userCount": 2,
                                              "topic": null
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 (UUID 형식 오류/참가자 미존재 등)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            [
                                              "ownerId 를 찾을 수 없습니다.",
                                              "participantIds 중 존재하지 않는 사용자가 있습니다."
                                            ]
                                            """
                            )
                    )
            )
    })
    ResponseEntity<ChannelDto> create(PrivateChannelCreateRequest request);


    // ---------- 사용자별 채널 조회 ----------
    @Operation(
            summary = "사용자별 채널 목록 조회",
            description = """
                    특정 사용자(userId)가 속한 채널 목록을 조회합니다.
                    
                    **요청 데이터**
                    - userId (Query/Path Param, UUID)
                    
                    **응답**
                    - ChannelDto 배열이 반환됩니다.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ChannelDto.class)),
                            examples = @ExampleObject(
                                    value = """
                                            [
                                              {
                                                "id": "bfa2c8d0-9c6f-4a7a-8b0b-2f6d3e9f12ab",
                                                "createdAt": "2025-11-12T10:15:00.000Z",
                                                "updatedAt": null,
                                                "name": "public-lounge",
                                                "type": "PUBLIC",
                                                "lastMessageAt": "2025-11-12T10:20:30.000Z",
                                                "unreadCount": 0
                                              },
                                              {
                                                "id": "0f7f8b3e-1c4d-4e2a-83f5-2a7f9d0e1c2b",
                                                "createdAt": "2025-11-12T10:18:42.000Z",
                                                "updatedAt": null,
                                                "name": "dm-je-won-and-jin",
                                                "type": "PRIVATE",
                                                "lastMessageAt": "2025-11-12T10:25:10.000Z",
                                                "unreadCount": 3
                                              }
                                            ]
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 (UUID 형식 오류 등)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            [
                                              "userId 형식이 올바르지 않습니다."
                                            ]
                                            """
                            )
                    )
            )
    })
    ResponseEntity<List<ChannelDto>> findAllByUserId(UUID userId);


    // ---------- 채널 수정 ----------
    @Operation(
            summary = "채널 정보 수정",
            description = """
                    채널 이름/설명/주제 등 메타데이터를 수정합니다.
                    
                    **요청 데이터**
                    - channelId (Path/Query Param, UUID)
                    - name/description/topic 등 (Request Body)
                    
                    **응답**
                    - 수정된 채널 정보가 반환됩니다.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "수정 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Channel.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "id": "bfa2c8d0-9c6f-4a7a-8b0b-2f6d3e9f12ab",
                                              "createdAt": "2025-11-12T10:15:00.000Z",
                                              "updatedAt": "2025-11-12T10:40:05.000Z",
                                              "name": "public-lounge-renamed",
                                              "type": "PUBLIC",
                                              "ownerId": "6f01be3c-e8f9-4597-a5b1-02e348db7d36",
                                              "userCount": 3,
                                              "topic": "잡담방 (수정)"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 (존재하지 않는 ID/형식 오류 등)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            [
                                              "channelId 를 찾을 수 없습니다.",
                                              "name 은 비어 있을 수 없습니다."
                                            ]
                                            """
                            )
                    )
            )
    })
    ResponseEntity<ChannelDto> update(UUID channelId, ChannelUpdateRequest request);


    // ---------- 채널 삭제 ----------
    @Operation(
            summary = "채널 삭제",
            description = """
                    채널을 삭제합니다.
                    
                    **요청 데이터**
                    - ChannelDeleteRequest (channelId 등)
                    
                    **응답**
                    - 성공 시 본문 없는 204(No Content) 응답을 반환합니다.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "삭제 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "삭제 완료 (본문 없음)")
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 (UUID 형식 오류/권한 없음 등)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            [
                                              "channelId 형식이 올바르지 않습니다.",
                                              "삭제 권한이 없습니다."
                                            ]
                                            """
                            )
                    )
            )
    })
    ResponseEntity<Void> delete(UUID channelId);
}

