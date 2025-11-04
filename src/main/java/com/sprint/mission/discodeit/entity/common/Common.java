package com.sprint.mission.discodeit.entity.common;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;


@Getter
public  class Common implements Serializable {

    //고유값이긴한데
    //삭제하면 파이널이 필요할까?
    //아니면 삭제해도 삭제기록같은걸 저장해서 고유번호는 정말 고유번호인걸까
    private final UUID id;
    //이건 생성시간이니 불변이겠고
    private final Instant createdAt;
    //이것도
    private Instant  updatedAt;



    /*
    공통이니 무었이든 생성되면 고유번호를 가지고
    생성일 자 업데이트 일자가 있고
    처음 업데이트는 생성일자로 하면고
    * */

    public Common() {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }





    //set은 안얻을꺼같은데
    //내가 수정을 해야 할 이유가 없는 필드같다
/*    public long getCreatedAt() {
        return createdAt;
    }*/

  /*  public UUID getId() {
        return id;
    }*/


   /* public long getUpdatedAt() {
        return updatedAt;
    }*/
    //이건 넣어줄수있을듯한데

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
