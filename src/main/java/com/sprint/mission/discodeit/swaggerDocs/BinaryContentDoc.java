package com.sprint.mission.discodeit.swaggerDocs;

import com.sprint.mission.discodeit.entity.dto.Res_BinaryContent;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name="BinaryContent", description="첨부 파일 API")
public interface BinaryContentDoc {

    /**
     * GET /api/binaryContents - 여러 첨부 파일 조회
     */
    @Operation(summary = "여러 첨부 파일 조회", description = "ID 목록을 통해 여러 개의 첨부 파일을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "첨부 파일 목록 조회 성공",
            content = @Content(
                schema = @Schema(implementation = Res_BinaryContent.class, type = "array")
            )
        )
    })
    // NOTE: Spring MVC에서 쿼리 파라미터로 UUID[]를 받을 때 @RequestParam을 명시하지 않는 경우도 있으나,
    // Swagger 문서화를 위해 @RequestParam을 생략하고 파라미터에 배열 타입을 그대로 사용합니다.
    ResponseEntity<ArrayList<Res_BinaryContent>> find(
        @Parameter(description = "조회할 첨부 파일 ID 목록") UUID[] binaryContentIds);


    /**
     * GET /api/binaryContents/{binaryContentId} - 첨부 파일 조회
     */
    @Operation(summary = "단일 첨부 파일 조회")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "첨부 파일 조회 성공",
            content = @Content(
                schema = @Schema(implementation = Res_BinaryContent.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "첨부 파일을 찾을 수 없음",
            content = @Content(
                examples = @ExampleObject(value = "BinaryContent with id {binaryContentId} not found")
            )
        )
    })
    ResponseEntity<Res_BinaryContent> find(
        @Parameter(description = "조회할 첨부 파일 ID") @PathVariable("binaryContentId") UUID binaryContentId);
}