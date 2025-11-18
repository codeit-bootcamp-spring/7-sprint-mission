package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/channels")
public class ChannelController {

  private final ChannelService channelService;

  /**
   * Public Channel 생성 API
   * POST /api/channels/public
   * 요청 본문으로 PublicChannelCreateRequest(JSON)를 받고,
   * 생성된 Channel 전체 정보를 201(CREATED) 상태 코드와 함께 반환한다.
   */
  @RequestMapping(
      method = RequestMethod.POST,
      value = "/public",
      consumes = "application/json",
      produces = "application/json"
  )
  public ResponseEntity<Channel> createPublic(@RequestBody PublicChannelCreateRequest request) {
    Channel createdChannel = channelService.create(request);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(createdChannel);
  }

  /**
   * Private Channel 생성 API
   * POST /api/channels/private
   * 요청 본문으로 PrivateChannelCreateRequest(JSON)를 받고,
   * 생성된 Channel 전체 정보를 201(CREATED) 상태 코드와 함께 반환한다.
   */
  @RequestMapping(
      method = RequestMethod.POST,
      value = "/private",
      consumes = "application/json",
      produces = "application/json"
  )
  public ResponseEntity<Channel> createPrivate(@RequestBody PrivateChannelCreateRequest request) {
    Channel createdChannel = channelService.create(request);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(createdChannel);
  }

  /**
   * Channel 정보 수정 API
   * PATCH /api/channels/{channelId}
   * - path variable로 수정할 Channel ID를 받고
   * - 요청 본문으로 PublicChannelUpdateRequest(JSON)를 받는다.
   * Public Channel만 수정 가능하며, Private Channel 수정 시 서비스에서 예외를 던진다.
   */
  @RequestMapping(
      method = RequestMethod.PATCH,
      value = "/{channelId}",
      consumes = "application/json",
      produces = "application/json"
  )
  public ResponseEntity<Channel> update(
      @PathVariable("channelId") UUID channelId,
      @RequestBody PublicChannelUpdateRequest request
  ) {
    Channel updatedChannel = channelService.update(channelId, request);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(updatedChannel);
  }

  /**
   * Channel 삭제 API
   * DELETE /api/channels/{channelId}
   * - path variable로 삭제할 Channel ID를 받는다.
   * 삭제 성공 시 204(NO_CONTENT)를 반환한다.
   */
  @RequestMapping(
      method = RequestMethod.DELETE,
      value = "/{channelId}"
  )
  public ResponseEntity<Void> delete(@PathVariable("channelId") UUID channelId) {
    channelService.delete(channelId);
    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .build();
  }

  /**
   * User가 참여 중인 Channel 목록 조회 API
   * GET /api/channels?userId={userId}
   * - query parameter로 User ID를 받고,
   * - 해당 사용자가 볼 수 있는 ChannelDto 리스트를 반환한다.
   */
  @RequestMapping(
      method = RequestMethod.GET,
      produces = "application/json"
  )
  public ResponseEntity<List<ChannelDto>> findAll(@RequestParam("userId") UUID userId) {
    List<ChannelDto> channels = channelService.findAllByUserId(userId);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(channels);
  }
}
