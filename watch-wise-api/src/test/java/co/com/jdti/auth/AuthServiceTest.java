package co.com.jdti.auth;

import co.com.jdti.domain.User;
import co.com.jdti.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    JwtService jwtService;
    @Mock
    AuthenticationManager authenticationManager;
    @InjectMocks
    AuthService authService;

    @Test
    void registerFailsWhenEmailExists() {
        when(userRepository.existsByEmail("taken@example.com")).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> authService.register("taken@example.com", "pwd"));

        verify(userRepository, never()).save(any(User.class));
        verify(jwtService, never()).generateToken(anyString());
    }

    @Test
    void registerCreatesUserAndReturnsToken() {
        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);
        when(passwordEncoder.encode("pwd")).thenReturn("hash");
        when(jwtService.generateToken("new@example.com")).thenReturn("jwt");

        String token = authService.register("new@example.com", "pwd");

        verify(userRepository).save(any(User.class));
        verify(jwtService).generateToken("new@example.com");
        assertThat(token).isEqualTo("jwt");
    }

    @Test
    void loginDelegatesToAuthManager() {
        when(jwtService.generateToken("u@example.com")).thenReturn("jwt");

        String token = authService.login("u@example.com", "pw");

        verify(authenticationManager).authenticate(new UsernamePasswordAuthenticationToken("u@example.com", "pw"));
        verify(jwtService).generateToken("u@example.com");
        assertThat(token).isEqualTo("jwt");
    }
}
