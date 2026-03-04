package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.ChannelCreateCommand;
import com.sprint.mission.discodeit.dto.channel.ChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

    // 채널 생성
    ChannelResponseDto createChannel(
            ChannelCreateCommand createCommand
    );

    // 채널 수정 -> 어차피 DB없으니 넘겨주는 값보고 같은지 비교후 다르면 해당 부분수정(그래야 "" 이런것도 지운걸로 인식할테니)
    void updateChannel(
            UUID channelId, ChannelUpdateRequestDto requestDto); // TODO: 추후 컨트롤러 계층생성시 파라미터를 DTO로 변경(파라미터가 길어질시)

    // 채널 삭제
    void deleteChannel(UUID channelId);

    // 채널 읽기(정보보기위해)
    ChannelResponseDto getChannel(UUID channelId);
    // 초기 메세지 가져오기 // 어차피 실시간 아니므로 단일 혹은 요청시에 보내는걸로?

    // 전체 채널목록 불러오기 getAllChannels (전체 체널 리스트)
    List<ChannelResponseDto> getAllChannels();

    List<ChannelResponseDto> getAllChannelsByUserId(UUID userId);

//    // 채널 입장
//    void joinChannel(UUID channelId, UUID userId);
//
//    // 채널 퇴장
//    void leaveChannel(UUID channelId, UUID userId);


    // 참여인원조회 getAllMembers
    List<User> getAllMembers(UUID channelId);

}
