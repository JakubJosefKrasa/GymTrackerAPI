package com.kuba.GymTrackerAPI.auth;

import com.kuba.GymTrackerAPI.exceptions.AlreadyExistsException;
import com.kuba.GymTrackerAPI.exceptions.BadRequestException;
import com.kuba.GymTrackerAPI.exceptions.InvalidCredentialsException;
import com.kuba.GymTrackerAPI.role.Role;
import com.kuba.GymTrackerAPI.role.RoleRepository;
import com.kuba.GymTrackerAPI.security.JwtService;
import com.kuba.GymTrackerAPI.user.User;
import com.kuba.GymTrackerAPI.user.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthenticationService authenticationService;

    private Role userRole;

    @BeforeEach
    void setUp() {
        userRole = Role.builder().name("USER").build();
    }

    @Test
    public void register_ShouldThrowAlreadyExistsException() {
        RegisterRequest registerRequest = new RegisterRequest(
                "email@gmail.com",
                "123Password*",
                "123Password*"
        );

        when(userRepository.findByEmail(registerRequest.email().toLowerCase())).thenReturn(Optional.of(new User()));

        assertThrows(AlreadyExistsException.class, () -> authenticationService.register(registerRequest));
        verify(userRepository, times(1)).findByEmail(registerRequest.email().toLowerCase());
    }

    @Test
    public void register_ShouldThrowBadRequestException() {
        RegisterRequest registerRequest = new RegisterRequest(
                "email@gmail.com",
                "123Password*",
                "123Password"
        );

        when(userRepository.findByEmail(registerRequest.email().toLowerCase())).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> authenticationService.register(registerRequest));
        verify(userRepository, times(1)).findByEmail(registerRequest.email().toLowerCase());
    }

    @Test
    public void register_ShouldRegisterUser() {
        RegisterRequest registerRequest = new RegisterRequest(
                "email@gmail.com",
                "123Password*",
                "123Password*"
        );

        when(userRepository.findByEmail(registerRequest.email().toLowerCase())).thenReturn(Optional.empty());
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(userRole));

        authenticationService.register(registerRequest);

        verify(userRepository, times(1)).findByEmail(registerRequest.email().toLowerCase());
        verify(roleRepository, times(1)).findByName("USER");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void login_ShouldThrowInvalidCredentialsException() {
        LoginRequest loginRequest = new LoginRequest("test", "test");
        HttpServletResponse mockedResponse = mock(HttpServletResponse.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(InvalidCredentialsException.class);

        assertThrows(InvalidCredentialsException.class, () -> authenticationService.login(loginRequest, mockedResponse));
    }

    @Test
    public void login_ShouldLoginUser() {
        LoginRequest loginRequest = new LoginRequest("test@gmail.com", "123Password*");
        HttpServletResponse mockedResponse = mock(HttpServletResponse.class);
        Authentication mockedAuthentication = mock(Authentication.class);
        User user = User.builder().id(1L).email("test@gmail.com").roles(List.of(userRole)).build();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(mockedAuthentication);
        when(mockedAuthentication.getPrincipal()).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn("generated_JWT");

        authenticationService.login(loginRequest, mockedResponse);

        verify(mockedResponse).addCookie(argThat(cookie ->
                "access_token".equals(cookie.getName()) && "generated_JWT".equals(cookie.getValue())
        ));
    }
}