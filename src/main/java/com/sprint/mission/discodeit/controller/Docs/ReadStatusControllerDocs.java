package com.sprint.mission.discodeit.controller.Docs;


import com.sprint.mission.discodeit.dto.readstatus.requset.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readstatus.requset.ReadStatusUpdateReuqest;
import com.sprint.mission.discodeit.dto.readstatus.response.ReadStatusDto;
import com.sprint.mission.discodeit.entity.status.ReadStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;

@Tag(name = "읽음 상태 관리 (ReadStatusController)",
        description = "채널별 유저의 마지막 읽음 시각 생성/수정/조회")
public interface ReadStatusControllerDocs {

    // 1) 생성
    @Operation(
            summary = "읽음 상태 생성",
            description = """
                    특정 유저와 채널에 대한 읽음 상태를 새로 생성합니다.
                    
                    **요청 데이터**
                    - userId (UUID)
                    - channelId (UUID)
                    - lastReadAt (ISO-8601 Instant)
                    
                    **응답**
                    - 생성된 ReadStatus 정보가 반환됩니다.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "읽음 상태 생성 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReadStatus.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "id": "6d1fa0a6-3a7c-4c4e-8f2c-2d7f7a0e9d11",
                                              "createdAt": "2025-11-12T09:30:00.000000000Z",
                                              "updatedAt": null,
                                              "userId": "6f01be3c-e8f9-4597-a5b1-02e348db7d36",
                                              "channelId": "b6b9a8b2-3d0f-4a1b-8a2f-7c0b2f2c9a11",
                                              "lastReadAt": "2025-11-12T09:28:43.140Z"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 (필드 누락/형식 오류/중복 등)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReadStatus.class),
                            examples = @ExampleObject(
                                    value = """
                                            [
                                              "존재하지 않는 userId 입니다.",
                                              "존재하지 않는 channelId 입니다.",
                                              "lastReadAt 형식이 올바르지 않습니다."
                                            ]
                                            """
                            )
                    )
            )
    })
    ResponseEntity<ReadStatusDto> createStatus(ReadStatusCreateRequest request);


    // 2) 수정
    @Operation(
            summary = "읽음 상태 업데이트",
            description = """
                    기존 ReadStatus의 lastReadAt 등을 수정합니다.
                    
                    **요청 데이터**
                    - readStatusId (Query Param, UUID)
                    - lastReadAt 등 변경 필드 (Request Body)
                    
                    **응답**
                    - 수정된 ReadStatus 정보가 반환됩니다.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "읽음 상태 업데이트 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReadStatus.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "id": "6d1fa0a6-3a7c-4c4e-8f2c-2d7f7a0e9d11",
                                              "createdAt": "2025-11-12T09:30:00.000000000Z",
                                              "updatedAt": "2025-11-12T10:02:11.123456700Z",
                                              "userId": "6f01be3c-e8f9-4597-a5b1-02e348db7d36",
                                              "channelId": "b6b9a8b2-3d0f-4a1b-8a2f-7c0b2f2c9a11",
                                              "lastReadAt": "2025-11-12T10:02:10.000Z"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 (UUID 형식 오류/존재하지 않는 ID 등)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReadStatus.class),
                            examples = @ExampleObject(
                                    value = """
                                            [
                                              "맞는 readStatusId 가 없습니다: 00000000-0000-0000-0000-000000000000",
                                              "lastReadAt 형식이 올바르지 않습니다."
                                            ]
                                            """
                            )
                    )
            )
    })
    ResponseEntity<ReadStatusDto> updateStatus(
            UUID readStatusId,
            ReadStatusUpdateReuqest request
    );


    // 3) 조회 (userId 기준)
    @Operation(
            summary = "특정 유저의 모든 읽음 상태 조회",
            description = """
                    userId 기준으로 해당 유저의 모든 ReadStatus 목록을 조회합니다.
                    
                    **요청 데이터**
                    - userId (Query Param, UUID)
                    
                    **응답**
                    - ReadStatus 배열이 반환됩니다.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ReadStatus.class)),
                            examples = @ExampleObject(
                                    value = """
                                            [
                                              {
                                                "id": "a1111111-2222-3333-4444-555555555555",
                                                "createdAt": "2025-11-12T08:47:51.451453600Z",
                                                "updatedAt": null,
                                                "userId": "6f01be3c-e8f9-4597-a5b1-02e348db7d36",
                                                "channelId": "c1111111-2222-3333-4444-555555555555",
                                                "lastReadAt": "2025-11-12T08:47:50.000Z"
                                              },
                                              {
                                                "id": "b2222222-3333-4444-5555-666666666666",
                                                "createdAt": "2025-11-12T08:49:10.000000000Z",
                                                "updatedAt": "2025-11-12T09:00:00.000000000Z",
                                                "userId": "6f01be3c-e8f9-4597-a5b1-02e348db7d36",
                                                "channelId": "c2222222-3333-4444-5555-666666666666",
                                                "lastReadAt": "2025-11-12T09:00:00.000Z"
                                              }
                                            ]
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 (UUID 형식 오류 등)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReadStatus.class),
                            examples = @ExampleObject(
                                    value = """
                                            [
                                              "userId 형식이 올바르지 않습니다."
                                            ]
                                            """
                            )
                    )
            )
    })
    ResponseEntity<List<ReadStatusDto>> findStatusByUserId(
            @RequestParam("userId") UUID userId
    );
}
