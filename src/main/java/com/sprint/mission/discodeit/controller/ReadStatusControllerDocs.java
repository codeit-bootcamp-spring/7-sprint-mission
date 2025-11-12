package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.readStatus.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.readStatus.ReadStatusPatchRequestDto;
import com.sprint.mission.discodeit.dto.response.ReadStatusResponseDto;
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
@Tag(name = "읽음 상태 관리(Read Status Controller)",description = "읽음 상태를 관리하는 API 입니다.")
public interface ReadStatusControllerDocs {
    @Operation(summary = "읽음 상태 생성(Create Read Status)", description = "읽음 상태를 생성합니다.")
    @ApiResponse(responseCode = "200", description = "읽음 상태 생성 성공",
    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReadStatusCreateRequestDto.class),
    examples = @ExampleObject(value = """
            읽음 상태 생성 하는 컨트롤러는 정말 평소에는 볼일이 없습니다. 이 서비스가 호출되었으면 유저서비스와 채널 서비스가 일을 잘 못한겁니다.
            """)
    )
    )
    @RequestMapping(value = "", method = RequestMethod.POST)
    ResponseEntity<ReadStatusResponseDto> createReadStatus(@RequestBody ReadStatusCreateRequestDto dto);

    @Operation(summary = "읽음 상태 조회(Read Read Status)", description = "읽음 상태를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "읽음 상태 조회 성공",
    content = @Content(mediaType = "application/json",
    examples = @ExampleObject(value = """
            읽음 상태를 조회합니다. channel-user 페어만큼 존재합니다. 삭제가 잘 되었는지 확인해 보십시오
            """)
    )
    )
    @RequestMapping(value = "", method = RequestMethod.GET)
    ResponseEntity<List<ReadStatusResponseDto>> readReadStatus(@RequestParam UUID userId);

    @Operation(summary = "읽음 상태 수정(Patch Read Status)", description = "읽음 상태를 수정합니다.")
    @ApiResponse(responseCode = "200", description = "읽음 상태 수정 성공",
    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ReadStatusPatchRequestDto.class),
    examples = @ExampleObject(value = """
            
            읽음 상태를 수정하는 코드로 제가 여기서 필드 하나를 못 찾아서 4시간을 허비하게 만들었습니다. 여러분은 그런 실수 하지 마세요
            """)
    )
    )
    @RequestMapping(value = "/{readStatusId}",method = RequestMethod.PATCH)
    ResponseEntity<ReadStatusResponseDto> patchReadStatus(@PathVariable UUID readStatusId, @RequestBody ReadStatusPatchRequestDto dto);
}
