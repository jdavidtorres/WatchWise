package com.watchwise.auth;

import com.watchwise.domain.User;
import com.watchwise.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    UserRepository userRepository;

    CustomUserDetailsService service;

    @BeforeEach
    void setup() {
        service = new CustomUserDetailsService(userRepository);
    }

    @Test
    void loadUserByUsernameThrowsWhenMissing() {
        when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> service.loadUserByUsername("missing@example.com"));
    }

    @Test
    void loadUserByUsernameReturnsDetails() {
        User user = new User();
        user.setEmail("present@example.com");
        user.setPasswordHash("hash");
        user.setRoles(Set.of("ROLE_USER"));
        when(userRepository.findByEmail("present@example.com")).thenReturn(Optional.of(user));

        var details = service.loadUserByUsername("present@example.com");
        assertEquals("present@example.com", details.getUsername());
    }
}
