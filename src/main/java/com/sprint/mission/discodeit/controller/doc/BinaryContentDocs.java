package com.sprint.mission.discodeit.controller.doc;

import com.sprint.mission.discodeit.dto.binaryContent.response.BinaryContentResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.UUID;

@Tag(name = "BinaryContent", description = "첨부 파일 API")
public interface BinaryContentDocs {

    @Operation(summary = "첨부 파일 조회")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "첨부 파일 조회 성공",
                    content = @Content(
                            mediaType = "*/*",
                            schema = @Schema(implementation = BinaryContentResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "첨부 파일을 찾을 수 없음",
                    content = @Content(
                            mediaType = "*/*",
                            examples = @ExampleObject(value = "BinaryContent with id {binaryContentId} not found"))
            )
    })
    ResponseEntity<BinaryContentResponseDto> getBinaryContent(@Parameter(description = "조회할 첨부 파일 ID")
                                                              @PathVariable UUID binaryContentId);

    @Operation(summary = "여러 첨부 파일 조회")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "첨부 파일 목록 조회 성공",
                    content = @Content(
                            mediaType = "*/*",
                            array = @ArraySchema(schema = @Schema(implementation = BinaryContentResponseDto.class))
                    )
            ),
    })
    ResponseEntity<List<BinaryContentResponseDto>> getAllBinaryContent(@Parameter(description = "조회할 첨부 파일 ID 목록")
                                                                       @PathVariable List<UUID> binaryContentIds);
}
