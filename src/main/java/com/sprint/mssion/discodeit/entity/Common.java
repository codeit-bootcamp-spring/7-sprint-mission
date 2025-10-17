package com.sprint.mssion.discodeit.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class Common implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public UUID id;
    public Long createAt;
    public Long updateAt;

    public Common() {
        this.id = UUID.randomUUID();
        this.createAt = System.currentTimeMillis();
        this.updateAt = System.currentTimeMillis();
    }

    public UUID getId() {
        return id;
    }

    public Long getCreateAt() {
        return createAt;
    }

    public Long getUpdateAt() {
        return updateAt;
    }

    public void touch(){
        this.updateAt = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "Common{" +
                "id=" + id +
                ", createAt=" + createAt +
                ", updateAt=" + updateAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Common common = (Common) o;
        return Objects.equals(id, common.id) && Objects.equals(createAt, common.createAt) && Objects.equals(updateAt, common.updateAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createAt, updateAt);
    }
}
