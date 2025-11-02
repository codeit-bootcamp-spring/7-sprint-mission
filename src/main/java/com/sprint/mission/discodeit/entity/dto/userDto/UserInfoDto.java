package com.sprint.mission.discodeit.entity.dto.userDto;

import com.sprint.mission.discodeit.entity.User;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

import static com.sprint.mission.discodeit.entity.dto.ChangeTime.changeTime;

/**
 * @param createAt 가입시기
 */
@Builder
public record UserInfoDto(
        UUID id, String userName, String state,
        Instant createAt, String phoneNum, boolean isOnline
) {

    public static UserInfoDto from(User user, boolean isOnline) {
        return UserInfoDto.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .state(user.getUserState().getDescState()) // 유저가 설정 가능한 상태
                .createAt(user.getCreatedAt())
                .phoneNum(user.getPhoneNum())
                .isOnline(isOnline)     // 시스템의 온라인 상태 여부
                .build();
    }

    @Override
    public String toString() {
        String userStatus = isOnline ? " (online)" : " (offline)";  // 실제로는 띄우지 않을 것

        return "유저 정보\n" +
                "이름: " + userName + userStatus + '\n' +
                "상태: " + state + '\n' +
                "가입 시기: " + createAt + '\n' +
                "전화번호: " + phoneNum + '\n';
    }
}
