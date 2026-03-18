package com.sprint.mission.discodeit.api;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Tag(name = "Binary Content API", description = "바이너리 파일 관련 API")
public interface BinaryContentApi {

    @Operation(summary = "바이너리컨텐츠 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))
            )
    })
    ResponseEntity<BinaryContentResponseDto> getBinaryContent(UUID id);

    @Operation(summary = "특정 다수 바이너리컨텐츠 조회")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))
            )
    })
    ResponseEntity<List<BinaryContentResponseDto>> getAllBinaryContentsByIds(List<UUID> ids);

    @Operation(summary = "바이너리컨텐츠 다운로드")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "파일 다운로드 성공"),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))
            )
    })
    ResponseEntity<?> downloadBinaryContent(UUID id);

    @Operation(summary = "바이너리컨텐츠 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "성공"),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))
            )
    })
    ResponseEntity<UUID> createBinaryContent(MultipartFile file, DiscodeitUserDetails userDetails);

}
