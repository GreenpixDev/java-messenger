package ru.greenpix.messenger.friends.entity;

import lombok.Data;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

@Data
@Embeddable
public class Relationship implements Serializable {

    private static final long serialVersionUID = -7146079704101644487L;

    private UUID externalUserId;

    private UUID targetUserId;

}