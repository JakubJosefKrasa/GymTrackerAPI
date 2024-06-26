package com.kuba.GymTrackerAPI.auth;

import jakarta.servlet.http.HttpServletResponse;

public interface AuthenticationService {
    void register(RegisterRequest request);
    void login(LoginRequest request, HttpServletResponse response);
}
