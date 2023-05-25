package ru.greenpix.messenger.chat.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class SendingMessageDto implements Serializable {

    @Size(min = 1, max = 500)
    private String text;

}
