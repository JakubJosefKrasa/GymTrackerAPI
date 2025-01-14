package com.kuba.gymtrackerapi.auth;

import com.kuba.gymtrackerapi.auth.dto.LoginRequestDTO;
import com.kuba.gymtrackerapi.auth.dto.RegisterRequestDTO;
import com.kuba.gymtrackerapi.exceptions.AlreadyExistsException;
import com.kuba.gymtrackerapi.exceptions.BadRequestException;
import com.kuba.gymtrackerapi.exceptions.InvalidCredentialsException;
import com.kuba.gymtrackerapi.role.Role;
import com.kuba.gymtrackerapi.role.RoleRepository;
import com.kuba.gymtrackerapi.security.JwtService;
import com.kuba.gymtrackerapi.user.User;
import com.kuba.gymtrackerapi.user.UserRepository;
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
        RegisterRequestDTO registerRequest = new RegisterRequestDTO(
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
        RegisterRequestDTO registerRequest = new RegisterRequestDTO(
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
        RegisterRequestDTO registerRequest = new RegisterRequestDTO(
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
        LoginRequestDTO loginRequest = new LoginRequestDTO("test", "test");
        HttpServletResponse mockedResponse = mock(HttpServletResponse.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(InvalidCredentialsException.class);

        assertThrows(InvalidCredentialsException.class, () -> authenticationService.login(loginRequest, mockedResponse));
    }

    @Test
    public void login_ShouldLoginUser() {
        LoginRequestDTO loginRequest = new LoginRequestDTO("test@gmail.com", "123Password*");
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