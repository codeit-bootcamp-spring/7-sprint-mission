package com.sprint.mission.discodeit.entity;

public class Channel extends DefEntity{
    private  String channelName;
    private String channelTopic;


    // getter
    public String getChannelName() {
        return channelName;
    }

    public String getChannelTopic() {
        return channelTopic;
    }

    // 생성자: 필수 값인 채널 이름만 받음
    public Channel(String channelName) {
        super();    // DefEntity의 생성자 호출 (기본 필드 초기화)
        this.channelName = channelName;
        this.channelTopic = null; // 토픽은 선택 사항이므로 null로 초기화
    }

    public void updateChannelName(String newName) {
        this.channelName = newName;
        touch();
    }

    public void updateChannelTopic(String newTopic) {
        this.channelTopic = newTopic;
        touch();
    }


}
