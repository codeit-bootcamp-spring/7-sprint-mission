package com.sprint.mission.discodeit.entity.dto.userDto;

import com.sprint.mission.discodeit.entity.User;
import lombok.Builder;
import lombok.Getter;

import static com.sprint.mission.discodeit.entity.dto.ChangeTime.changeTime;

@Getter
@Builder
public class UserInfoDto {

    private final String userName;
    private final String state;
    private final String createAt;  // 가입시기
    private final String phoneNum;

    public static UserInfoDto from(User user) {
        return UserInfoDto.builder()
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
