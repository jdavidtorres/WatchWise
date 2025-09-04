package co.com.jdti.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    AuthService authService;

    AuthController controller;

    @BeforeEach
    void setup() {
        controller = new AuthController(authService);
    }

    @Test
    void registerReturnsToken() {
        when(authService.register("tester@example.com", "secret")).thenReturn("jwt");
        var res = controller.register(new AuthController.RegisterRequest("tester@example.com", "secret"));
        assertThat(res.getBody().token()).isEqualTo("jwt");
    }

    @Test
    void loginReturnsToken() {
        when(authService.login("tester@example.com", "secret")).thenReturn("jwt");
        var res = controller.login(new AuthController.LoginRequest("tester@example.com", "secret"));
        assertThat(res.getBody().token()).isEqualTo("jwt");
    }
}
