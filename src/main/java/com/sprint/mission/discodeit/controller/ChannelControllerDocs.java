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
                    schema = @Schema(implementation = ChannelReadResponseDto.class),
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


    ResponseEntity<ChannelReadResponseDto> createPublicChannel(@RequestBody ChannelPublicCreateRequestDto dto);


    void deleteChannel(@PathVariable UUID channelId);


    ResponseEntity<List<ChannelReadResponseDto>> readChannelById(@RequestParam UUID userId);


    ResponseEntity<ChannelReadResponseDto> patchChannel(@PathVariable UUID channelId, @RequestBody ChannelPatchRequestDto dto);


    void reset();


    ResponseEntity<List<ChannelReadResponseDto>> readAll();
}
