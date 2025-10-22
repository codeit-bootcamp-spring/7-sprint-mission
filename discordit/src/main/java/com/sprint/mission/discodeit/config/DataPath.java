package com.sprint.mission.discodeit.config;

public final class DataPath {
    private DataPath(){}

    private static final String BASE_PATH = "C:\\Users\\hyo37\\Documents\\7-sprint-mission\\discordit\\src\\main\\java\\com\\sprint\\mission";
    private static final String FILE_IO_PATH = "/repository/file/data";
    public static final String FILE_DIR = BASE_PATH + FILE_IO_PATH;

    public static final String CHANNEL_FILE_PATH = DataPath.FILE_DIR + "/channel.sav";
    public static final String USER_FILE_PATH = DataPath.FILE_DIR + "/user.sav";
    public static final String MESSAGE_FILE_PATH = DataPath.FILE_DIR + "/message.sav";
}
