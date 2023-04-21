package ru.greenpix.messenger.friends.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.greenpix.messenger.friends.repository.BlacklistRepository;
import ru.greenpix.messenger.jwt.manager.JwtManager;

@Tag("e2e")
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BlacklistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BlacklistRepository blacklistRepository;

    @Autowired
    private JwtManager jwtManager;

    private String token;

    @BeforeAll
    public void beforeAll() {
        blacklistRepository.deleteAll();
    }

}
