package com.sprint.mission.discodeit.service.util;

import com.sprint.mission.discodeit.entity.User;

import java.util.concurrent.Flow;

public abstract class StaticString {
    public static final String DELETE_USER ="user is deleted : ";
    public static final String DELETE_MESSAGE ="message is deleted : ";
    public static final String DELETE_CHANNEL ="channel is deleted : ";

    public static final String CHANNEL_NOT_EXIST ="channel is not exist. : ";
    public static final String USER_NOT_EXIST ="user is not exist : ";
    public static final String MESSAGE_NOT_EXIST ="message is not exist : ";

    public static final String CHANNEL_EXIST ="channel is already exist. : ";
    public static final String USER_EXIST ="user is already exist : ";
    public static final String MESSAGE_EXIST ="message is already exist : ";

    public static final String CHANNEL_ALREADY_DELETED ="channel is already deleted. : ";
    public static final String USER_ALREADY_DELETED ="user is already deleted. : ";
    public static final String MESSAGE_ALREADY_DELETED ="message is already deleted. : ";

    public static final String CREATE_CHANNEL ="channel is created : ";
    public static final String CREATE_USER ="user is created: ";
    public static final String CREATE_MESSAGE ="message is created : ";

    public static final String WRONG_TYPE ="Wrong type Please check the walkthrough ";
    public static final String WRONG_INPUT ="Wrong input Please use !help command";
    public static final String WRONG_COMMAND ="Wrong command Please use !help command";
    public static final String NULL_INPUT ="No input, cancel command";

    public static final String CHANNEL_EMPTY ="channel is empty. create channel";
    public static final String USER_EMPTY ="user is empty create user";
    public static final String MESSAGE_EMPTY ="message is empty create message";
    public static final String VALIDATE_FAIL ="validate test fail";

    public static final String DATA_PATH = "src/main/java/com/sprint/mission/discodeit/data/";
    public static final String DEFAULT_CHANNEL_NAME = "defaultChannel";
    public static final String DEFAULT_CHANNEL_DESCRIPTION = "defaultChannel description";
    public static final User DEFAULT_SENDER = User.builder().name("Default Sender").userName("DefaultSender")
            .email("")
            .isOnline(false)
            .build();
    public static final String DISCODEIT_DIRECTORY = "discodeit.repository.file-directory";

}
