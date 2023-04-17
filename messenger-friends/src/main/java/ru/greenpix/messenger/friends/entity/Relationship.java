package ru.greenpix.messenger.friends.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class Relationship implements Serializable {

    private static final long serialVersionUID = -7146079704101644487L;

    /**
     * ID целевого пользователя
     * (например, пользователя, который решил подружиться)
     */
    private UUID targetUserId;

    /**
     * ID внешнего (искомого) пользователя
     * (например, пользователя, с которым решили подружиться)
     */
    private UUID externalUserId;

}