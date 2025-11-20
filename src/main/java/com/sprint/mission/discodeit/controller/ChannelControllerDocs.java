package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.channel.ChannelPatchRequestDto;
import com.sprint.mission.discodeit.dto.request.channel.ChannelPrivateCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.channel.ChannelPublicCreateRequestDto;
import com.sprint.mission.discodeit.dto.response.ChannelReadResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
@Tag(name = "채널 관리(Channel Controller)",
description = "채널을 조회,생성, 삭제, 업데이트를 관리하는 API 입니다."
)
public interface ChannelControllerDocs {

    @Operation(
            summary = "private 채널 생성(Create private Channel)",
            description = """
                    채널을 생성합니다.
    
                    - 성공시 생성한 채널의 정보를 반환합니다
                    """
    )
    @ApiResponse(responseCode = "201", description = "private 채널 생성 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ChannelReadResponseDto.class),
                    examples = @ExampleObject(
                            value = """
                                    {
                                    "id" : "550e8400-e29b-41d4-a716-446655440000",
                                    "createdAt" : "2025-11-13T10:30:00Z",
                                    "updatedAt" : "2025-11-13T10:30:00Z",
                                    "name":"ManChesterCity",
                                    "description":"ManChester city travel",
                                    "ChannelType" : "PRIVATE",
                                    "isTextChannel": true,
                                    "participantIds":[
                                    "550e8400-e29b-41d4-a716-446655440000","a9f3e1f1-22c3-4e50-9b12-38d5a0c0f412"
                                    ]
                                    }
                                   """
                    )

            )
    )
    ResponseEntity<ChannelReadResponseDto> createPrivateChannel(@RequestBody ChannelPrivateCreateRequestDto dto);
    @Operation(
            summary = " Public 채널 생성",
            description = """
                    Public 채널을 생성합니다. 채널에 포함된 유저가 필요하지 않습니다.
                    T1의 쓰리핏을 축하합니다.
                    
                    """


    )
    @ApiResponse(responseCode = "201", description = "public 채널 생성 성공",
    content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = ChannelReadResponseDto.class),
            examples = @ExampleObject(
                    """
                            {
                                   "id" : "550e8400-e29b-41d4-a716-446655440000",
                                    "createdAt" : "2025-11-13T10:30:00Z",
                                    "updatedAt" : "2025-11-13T10:30:00Z",
                                    "name":"ManChesterCity",
                                    "description":"ManChester city travel",
                                    "ChannelType" : "PRIVATE",
                                    "isTextChannel": true,
                                    "participantIds":[
                                    "550e8400-e29b-41d4-a716-446655440000","a9f3e1f1-22c3-4e50-9b12-38d5a0c0f412"
                                    ]
                                    }
                            """
            )
    )

    )
    @ApiResponse(responseCode = "400", description = "유효하지 않은 요청을 넣었을 때 반환합니다.",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)
                ,examples = @ExampleObject(value = """
                유효하지 않은 요청을 보내면 됩니다.
                """)

        )
    )

    ResponseEntity<ChannelReadResponseDto> createPublicChannel(@RequestBody ChannelPublicCreateRequestDto dto);

    @Operation(summary = "채널 삭제(Delete Channel)", description = "채널을 삭제합니다.")
    @ApiResponse(responseCode = "204", description = "채널 삭제 성공",
    content = @Content(mediaType = "No",
    examples = @ExampleObject(value = """
            채널 삭제 성공시 아무것도 반환하지 않습니다.
            """)
    )
    )
    void deleteChannel(@PathVariable UUID channelId);

    @Operation(summary = "유저 채널 조회(Read Channel)", description = "유저가 접근 권한이 있는 채널을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "채널 조회 성공",
    content = @Content( schema = @Schema(implementation = ChannelReadResponseDto.class),
    examples = @ExampleObject(value = """
               {
                                    "id" : "550e8400-e29b-41d4-a716-446655440000",
                                    "createdAt" : "2025-11-13T10:30:00Z",
                                    "updatedAt" : "2025-11-13T10:30:00Z",
                                    "name":"ManChesterCity",
                                    "description":"ManChester city travel",
                                    "ChannelType" : "PRIVATE",
                                    "isTextChannel": true,
                                    "participantIds":[
                                    "550e8400-e29b-41d4-a716-446655440000","a9f3e1f1-22c3-4e50-9b12-38d5a0c0f412"
                                    ]
                                    }
            """)
    )
    )
    ResponseEntity<List<ChannelReadResponseDto>> readChannelById(@RequestParam UUID userId);

    @Operation(summary = "채널 수정(Patch Channel)", description = "채널을 수정합니다.")
    @ApiResponse(responseCode = "200", description = "채널 수정 성공",
    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ChannelPatchRequestDto.class),
    examples = @ExampleObject(value = """
            {
                                    "id" : "550e8400-e29b-41d4-a716-446655440000",
                                    "createdAt" : "2025-11-13T10:30:00Z",
                                    "updatedAt" : "2025-11-13T10:30:00Z",
                                    "name":"ManChesterCity",
                                    "description":"ManChester city travel",
                                    "ChannelType" : "PRIVATE",
                                    "isTextChannel": true,
                                    "participantIds":[
                                    "550e8400-e29b-41d4-a716-446655440000","a9f3e1f1-22c3-4e50-9b12-38d5a0c0f412"
                                    ]
                                    }
            """)
    )
    )
    ResponseEntity<ChannelReadResponseDto> patchChannel(@PathVariable UUID channelId, @RequestBody ChannelPatchRequestDto dto);


    void reset();


    ResponseEntity<List<ChannelReadResponseDto>> readAll();
}
