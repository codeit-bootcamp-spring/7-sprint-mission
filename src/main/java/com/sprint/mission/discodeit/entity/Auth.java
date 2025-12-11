package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 🔥 추가 필수
@AllArgsConstructor
public class Auth extends BaseEntity {
    private UUID userID;

//    public Auth(UUID userID) {
//        this.userID = userID;
//    }
}
