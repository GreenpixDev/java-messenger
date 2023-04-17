package ru.greenpix.messenger.auth.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.greenpix.messenger.jwt.manager.JwtManager;
import ru.greenpix.messenger.jwt.manager.impl.JwtManagerImpl;
import ru.greenpix.messenger.jwt.model.JwtUser;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
public class JwtManagerTest {

    static String TEST_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJmYmZmOGJkYS1hMmFiLTRkMzctYWQ5Ni1jNTViOGIzYjU3MDMiLCJleHAiOjEyODg0ODk5MzYyMCwidXNlcm5hbWUiOiJ0ZXN0In0.AbAXGP9s9SKdOWXhzPusZf1CVKybIrIr60c8bPQPMh4";
    static String TEST_EXPIRED_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJmYmZmOGJkYS1hMmFiLTRkMzctYWQ5Ni1jNTViOGIzYjU3MDMiLCJleHAiOi0yNTIwMCwidXNlcm5hbWUiOiJ0ZXN0In0.zqCEN_fxOl1ubYuIc0qO70WAbzOSxHYmzaKJD4FDbuQ";
    static JwtUser TEST_USER = new JwtUser(
            UUID.fromString("fbff8bda-a2ab-4d37-ad96-c55b8b3b5703"),
            "test"
    );
    static Clock FIXED_CLOCK = Clock.fixed(
            LocalDate.EPOCH.atStartOfDay(ZoneId.systemDefault()).toInstant(),
            ZoneId.systemDefault()
    );
    static String SECRET_KEY = "VGhpcyBpcyB0ZXN0IHNlY3JldCBrZXkgZm9yIEpXVCB0b2tlbg==";

    @Mock
    Clock clock;

    JwtManager jwtManager;

    @BeforeEach
    void beforeEach() {
        jwtManager = new JwtManagerImpl(
                SECRET_KEY,
                0,
                clock
        );
    }

    @Test
    void generateTokenTest() {
        jwtManager = new JwtManagerImpl(
                SECRET_KEY,
                Integer.MAX_VALUE,
                clock
        );

        when(clock.instant()).thenReturn(FIXED_CLOCK.instant());
        when(clock.getZone()).thenReturn(FIXED_CLOCK.getZone());

        assertEquals(TEST_TOKEN, jwtManager.generateToken(TEST_USER));
    }

    @Test
    void validateValidTokenTest() {
        assertTrue(jwtManager.validateToken(TEST_TOKEN));
    }

    @Test
    void validateInvalidTokenTest() {
        assertFalse(jwtManager.validateToken("some.invalid.token"));
    }

    @Test
    void validateExpiredTokenTest() {
        assertFalse(jwtManager.validateToken(TEST_EXPIRED_TOKEN));
    }

    @Test
    void parseUserTest() {
        assertEquals(TEST_USER, jwtManager.parseUser(TEST_TOKEN));
    }
}
