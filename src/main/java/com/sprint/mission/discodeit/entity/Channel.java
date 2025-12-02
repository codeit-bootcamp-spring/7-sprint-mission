package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entityElement.ChannelType;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Getter
@Entity
@Table(name = "channels"
)
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class Channel extends BaseUpdatableEntity implements Serializable {


    @Column(name = "name",length = 100)
    private String name;

    @Column(name = "description",length = 500)
    private String description;

    @Column(name = "type",nullable = false,length = 10)
    private ChannelType type;

    public static Channel privateChannelFactory(String name,String description){
      return new Channel(name, description,ChannelType.PRIVATE);
    }

    public static Channel publicChannelFactory(String name,String description){
        return new Channel(name,description,ChannelType.PUBLIC);
    }


}
