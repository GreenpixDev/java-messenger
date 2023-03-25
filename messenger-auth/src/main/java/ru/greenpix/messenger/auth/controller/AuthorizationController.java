package ru.greenpix.messenger.auth.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.greenpix.messenger.auth.controller.dto.SignUpRequestDto;

@RestController
@RequestMapping("authorization")
public class AuthorizationController {

    @PostMapping("sign-up")
    public void register(
            @RequestBody SignUpRequestDto signUpRequestDto
    ) {

    }

}
