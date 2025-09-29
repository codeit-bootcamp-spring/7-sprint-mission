package com.sprint.mission.discodeit.entity;

public class Channel extends BaseEntity {
    private String name;
    private boolean alarm;
    private String[] members;

    public Channel(String name) {
        this.name = name;
        this.alarm = false;
        this.members = new String[0];
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getMembers() {
        return members;
    }

    public void setMembers(String[] members) {
        this.members = members;
    }

    public boolean isAlarm() {
        return alarm;
    }

    public void setAlarm(boolean alarm) {
        this.alarm = alarm;
    }
}
