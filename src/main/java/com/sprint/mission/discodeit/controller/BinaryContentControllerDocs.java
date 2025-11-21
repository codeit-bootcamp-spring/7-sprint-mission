package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.response.binaryContent.BinaryContentDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;
@Tag(name = "파일 관리(File Controller)",description = " 파일을 생성 및 조회하는 곳입니다. 놀랍게도 삭제와 변경은 하지 않습니다. 바지 사장 계층입니다.")
public interface BinaryContentControllerDocs {
    @Operation(summary = "파일 조회(Create File)", description = "파일을 id로 조회합니다.")
    @ApiResponse(responseCode = "200", description = "파일 조회 성공",
    content = @Content(mediaType = "application/json",
    schema = @Schema(implementation = BinaryContentDto.class),
    examples = @ExampleObject(value = """
            {
            "id":"550e8400-e29b-41d4-a716-446655440000",
            "createdAt":"2025-11-13T10:30:00Z",
            "fileName" : "Siuuuuuu",
            "size":256,
            "contentTyp":"jpg",
            "bytes": "111111111111"
            }
            """)
    )
    )
    @RequestMapping(value = "/{binaryContentId}", method = RequestMethod.GET)
    ResponseEntity<BinaryContentDto> read(@PathVariable UUID binaryContentId);
    @Operation(summary = "파일 다건 조회(Read File)", description = "파일을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "파일 다건 조회 성공",
    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BinaryContentDto.class),
    examples = @ExampleObject(value = """
             [
             {
            "id":"550e8400-e29b-41d4-a716-446655440000",
            "createdAt":"2025-11-13T10:30:00Z",
            "fileName" : "Siuuuuuu",
            "size":256,
            "contentTyp":"jpg",
            "bytes": "111111111111111111111"
            }
            ]
            """)
    )
    )
    @RequestMapping(value = "",method = RequestMethod.GET)
    ResponseEntity<List<BinaryContentDto>> readByIdList(@RequestParam List<UUID> binaryContentIds);
}
