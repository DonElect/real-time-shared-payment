package com.tredbase.shared_payment_processing.services.impl;

import com.tredbase.shared_payment_processing.dtos.AuthResponse;
import com.tredbase.shared_payment_processing.dtos.LoginRequest;
import com.tredbase.shared_payment_processing.exceptions.InvalidPasswordException;
import com.tredbase.shared_payment_processing.exceptions.UserNotFoundException;
import com.tredbase.shared_payment_processing.models.entities.UserEntity;
import com.tredbase.shared_payment_processing.repositories.UserRepository;
import com.tredbase.shared_payment_processing.security.JWTGenerator;
import com.tredbase.shared_payment_processing.services.UserMgmtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserMgmtServiceImpl implements UserMgmtService {
    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final JWTGenerator jwtGenerator;

    @Override
    public AuthResponse login(LoginRequest loginRequest) {

        UserEntity user = userRepo.findUserEntityByEmail(loginRequest.getEmail().toLowerCase())
                .orElseThrow(()-> new UserNotFoundException("Invalid login details."));

        if (!encoder.matches(loginRequest.getPassword(), user.getPassword())){
            throw new InvalidPasswordException("Invalid password!");
        }

        String token = jwtGenerator.generateNewToken(loginRequest.getEmail().toLowerCase(), 120L);
        String freshToken = jwtGenerator.generateNewToken(loginRequest.getEmail().toLowerCase(), 1440L);

        return new AuthResponse(token, freshToken);
    }
}
