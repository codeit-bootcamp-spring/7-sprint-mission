package com.sprint.mission.discodeit.service.file;

public enum Path {
   RooT_PATH("C:\\Users\\qlqhr\\sprint");

    private final String path;

    Path(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
