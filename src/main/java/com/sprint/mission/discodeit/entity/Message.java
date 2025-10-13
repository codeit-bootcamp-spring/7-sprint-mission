package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message extends CommonModel{
    private String msg;
    private UUID userID;

    public Message(String msg) {
        super();
        this.msg = msg;
        this.userID = null;
    }

    //===========================
    public String getMsg() {
        return msg;
    }

    public void updateMsg(String name) {
        this.msg = msg;
    }

    public void setMsg(String msg) {
        this.msg = msg; // 메세지 수정시 호출. so 시간 업데이트 하지 않는다!
    }

    public UUID getUserID() {
        return userID;
    }

    public void setUserID(UUID userID) {
        this.userID = userID;
    }

    @Override
    public String toString() {
        return "Message{" +
                super.toString() +
                "msg='" + msg + '\'' +
                '}';
    }
}
