package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
/*
memberId :
    1. 배열
        구조가 단순하고 직관적이다.
        고정된 크기를 가지고 있어서 메모리 관리가 빠르다.
        대신 고정된 값을 넣어야하기 때문에 매번 배열을 새로 만들어야 함.
        멤버 중복 검사 / 예외 검사에 구현이 필요.
    2. Array
        멤버 추가/삭제가 배열보다 편하고 구현이 단순함.
        순서대로 나열할 필요가 있다면 Array가 좋다.
        대신 중복 검사 / 예외 검사에 구현이 필요. ( 배열보단 낫다 )
    3. Set
        HashSet > 중복 방지나 탐색 등을 지원하는 각종 함수가 있다.
        대신 순서가 없어서 순서가 고정되어야 한다면 LinkedHashSet을 사용해야 한다.
    4. Map
        맵핑되는 것으로 이해했음, 빠른 조회에 매우 용이함.
        각 인자별 추가로 데이터가 필요하다면 관리에 좋음
        간단하게 구성하기에는 과하기도 하지만 실제 현업에서는 더 많이 쓰일 것 같음.
 */

    // 데이터 관리, 실제 변경 부분 구현!!
@Getter
@ToString
public class Channel extends BaseEntity {
    private String channelName; // 채널 명
    private final ChannelType type; // 0: 음성채널, 1: 채팅채널
    private int slowModeSeconds; // 슬로우모드 초(s)
    private final Map<UUID, UserRole> members = new HashMap<>();
   // private UUID ownerId; // 추후 채널 삭제/멤버 강퇴 등 권한적인 내용 사용시 사용

    public Channel(ChannelType type, String channelName) {
        if(type == null) { throw new IllegalArgumentException("type cannot be null"); }
        this.type = type;
        this.channelName = VerifiedUtils.verifyName(channelName);
    }

    public void setChannelName(String channelName) {
        String v = VerifiedUtils.verifyName(channelName);
        if(!v.equals(this.channelName)) {
            this.channelName = v;
            reUpdatedAt();
        }
    }

    public void setSlowModeSeconds(int slowModeSeconds) {
        if(slowModeSeconds < 0) {
            throw new IllegalArgumentException("slowModeSeconds cannot be negative");
        }
        if(this.slowModeSeconds != slowModeSeconds) {
            this.slowModeSeconds = slowModeSeconds;
            reUpdatedAt();
        }
    }

    public boolean join(UUID memberId) {
        if(memberId == null) { throw new IllegalArgumentException("memberId cannot be null"); }
        // map.put은 이전값을 리턴한다!!
        // 이전값이 없다면 null을 리턴, 있다면 value를 리턴!
        // 그래서 null이면 멤버 추가, value가 있다면 변경!
        boolean access = members.putIfAbsent(memberId,UserRole.MEMBER) == null;
        if(access) reUpdatedAt();
        return access;
    }

    public boolean leave(UUID memberId) {
        if(memberId == null) { throw new IllegalArgumentException("memberId cannot be null"); }
        // remove의 경우 멤버가 있다면 remove 가능 ! > true
        // member가 없으면 null을 리턴! remove가 안되니 > false
        boolean access = members.remove(memberId) != null;
        if(access) reUpdatedAt();
        return access;
    }
}
