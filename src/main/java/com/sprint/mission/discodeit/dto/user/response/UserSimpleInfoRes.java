package com.sprint.mission.discodeit.dto.user.response;

import com.sprint.mission.discodeit.dto.binarycontent.response.BinaryContentInfoRes;
import java.util.UUID;

public record UserSimpleInfoRes(
    UUID userId,                //user UUID
    String nickname,            //닉네임
    String email,               //이메일
    BinaryContentInfoRes profileImg,        //프로필 이미지
    boolean isOnline            //온라인 상태
) {

}
