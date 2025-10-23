package com.sprint.mission.discodeit.entity.dto.userDto;

import com.sprint.mission.discodeit.entity.User;
import lombok.Builder;

import java.util.UUID;

import static com.sprint.mission.discodeit.entity.dto.ChangeTime.changeTime;

/**
 * @param createAt 가입시기
 */
@Builder
public record UserInfoDto(UUID id, String userName, String state, String createAt, String phoneNum) {

    public static UserInfoDto from(User user) {
        return UserInfoDto.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .state(user.getUserState().getDescState())
                .createAt(changeTime(user.getCreatedAt()))
                .phoneNum(user.getPhoneNum())
                .build();
    }

    @Override
    public String toString() {
        return "유저 정보\n" +
                "이름: " + userName + '\n' +
                "상태: " + state + '\n' +
                "가입 시기: " + createAt + '\n' +
                "전화번호: " + phoneNum + '\n';
    }
}
