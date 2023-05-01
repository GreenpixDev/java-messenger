package ru.greenpix.messenger.chat.dto;

import lombok.Data;

import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
public class SendingMessageDto implements Serializable {

    @Size(max = 500)
    private final String text;

}
