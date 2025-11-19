package com.sprint.mission.discodeit.controller.openapi;

import com.sprint.mission.discodeit.dto.channel.request.CreatePrivateChannelRequestDto;
import com.sprint.mission.discodeit.dto.channel.request.CreatePublicChannelRequestDto;
import com.sprint.mission.discodeit.dto.channel.request.UpdatePublicChannelRequestDto;
import com.sprint.mission.discodeit.dto.channel.response.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

@Tag(name = "채널 관리(Channel Controller)", description = "채널 생성/수정/삭제/조회를 관리하는 API입니다.")
public interface ChannelControllerDocs {

    @Operation(
            summary = "공개 채널 생성",
            description = """
                    새로운 공개 채널을 생성합니다.
                    
                    ### 요청 데이터(JSON)
                    - name : 이름
                    - description : 설명
                    - 채널 이름은 중복될 수 없습니다.
                    
                    ### 응답 데이터
                    - 성공 시 저장된 채널 정보가 반환됩니다.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "공개 채널 생성 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Channel.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "channelType": "MESSAGE",
                                                "visibility": "PUBLIC",
                                                "channelName": "이미지 생성 채널2",
                                                "description": "이미지를 자유롭게 생성해서 멤버들에게 뽐내보세요.",
                                                "adminId": null,
                                                "id": "d3cee723-cb74-4a4f-a8be-e940aaf6eac3",
                                                "createdAt": "2025-11-12T02:26:30.995726500Z",
                                                "updatedAt": "2025-11-12T02:26:30.995726500Z",
                                                "memberIds": []
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "중복된 채널 이름으로 생성 요청",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = com.sprint.mission.discodeit.global.dto.ApiResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "status": "CONFLICT",
                                                "message": "채널 이름이 이미 존재합니다. 다시 입력해주세요.",
                                                "data": null
                                            }
                                            """
                            )
                    )
            )
    })
    ResponseEntity<Channel> createPublic(CreatePublicChannelRequestDto requestDto);

    @Operation(
            summary = "비공개 채널 생성",
            description = """
                    비공개 채널을 생성합니다.
                    
                    ### 요청 데이터(JSON)
                    - participantIds : 참여할 사용자 UUID 리스트
                    
                    ### 응답 데이터
                    - 성공 시 저장된 채널 정보가 반환됩니다.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "비공개 채널 생성 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Channel.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "channelType": "MESSAGE",
                                                "visibility": "PRIVATE",
                                                "channelName": null,
                                                "description": null,
                                                "adminId": null,
                                                "id": "efc13a84-54d3-44cb-88ea-9cd51d96fc58",
                                                "createdAt": "2025-11-12T02:32:51.227916Z",
                                                "updatedAt": "2025-11-12T02:32:51.233426500Z",
                                                "memberIds": [
                                                    "71d92a3c-04ad-431f-aae4-88c5dc2b875e",
                                                    "4f5f8a05-fdbc-41ad-b736-e6a2695e4e6d"
                                                ]
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "비공개 채널 생성 요청 시 지정한 사용자를 찾을 수 없음",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = com.sprint.mission.discodeit.global.dto.ApiResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "status": "NOT_FOUND",
                                                "message": "해당 사용자를 찾을 수 없습니다.",
                                                "data": null
                                            }
                                            """
                            )
                    )
            )
    })
    ResponseEntity<Channel> createPrivate(@RequestBody CreatePrivateChannelRequestDto requestDto);

    @Operation(
            summary = "채널 정보 수정",
            description = """
                    채널 정보를 수정합니다.
                    
                    ### 요청 경로(Path Variable)
                    - channelId : 채널 고유 식별자(UUID)
                    
                    ### 요청 본문(JSON)
                    - newName : 채널 이름
                    - newDescription : 설명
                    - 변경하지 않을 필드는 생략 가능
                    
                    ### 응답 데이터
                    - 성공 시 수정된 채널 정보가 반환됩니다.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "채널 정보 수정 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Channel.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "channelType": "MESSAGE",
                                                "visibility": "PUBLIC",
                                                "channelName": "이미지 공유 채널3",
                                                "description": "좋아하는 이미지를 공유해보세요.",
                                                "adminId": null,
                                                "id": "0e8f0e42-6ef8-4edb-a28a-210a9cd270de",
                                                "createdAt": "2025-11-12T04:33:54.087721200Z",
                                                "updatedAt": "2025-11-12T04:38:23.819765300Z",
                                                "memberIds": []
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "비공개 채널 수정 요청",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = com.sprint.mission.discodeit.global.dto.ApiResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "status": "FORBIDDEN",
                                                "message": "비공개 채널은 수정이 불가합니다.",
                                                "data": null
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "중복된 채널 이름으로 수정 요청",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = com.sprint.mission.discodeit.global.dto.ApiResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "status": "CONFLICT",
                                                "message": "채널 이름이 이미 존재합니다. 다시 입력해주세요.",
                                                "data": null
                                            }
                                            """
                            )
                    )
            )
    })
    ResponseEntity<Channel> update(UUID channelId, UpdatePublicChannelRequestDto request);


    @Operation(
            summary = "채널 정보 삭제",
            description = """
                    기존 채널 정보를 삭제합니다.
                    
                    ## 요청 경로(Path Variable)
                    - channelId : 채널 고유 식별자(UUID)
                    
                    ## 응답 데이터
                    - 응답 본문은 없습니다.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "채널 정보 삭제 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Void.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "채널 삭제 요청시 지정한 채널을 찾을 수 없음",
                    content = @Content(
                            mediaType = "application/json",
                            schema =  @Schema(implementation = com.sprint.mission.discodeit.global.dto.ApiResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "status": "NOT_FOUND",
                                                "message": "해당 채널을 찾을 수 없습니다.",
                                                "data": null
                                            }
                                            """
                            )
                    )
            )
    })
    ResponseEntity<Void> deleteChannel(UUID channelId);

    @Operation(
            summary = "사용자가 접속 가능한 채널 목록 조회",
            description = """
                    특정 사용자가 접속 가능한 채널 목록을 조회합니다.
                    
                    ## 요청 데이터(`@RequestParam`)
                    - userId : 사용자 고유 식별자(UUID)
                    
                    ## 응답 데이터
                    - 성공 시 채널 정보를 리스트로 반환합니다.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "채널 목록 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = ChannelDto.class)
                            ),
                            examples = @ExampleObject(
                                    value = """
                                            [
                                                {
                                                    "id": "efc13a84-54d3-44cb-88ea-9cd51d96fc58",
                                                    "type": "PRIVATE",
                                                    "name": null,
                                                    "description": null,
                                                    "participantIds": [
                                                        "71d92a3c-04ad-431f-aae4-88c5dc2b875e",
                                                        "4f5f8a05-fdbc-41ad-b736-e6a2695e4e6d"
                                                    ],
                                                    "lastMessageAt": null
                                                },
                                                {
                                                    "id": "1bb1b6a9-6c1b-4534-b595-c14bdfc0ca86",
                                                    "type": "PUBLIC",
                                                    "name": "이미지 공유 채널",
                                                    "description": "좋아하는 이미지를 공유해보세요.",
                                                    "participantIds": [],
                                                    "lastMessageAt": "2025-11-11T09:59:17.247309Z"
                                                }
                                            ]
                                            """
                            )
                    )
            )
    })
    ResponseEntity<List<ChannelDto>> searchChannels(UUID userId);
}
