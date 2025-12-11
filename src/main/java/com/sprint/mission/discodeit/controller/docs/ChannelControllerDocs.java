package com.sprint.mission.discodeit.controller.docs;


import com.sprint.mission.discodeit.dto.request.CreatePrivateChannelDto;
import com.sprint.mission.discodeit.dto.request.CreatePublicChannelDto;
import com.sprint.mission.discodeit.dto.response.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.update.UpdateChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Channel", description = "Channel API")
public interface ChannelControllerDocs {

  @Operation(summary = "Public Channel 생성")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201",
          description = "Public Channel이 성공적으로 생성됨",
          content = @Content(
              schema = @Schema(implementation = Channel.class)
          )
      )
  })
  ResponseEntity<ChannelResponseDto> createPublicChannel(
      @Parameter(description = "Public Channel 생성 정보")
      @Valid @RequestBody CreatePublicChannelDto request);

  @Operation(summary = "Private Channel 생성")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201",
          description = "Private Channel이 성공적으로 생성됨",
          content = @Content(
              schema = @Schema(implementation = ChannelResponseDto.class)
          )
      )
  })
  ResponseEntity<ChannelResponseDto> createPrivateChannel(
      @Parameter(description = "Private Channel 생성 정보")
      @Valid @RequestBody CreatePrivateChannelDto request);


  @Operation(summary = "Channel 정보 수정")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Channel 정보가 성공적으로 수정됨",
          content = @Content(
              schema = @Schema(implementation = ChannelResponseDto.class)
          )
      ),
      @ApiResponse(
          responseCode = "400",
          description = "Private Channel은 수정할 수 없음",
          content = @Content(
              examples = @ExampleObject(
                  value = "Private Channel cannot be updated"
              )
          )
      ),
      @ApiResponse(
          responseCode = "404",
          description = "Channel을 찾을 수 없음",
          content = @Content(
              examples = @ExampleObject(
                  value = "Channel with id {channelId} not found"
              )
          )
      )
  })
  ResponseEntity<ChannelResponseDto> updatePublicChannel(
      @Parameter(description = "수정할 Channel ID")
      @PathVariable UUID channelId,
      @Parameter(description = "수정할 Channel 정보")
      @RequestBody UpdateChannelDto request);


  @Operation(summary = "User가 참여 중인 Channel 목록 조회")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Channel 목록 조회 성공",
          content = @Content(
              schema = @Schema(implementation = ChannelResponseDto.class)
          )
      )
  })
  ResponseEntity<List<ChannelResponseDto>> findUserAllChannels(
      @RequestParam UUID userId);

  @Operation(summary = "Channel 삭제")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "204",
          description = "Channel 성공적으로 삭제됨"
      ),
      @ApiResponse(
          responseCode = "404",
          description = "Channel을 찾을 수 없음",
          content = @Content(
              examples = @ExampleObject(
                  value = "Channel with id {channelId} not found"
              )
          )
      )
  })
  ResponseEntity<Void> deleteChannel(
      @Parameter(description = "삭제할 Channel ID")
      @PathVariable UUID channelId);


}
