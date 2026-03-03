package com.sprint.mission.discodeit.entityElement;

public enum BinaryContentStatus {
    SUCCESS(0),
    FAILURE(1),
    PROCESSING(2);
    private int value;
    BinaryContentStatus(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }

}
