package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.exception.InvalidInputException;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class Channel extends BaseEntity {

    private String channelName;
    private final ChannelType type;    // 채널타입(public, private)
    private final User channelAdmin;
    private final List<User> members;

    /*
    create
    PRIVATE 채널을 생성할 때:
    [ ] 채널에 참여하는 User의 정보를 받아 User 별 ReadStatus 정보를 생성합니다.
    [ ] name과 description 속성은 생략합니다. -> 무슨 뜻인지 이해를 못했;;

    PUBLIC 채널을 생성할 때에는 기존 로직을 유지합니다. -> public은 시간을 확인하지 않는다는 건가??

    find
    DTO를 활용하여:
    [ ] 해당 채널의 가장 최근 메시지의 시간 정보를 포함합니다.
    [ ] PRIVATE 채널인 경우 참여한 User의 id 정보를 포함합니다.
     */

    public Channel(User user, String channelName, ChannelType type) {
        super();

        this.channelAdmin = user;
        this.type = type;
        if (type == ChannelType.PRIVATE) {
            this.channelName = user.getUserName() + "의 비공개 채널";
        } else {
            if (channelName == null || channelName.isBlank()) {
                this.channelName = user.getUserName() + "의 채널";
            } else this.channelName = channelName;
        }

        this.members = new ArrayList<>();
        members.add(user);
    }

    // updateMessage (private는 수정불가)
    public void changeChannelName(String channelName) {
        if (channelName == null || channelName.isEmpty()) {
            throw new InvalidInputException("잘못된 입력");
        }
        this.channelName = channelName;
        updateTimestamp();
    }

    public boolean addMember(User user) {
        if (!members.contains(user)) {
            members.add(user);
            updateTimestamp();
            return true;
        }
        return false;
    }

    public boolean removeMember(User user) {
        if (members.contains(user)) {
            members.remove(user);
            updateTimestamp();
            return true;
        }
        return false;
    }

/* 현재 코드가 '누가' '어떤 채널'의 정보를 바꾼다는 것이 아니라 미구현
    public boolean isAdmin(User user) {
        return this.channelAdmin.getId().equals(user.getId());
    }
*/
}
