package com.sprint.mission.discodeit.controller.openapi;

import com.sprint.mission.discodeit.dto.binarycontent.Response.BinaryContentResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
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

@Tag(name = "파일 관리(BinaryContentController)", description = "파일 조회를 관리하는 API입니다.")
public interface BinaryContentControllerDocs {

    @Operation(
            summary = "파일 단일 조회",
            description = """
                    특정 파일을 단일로 조회합니다.
                    
                    ## 요청 데이터(Path Variable)
                    - binaryContentId : 파일 고유 식별자(UUID)
                    
                    ## 응답 데이터
                    - 성공 시 조회된 파일 정보를 반환합니다.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "파일 단일 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BinaryContent.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "name": "KakaoTalk_20251105_112449628.webp",
                                                "contentType": "multipart/form-data",
                                                "bytes": "<BASE64_ENCODED_DATA>",
                                                "id": "6ddc517b-e6ff-462d-94e8-85730be2fadc",
                                                "createdAt": "2025-11-11T09:36:33.711636300Z"
                                            }
                                            """
                            )
                    )
            )
    })
    ResponseEntity<BinaryContentResponseDto> find(UUID binaryContentId);

    @Operation(
            summary = "파일 다중 조회",
            description = """
                    요청된 파일들을 조회합니다.
                    
                    ## 요청 데이터(Request Param)
                    - binaryContentIds : 파일 고유 식별자 리스트(List<UUID>)
                    
                    ## 응답 데이터
                    - 성공 시 조회된 파일 리스트를 반환합니다.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "파일 다중 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = BinaryContent.class)
                            ),
                            examples = @ExampleObject(
                                    value = """
                                            [
                                                {
                                                    "name": "KakaoTalk_20251105_112449628.webp",
                                                    "contentType": "multipart/form-data",
                                                    "bytes": "<BASE64_ENCODED_DATA>",
                                                    "id": "d942a7cd-e57c-4cb8-b367-279bd7e8815e",
                                                    "createdAt": "2025-11-11T09:59:17.245808900Z"
                                                },
                                                {
                                                    "name": "KakaoTalk_20251105_112449628_01.png",
                                                    "contentType": "multipart/form-data",
                                                    "bytes": "<BASE64_ENCODED_DATA>",
                                                    "id": "6e1a075f-9946-47b0-b6f7-d52e29e20511",
                                                    "createdAt": "2025-11-11T09:59:17.247309Z"
                                                }
                                            ]
                                            """
                            )
                    )
            )
    })
    ResponseEntity<List<BinaryContentResponseDto>> findAllByIdIn(List<UUID> binaryContentIds);
}
