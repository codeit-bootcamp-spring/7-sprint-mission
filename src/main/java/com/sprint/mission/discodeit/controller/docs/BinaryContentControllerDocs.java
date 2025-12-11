package com.sprint.mission.discodeit.controller.docs;

import com.sprint.mission.discodeit.dto.response.BinaryContentResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "BinaryContent", description = "첨부 파일 API")
public interface BinaryContentControllerDocs {

  @Operation(summary = "첨부 파일 조회")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "첨부 파일 조회 성공",
          content = @Content(
              schema = @Schema(implementation = BinaryContentResponseDto.class)
          )
      ),
      @ApiResponse(
          responseCode = "404",
          description = "첨부 파일을 찾을 수 없음",
          content = @Content(
              examples = @ExampleObject(
                  value = "BinaryContent with id {binaryContentId} not found"
              )
          )
      )
  })
  ResponseEntity<BinaryContentResponseDto> findByBinaryContent(
      @Parameter(description = "조회할 첨부 파일 ID")
      @PathVariable UUID binaryContentId);

  @Operation(summary = "여러 첨부 파일 조회")
  @ApiResponse(
      responseCode = "200",
      description = "첨부 파일 목록 조회 성공",
      content = @Content(
          schema = @Schema(implementation = BinaryContentResponseDto.class)
      )
  )
  ResponseEntity<List<BinaryContentResponseDto>> findAllByBinaryContent(
      @Parameter(description = "조회할 첨부 파일 ID 목록")
      @RequestParam List<UUID> binaryContentIds);


  @Operation(summary = "파일 다운로드")
  @ApiResponse(
      responseCode = "200",
      description = "파일 다운로드 성공",
      content = @Content(schema = @Schema(type = "string", format = "binary"))
  )
  ResponseEntity<Resource> download(
      @Parameter(description = "다운로드할 파일 ID")
      @PathVariable UUID binaryContentId);

}
