package com.tredbase.shared_payment_processing.controllers;

import com.tredbase.shared_payment_processing.dtos.LoginRequest;
import com.tredbase.shared_payment_processing.models.ApiResponse;
import com.tredbase.shared_payment_processing.services.UserMgmtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserMgmtController {
    private final UserMgmtService userMgmtService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(new ApiResponse("200", "Login successfully", userMgmtService.login(loginRequest)));
    }
}
