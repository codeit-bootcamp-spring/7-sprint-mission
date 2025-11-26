package com.sprint.mission.discodeit.controller.Docs;

import com.sprint.mission.discodeit.dto.Binarycontent.response.BinaryContentDto;
import com.sprint.mission.discodeit.entity.content.BinaryContent;
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

@Tag(name = "이진 콘텐츠 관리 (BinaryContentController)", description = "파일(바이너리 데이터) 조회 및 다중 조회")
public interface BinaryContentServiceDocs {

    @Operation(
            summary = "단일 이진 콘텐츠 조회",
            description = """
                    특정 binaryContentId를 사용하여 저장된 이진 콘텐츠(파일)를 조회합니다.
                    
                    **요청 데이터**
                    - binaryContentId (UUID)
                    
                    **응답**
                    - BinaryContent 객체 (파일 바이트 배열 및 메타정보)
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BinaryContent.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "id": "f7a1e6e4-bf2c-4c5f-a0de-9abf132a7a30",
                                              "createdAt": "2025-11-12T11:00:00.000Z",
                                              "updatedAt": null,
                                              "contentsType": "PROFILE_IMAGE",
                                              "contentByte": "Qk2...=="  // Base64 인코딩된 데이터
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 (UUID 형식 오류/데이터 없음)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "유효하지 않은 binaryContentId입니다.")
                    )
            )
    })
    ResponseEntity<BinaryContentDto> find(UUID binaryContentId);


    @Operation(
            summary = "여러 이진 콘텐츠 일괄 조회",
            description = """
                    여러 binaryContentId를 한 번에 조회합니다.
                    
                    **요청 데이터**
                    - binaryContentIds (UUID 배열)
                    
                    **응답**
                    - BinaryContent 객체 리스트 반환
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = BinaryContent.class)),
                            examples = @ExampleObject(
                                    value = """
                                            [
                                              {
                                                "id": "f7a1e6e4-bf2c-4c5f-a0de-9abf132a7a30",
                                                "createdAt": "2025-11-12T11:00:00.000Z",
                                                "updatedAt": null,
                                                "contentsType": "PROFILE_IMAGE",
                                                "contentByte": "Qk2..."
                                              },
                                              {
                                                "id": "c5d9a7d2-5a21-4bc2-bb73-ef2a65b2b5ff",
                                                "createdAt": "2025-11-12T11:02:00.000Z",
                                                "updatedAt": null,
                                                "contentsType": "MESSAGE_ATTACHMENT",
                                                "contentByte": "aGVsbG8gd29ybGQ="
                                              }
                                            ]
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 (UUID 배열 오류/데이터 일부 누락)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            [
                                              "유효하지 않은 UUID 형식이 포함되어 있습니다.",
                                              "일부 binaryContentId를 찾을 수 없습니다."
                                            ]
                                            """
                            )
                    )
            )
    })
    ResponseEntity<List<BinaryContentDto>> findAllByIdIn(List<UUID> binaryContentIds);
}
