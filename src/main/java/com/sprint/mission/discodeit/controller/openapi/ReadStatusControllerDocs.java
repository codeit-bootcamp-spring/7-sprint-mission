package com.sprint.mission.discodeit.controller.openapi;

import com.sprint.mission.discodeit.dto.readstatus.request.CreateReadStatusRequestDto;
import com.sprint.mission.discodeit.dto.readstatus.request.UpdateReadStatusRequestDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
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

@Tag(name = "메시지 수신 정보 관리(ReadStatusController)", description = "메시지 수신 정보 생성/수정/조회를 관리하는 API입니다.")
public interface ReadStatusControllerDocs {

    @Operation(
            summary = "채널 메시지 수신 정보 생성",
            description = """
                    특정 채널의 메시지 수신 정보를 생성합니다.
                    
                    ## 요청 데이터(JSON)
                    - userId : 사용자 고유 식별자(UUID)
                    - channelId : 채널 고유 식별자(UUID)
                    - lastReadAt : 마지막으로 메시지 읽은 시각
                    
                    ## 응답 데이터
                    - 성공 시 생성된 메시지 수신 정보를 반환합니다.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "메시지 수신 정보 생성 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReadStatus.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "userId": "4f5f8a05-fdbc-41ad-b736-e6a2695e4e6d",
                                                "channelId": "098af64f-6c36-48a2-b862-bfdd776261f4",
                                                "lastReadAt": "2025-11-11T09:21:02.812519900Z",
                                                "id": "30f9c801-b393-42de-aea0-eb691ed3002c",
                                                "createdAt": "2025-11-11T09:21:02.812519900Z",
                                                "updatedAt": "2025-11-11T09:21:02.812519900Z"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "공개 채널에서 불필요한 생성 요청",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = com.sprint.mission.discodeit.global.dto.ApiResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "status": "FORBIDDEN",
                                                "message": "공개 채널에는 read status를 생성할 수 없습니다.",
                                                "data": null
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "요청한 채널에 대한 사용자의 메시지 수신 정보가 이미 존재합니다.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = com.sprint.mission.discodeit.global.dto.ApiResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "status": "CONFLICT",
                                                "message": "이미 멤버가 채널에 속해 있습니다.",
                                                "data": null
                                            }
                                            """
                            )
                    )
            )
    })
    ResponseEntity<ReadStatus> createReadStatus(CreateReadStatusRequestDto requestDto);

    @Operation(
            summary = "채널의 메시지 수신 정보 수정",
            description = """
                    특정 채널의 사용자 메시지 수신 정보를 수정합니다.
                    
                    ## 요청 경로(Path Variable)
                    - readStatusId : 메시지 수신 정보 고유 식별자(UUID)
                    
                    ## 요청 데이터(JSON)
                    - newLastReadAt : 마지막으로 메시지 읽은 시각
                    
                    ## 응답 데이터
                    - 성공 시 수정된 메시지 수신 정보를 반환합니다.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "메시지 수신 정보 수정 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ReadStatus.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "userId": "4f5f8a05-fdbc-41ad-b736-e6a2695e4e6d",
                                                "channelId": "098af64f-6c36-48a2-b862-bfdd776261f4",
                                                "lastReadAt": "2025-11-12T02:17:35Z",
                                                "id": "30f9c801-b393-42de-aea0-eb691ed3002c",
                                                "createdAt": "2025-11-11T09:21:02.812519900Z",
                                                "updatedAt": "2025-11-12T06:42:09.655032100Z"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "수정 요청시 지정된 메시지 수신 정보가 존재하지 않음",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = com.sprint.mission.discodeit.global.dto.ApiResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "status": "NOT_FOUND",
                                                "message": "read status가 존재하지 않습니다.",
                                                "data": null
                                            }
                                            """
                            )
                    )
            )
    })
    ResponseEntity<ReadStatus> createReadStatus(UUID readStatusId, UpdateReadStatusRequestDto requestDto);

    @Operation(
            summary = "사용자의 메시지 수신 정보 조회",
            description = """
                    특정 사용자의 모든 메시지 수신 정보를 조회합니다.
                    
                    ## 요청 데이터(Request Param)
                    - userId : 사용자의 고유 식별자(UUID)
                    
                    ## 응답 데이터
                    - 성공시 조회된 메시지 수신 정보 리스트를 반환합니다.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "사용자의 모든 메시지 수신 정보 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = ReadStatus.class)
                            ),
                            examples = @ExampleObject(
                                    value = """
                                            [
                                                {
                                                    "userId": "4f5f8a05-fdbc-41ad-b736-e6a2695e4e6d",
                                                    "channelId": "efc13a84-54d3-44cb-88ea-9cd51d96fc58",
                                                    "lastReadAt": "2025-11-12T02:32:51.232425300Z",
                                                    "id": "468fb94d-4541-40a6-b309-c9157f664bb5",
                                                    "createdAt": "2025-11-12T02:32:51.232425300Z",
                                                    "updatedAt": "2025-11-12T02:32:51.232425300Z"
                                                },
                                                {
                                                    "userId": "4f5f8a05-fdbc-41ad-b736-e6a2695e4e6d",
                                                    "channelId": "1bb1b6a9-6c1b-4534-b595-c14bdfc0ca86",
                                                    "lastReadAt": "2025-11-11T02:17:35Z",
                                                    "id": "32f96175-218c-4851-9a44-64714b898a90",
                                                    "createdAt": "2025-11-11T09:46:06.337439400Z",
                                                    "updatedAt": "2025-11-11T09:46:06.337439400Z"
                                                }
                                            ]
                                            """
                            )
                    )
            )
    })
    ResponseEntity<List<ReadStatus>> search(UUID userId);
}
