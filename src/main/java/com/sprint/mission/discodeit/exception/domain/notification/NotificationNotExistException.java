package com.sprint.mission.discodeit.exception.domain.notification;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.HashMap;

public class NotificationNotExistException extends NotificationException{

    public NotificationNotExistException(){
        super(ErrorCode.NOTIFICIATION_NOT_EXIST,new HashMap<>());
    }
}
