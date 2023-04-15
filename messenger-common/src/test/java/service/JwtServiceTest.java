package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.greenpix.messenger.common.model.JwtUser;
import ru.greenpix.messenger.common.provider.JwtSettingsProvider;
import ru.greenpix.messenger.common.service.impl.JwtServiceImpl;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {

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

    @Mock
    Clock clock;

    @Mock
    JwtSettingsProvider jwtSettings;

    JwtServiceImpl jwtService;

    @BeforeEach
    void beforeEach() {
        when(jwtSettings.getSecret()).thenReturn("VGhpcyBpcyB0ZXN0IHNlY3JldCBrZXkgZm9yIEpXVCB0b2tlbg==");

        jwtService = new JwtServiceImpl(jwtSettings, clock);
    }

    @Test
    void generateTokenTest() {
        when(jwtSettings.getExpirationMinutes()).thenReturn((long) Integer.MAX_VALUE);
        when(clock.instant()).thenReturn(FIXED_CLOCK.instant());
        when(clock.getZone()).thenReturn(FIXED_CLOCK.getZone());

        assertEquals(TEST_TOKEN, jwtService.generateToken(TEST_USER));
    }

    @Test
    void validateValidTokenTest() {
        assertTrue(jwtService.validateToken(TEST_TOKEN));
    }

    @Test
    void validateInvalidTokenTest() {
        assertFalse(jwtService.validateToken("some.invalid.token"));
    }

    @Test
    void validateExpiredTokenTest() {
        assertFalse(jwtService.validateToken(TEST_EXPIRED_TOKEN));
    }

    @Test
    void parseUserTest() {
        assertEquals(TEST_USER, jwtService.parseUser(TEST_TOKEN));
    }
}
