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
            summary = "채널 생성(Create Channel)",
            description = """
                    채널을 생성합니다
                    ## 요청 데이터
                    - 채널의 어쩌구 저쩌구가 필요합니다
                    - 작성해야 하는데 귀찮아서 안하겠습니다.
                    
                    ## 응답
                    - 성공시 생성한 채널의 정보를 반환합니다
                    - 이거 한번만 쓰고 테스트 한 다음 다시 적지 않을 겁니다. 꼬우면 니가 쓰세요.
                    """
    )
    @ApiResponse(responseCode = "201", description = "채널 생성 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ChannelPrivateCreateRequestDto.class),
                    examples = @ExampleObject(
                            value = """
                                    
                                    {
                                    여기에 channelCreateRequestDto 내용물을 적으면 됩니다.
                                    전 귀찮아서 안 적을 거지만, 다른 분들은 하시면 도움이 될 겁니다.
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
            schema = @Schema(implementation = ChannelPublicCreateRequestDto.class),
            examples = @ExampleObject(
                    """
                            example은 다음 기회에
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
    content = @Content( schema = @Schema(implementation = UUID.class),
    examples = @ExampleObject(value = """
            그냥 현재 존재하는 아무 유저 UUID 나 넣어도 됩니다. 그걸 넣으면
            모든 public channel 및 유저가 속한 private channel을 조회할 수 있습니다.
            """)
    )
    )
    ResponseEntity<List<ChannelReadResponseDto>> readChannelById(@RequestParam UUID userId);

    @Operation(summary = "채널 수정(Patch Channel)", description = "채널을 수정합니다.")
    @ApiResponse(responseCode = "200", description = "채널 수정 성공",
    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ChannelPatchRequestDto.class),
    examples = @ExampleObject(value = """
            이런 example 문서가 없어도 한번에 보면 누구나 코드 구조를 알아볼 수 있는 코드를 짜는 사람이 됩시다.
            """)
    )
    )
    ResponseEntity<ChannelReadResponseDto> patchChannel(@PathVariable UUID channelId, @RequestBody ChannelPatchRequestDto dto);


    void reset();


    ResponseEntity<List<ChannelReadResponseDto>> readAll();
}
