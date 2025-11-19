package com.sprint.mission.discodeit.controller.docs;

import com.sprint.mission.discodeit.dto.channel.request.ChannelCreateReq;
import com.sprint.mission.discodeit.dto.channel.request.ChannelCreateSecReq;
import com.sprint.mission.discodeit.dto.channel.request.ChannelUpdateReq;
import com.sprint.mission.discodeit.dto.channel.response.ChannelInfoRes;
import com.sprint.mission.discodeit.dto.channel.response.ChannelPrivateInfoRes;
import com.sprint.mission.discodeit.dto.channel.response.ChannelPublicInfoRes;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.exception.ErrorInfoRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Tag(name = "Channel", description = "Channel API")
public interface ChannelControllerDocs {

  @Operation(summary = "User가 참여 중인 Channel 목록 조회")
  @ApiResponses(
      value = {
          @io.swagger.v3.oas.annotations.responses.ApiResponse(
              responseCode = ResponseCode.OK,
              description = "Channel 목록 조회 성공",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(oneOf = {
                      ChannelPrivateInfoRes.class,
                      ChannelPublicInfoRes.class}),
                  examples = @ExampleObject(
                      name = "Channel 목록 조회 성공 예시",
                      value = """
                          {
                                "PRIVATE": [
                                    {
                                        "type": "private",
                                        "managerId": "46a9e5e8-26bf-4dd8-828b-4d859f69823f",
                                        "userIds": [],
                                        "lastMessageTime": null
                                    },
                                    {
                                        "type": "private",
                                        "managerId": "46a9e5e8-26bf-4dd8-828b-4d859f69823f",
                                        "userIds": [],
                                        "lastMessageTime": null
                                    }
                                ],
                                "PUBLIC": [
                                    {
                                        "type": "public",
                                        "name": "공개 채널",
                                        "description": "공개 채널의 설명입니다.",
                                        "lastMessageTime": null
                                    },
                                    {
                                        "type": "public",
                                        "name": "공개 채널22222",
                                        "description": "공개 채널의 설명입니다.2222",
                                        "lastMessageTime": null
                                    }
                                ]
                            }
                          """
                  )
              )
          )
      }
  )
  ResponseEntity<Map<ChannelType, List<ChannelInfoRes>>> getAllChannels(
      @RequestHeader("X-LOGINUSER-ID") UUID userId);

  @Operation(summary = "Public Channel 생성")
  @ApiResponses(
      value = {
          @io.swagger.v3.oas.annotations.responses.ApiResponse(
              responseCode = ResponseCode.CREATED,
              description = "Public Channel이 성공적으로 생성됨",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ChannelPublicInfoRes.class),
                  examples = @ExampleObject(
                      name = "Channel이 성공적으로 생성 예시",
                      value = """
                          {
                              "channelId": "db6da483-b06a-4380-8c54-d3efd4cd1314",
                              "type": "public",
                              "name": "공개 채널3333",
                              "description": "공개 채널의 설명입니다.333",
                              "lastMessageTime": null
                          }
                          """
                  )
              )
          ),
      }
  )
  ResponseEntity<ChannelPublicInfoRes> createPublicChannel(
      @RequestHeader("X-LOGINUSER-ID") UUID managerId,
      @RequestBody ChannelCreateReq req);

  @Operation(summary = "Private Channel 생성")
  @ApiResponses(
      value = {
          @io.swagger.v3.oas.annotations.responses.ApiResponse(
              responseCode = ResponseCode.CREATED,
              description = "Private Channel이 성공적으로 생성됨",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ChannelPrivateInfoRes.class),
                  examples = @ExampleObject(
                      name = "Channel이 성공적으로 생성 예시",
                      value = """
                          {
                              "channelId": "bc9ee5d5-4e5f-4750-a449-5ed2f70bd8ce",
                              "type": "private",
                              "managerId": "46a9e5e8-26bf-4dd8-828b-4d859f69823f",
                              "userIds": [],
                              "lastMessageTime": null
                          }
                          """
                  )
              )
          ),
      }
  )
  ResponseEntity<ChannelPrivateInfoRes> createPrivateChannel(
      @RequestHeader("X-LOGINUSER-ID") UUID managerId,
      @RequestBody ChannelCreateSecReq req);

  @Operation(summary = "Channel 정보 수정")
  @ApiResponses(
      value = {
          @io.swagger.v3.oas.annotations.responses.ApiResponse(
              responseCode = ResponseCode.OK,
              description = "Channel 정보가 성공적으로 수정됨",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ChannelPublicInfoRes.class),
                  examples = @ExampleObject(
                      name = "Channel 정보가 성공적으로 수정 예시",
                      value = """
                          {
                              "channelId": "db6da483-b06a-4380-8c54-d3efd4cd1314",
                              "type": "public",
                              "name": "공개 채널 수정됨",
                              "description": "수정 확인 완료",
                              "lastMessageTime": null
                          }
                          """
                  )
              )
          ),
          @io.swagger.v3.oas.annotations.responses.ApiResponse(
              responseCode = ResponseCode.NOT_FOUND,
              description = "해당 UUID 를 가진 채널이 없음",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorInfoRes.class),
                  examples = @ExampleObject(
                      name = "해당 UUID 를 가진 채널이 없음 예시",
                      value = """
                          {
                              "code": "CHANNEL_001",
                              "message": "해당 UUID를 가진 채널이 존재하지 않습니다.",
                              "httpStatus": 404
                          }
                          """
                  )
              )
          ),
          @io.swagger.v3.oas.annotations.responses.ApiResponse(
              responseCode = ResponseCode.BAD_REQUEST,
              description = "Private Channel은 수정할 수 없음",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorInfoRes.class),
                  examples = @ExampleObject(
                      name = "Private Channel은 수정할 수 없음 예시",
                      value = """
                          {
                               "code": "CHANNEL_002",
                               "message": "Private 채널은 수정할 수 없습니다.",
                               "httpStatus": 400
                           }
                          """
                  )
              )
          )
      }
  )
  ResponseEntity<ChannelPublicInfoRes> updatePublicChannel(@PathVariable UUID channelId,
      @RequestBody ChannelUpdateReq req);

  @Operation(summary = "Channel 삭제")
  @ApiResponses(
      value = {
          @io.swagger.v3.oas.annotations.responses.ApiResponse(
              responseCode = ResponseCode.NO_CONTENT,
              description = "Channel이 성공적으로 삭제됨"
          ),
          @io.swagger.v3.oas.annotations.responses.ApiResponse(
              responseCode = ResponseCode.NOT_FOUND,
              description = "해당 UUID 를 가진 채널이 없음",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorInfoRes.class),
                  examples = @ExampleObject(
                      name = "해당 UUID 를 가진 채널이 없음 예시",
                      value = """
                          {
                              "code": "CHANNEL_001",
                              "message": "해당 UUID를 가진 채널이 존재하지 않습니다.",
                              "httpStatus": 404
                          }
                          """
                  )
              )
          ),
      }
  )
  ResponseEntity<Void> deleteChannel(@PathVariable UUID channelId);
}
