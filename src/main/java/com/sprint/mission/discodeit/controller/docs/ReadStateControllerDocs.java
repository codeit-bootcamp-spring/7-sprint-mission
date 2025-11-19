package com.sprint.mission.discodeit.controller.docs;

import com.sprint.mission.discodeit.dto.channel.response.ChannelPublicInfoRes;
import com.sprint.mission.discodeit.dto.readstatus.request.ReadStatusCreateReq;
import com.sprint.mission.discodeit.dto.readstatus.response.ReadStatusInfoRes;
import com.sprint.mission.discodeit.exception.ErrorInfoRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "ReadState", description = "Message 읽음 상태 API")
public interface ReadStateControllerDocs {

  @Operation(summary = "Message 읽음 상태 생성")
  @ApiResponses(
      value = {
          @io.swagger.v3.oas.annotations.responses.ApiResponse(
              responseCode = ResponseCode.CREATED,
              description = "Message 읽음 상태가 성공적으로 생성됨",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ReadStatusInfoRes.class),
                  examples = @ExampleObject(
                      name = "Message 읽음 상태가 성공적으로 생성 예시",
                      value = """
                          {
                             "createdAt": "2025-11-15 23:38:35",
                             "updatedAt": "2025-11-15 23:38:35",
                             "userId": "401892ff-989e-4025-bba3-b0ed01a91808",
                             "channelId": "db6da483-b06a-4380-8c54-d3efd4cd1314"
                          }
                          """
                  )
              )
          ),
          @io.swagger.v3.oas.annotations.responses.ApiResponse(
              responseCode = ResponseCode.NOT_FOUND,
              description = "User를 찾을 수 없음",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorInfoRes.class),
                  examples = @ExampleObject(
                      name = "User를 찾을 수 없음 예시",
                      value = """
                          {
                              "code": "USER_001",
                              "message": "해당 UUID를 가진 유저가 존재하지 않습니다.",
                              "httpStatus": 404
                          }
                          """
                  )
              )
          ),
          @io.swagger.v3.oas.annotations.responses.ApiResponse(
              responseCode = ResponseCode.NOT_FOUND,
              description = "Channel를 찾을 수 없음",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorInfoRes.class),
                  examples = @ExampleObject(
                      name = "Channel를 찾을 수 없음 예시",
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
              description = "이미 읽음 상태가 존재함",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorInfoRes.class),
                  examples = @ExampleObject(
                      name = "이미 읽음 상태가 존재함 예시",
                      value = """
                          {
                              "code": "READSTATUS_002",
                              "message": "이미 읽은 상태가 존재합니다.",
                              "httpStatus": 400
                          }
                          """
                  )
              )
          )
      }
  )
  ResponseEntity<ReadStatusInfoRes> createReadStatus(@RequestBody ReadStatusCreateReq req);

  @Operation(summary = "Message 읽음 상태 수정")
  @ApiResponses(
      value = {
          @io.swagger.v3.oas.annotations.responses.ApiResponse(
              responseCode = ResponseCode.OK,
              description = "Message 읽음 상태가 성공적으로 수정됨",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ReadStatusInfoRes.class),
                  examples = @ExampleObject(
                      name = "Message 읽음 상태가 성공적으로 수정됨 예시",
                      value = """
                            {
                               "createdAt": "2025-11-15 23:38:35",
                               "updatedAt": "2025-11-15 23:38:35",
                               "userId": "401892ff-989e-4025-bba3-b0ed01a91808",
                               "channelId": "db6da483-b06a-4380-8c54-d3efd4cd1314"
                            }
                          """
                  )
              )
          ),
          @io.swagger.v3.oas.annotations.responses.ApiResponse(
              responseCode = ResponseCode.NOT_FOUND,
              description = "Message 읽음 상태를 찾을 수 없음",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorInfoRes.class),
                  examples = @ExampleObject(
                      name = "Message 읽음 상태를 찾을 수 없음 예시",
                      value = """
                          {
                              "code": "READSTATUS_001",
                              "message": "해당 UUID를 가진 읽은 상태가 존재하지 않습니다.",
                              "httpStatus": 404
                          }
                          """
                  )
              )
          )
      }
  )
  ResponseEntity<ReadStatusInfoRes> updateReadStatus(@PathVariable UUID readStatusId);

  @Operation(summary = "User의 Message 읽음 상태 목록 조회")
  @ApiResponses(
      value = {
          @io.swagger.v3.oas.annotations.responses.ApiResponse(
              responseCode = ResponseCode.OK,
              description = "Message 읽음 상태 목록 조회 성공",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ReadStatusInfoRes.class),
                  examples = @ExampleObject(
                      name = "Message 읽음 상태 목록 조회 성공 예시",
                      value = """
                          {
                              "createdAt": "2025-11-15 23:38:35",
                              "updatedAt": "2025-11-16 00:00:24",
                              "userId": "401892ff-989e-4025-bba3-b0ed01a91808",
                              "channelId": "db6da483-b06a-4380-8c54-d3efd4cd1314"
                          }
                          """
                  )
              )
          )
      }
  )
  ResponseEntity<ReadStatusInfoRes> getReadStatus(@PathVariable UUID readStatusId);
}
