package com.kuba.GymTrackerAPI.security;

import com.kuba.GymTrackerAPI.exceptions.InvalidCredentialsException;
import com.kuba.GymTrackerAPI.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new InvalidCredentialsException("Neplatné přihlašovací údaje!"));
    }
}
