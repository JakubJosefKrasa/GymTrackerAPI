package com.kuba.gymtrackerapi.auth;

import com.kuba.gymtrackerapi.exceptions.AlreadyExistsException;
import com.kuba.gymtrackerapi.exceptions.BadRequestException;
import com.kuba.gymtrackerapi.exceptions.InvalidCredentialsException;
import com.kuba.gymtrackerapi.role.Role;
import com.kuba.gymtrackerapi.role.RoleRepository;
import com.kuba.gymtrackerapi.security.JwtService;
import com.kuba.gymtrackerapi.user.User;
import com.kuba.gymtrackerapi.user.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    private static Cookie createAccessTokenCookie(String cookieValue, int cookieExpiration) {
        Cookie accessTokenCookie = new Cookie("access_token", cookieValue);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(true);
        accessTokenCookie.setMaxAge(cookieExpiration);
        accessTokenCookie.setPath("/");

        return accessTokenCookie;
    }

    public void register(RegisterRequestDTO request) {
        if (userRepository.findByEmail(request.email().toLowerCase()).isPresent()) {
            throw new AlreadyExistsException("Uživatel již existuje!");
        }

        if (!request.password().equals(request.confirmPassword())) {
            throw new BadRequestException("Hesla se musí shodovat!");
        }

        Role userRole = roleRepository.findByName("USER").orElseThrow();

        User newUser = User.builder()
                           .email(request.email().toLowerCase())
                           .password(passwordEncoder.encode(request.password()))
                           .roles(List.of(userRole))
                           .build();

        userRepository.save(newUser);
    }

    public void login(LoginRequestDTO request, HttpServletResponse response) {
        try {
            Authentication authenticatedUser = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email().toLowerCase(),
                            request.password()
                    )
            );

            User user = (User) authenticatedUser.getPrincipal();
            String accessToken = jwtService.generateToken(user);
            Cookie accessTokenCookie = createAccessTokenCookie(accessToken, (int) (jwtExpiration / 1000));

            response.addCookie(accessTokenCookie);
        } catch (BadCredentialsException ex) {
            throw new InvalidCredentialsException("Neplatné přihlašovací údaje!");
        }
    }
}
