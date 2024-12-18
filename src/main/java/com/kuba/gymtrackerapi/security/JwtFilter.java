package com.kuba.gymtrackerapi.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final UserDetailsService userDetailsService;

    @Qualifier("handlerExceptionResolver")
    private final HandlerExceptionResolver resolver;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            log.info("[REQEUST]: Method: {} Path: {}", request.getMethod(), request.getServletPath());

            if (request.getServletPath().contains("/auth")) {
                filterChain.doFilter(request, response);

                return;
            }

            String accessToken = null;

            if (request.getCookies() != null) {
                for (Cookie cookie : request.getCookies()) {
                    if (cookie.getName().equals("access_token")) {
                        accessToken = cookie.getValue();
                    }
                }
            }

            log.info("Checking if access token is null");
            if (accessToken == null) {
                log.info("Access token is null");
                filterChain.doFilter(request, response);

                return;
            }

            final String email = jwtService.extractUsername(accessToken);

            log.info("Checking if email isn't null and getAuthentication() is null - access token: {} ", accessToken);
            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                log.info("email: {}", email);
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                log.info("Checking if token is valid");
                if (jwtService.isTokenValid(accessToken, userDetails)) {
                    log.info("Token is valid");
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("Exception caught in jwtFilter: ", e);
            Cookie accessTokenCookie = new Cookie("access_token", null);
            accessTokenCookie.setHttpOnly(true);
            accessTokenCookie.setSecure(true);
            accessTokenCookie.setMaxAge(0);
            accessTokenCookie.setPath("/");
            response.addCookie(accessTokenCookie);

            resolver.resolveException(request, response, null, e);
        }
    }
}
