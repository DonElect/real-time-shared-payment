package com.tredbase.shared_payment_processing.services;

import com.tredbase.shared_payment_processing.dtos.AuthResponse;
import com.tredbase.shared_payment_processing.dtos.LoginRequest;

public interface UserMgmtService {
    AuthResponse login(LoginRequest loginRequest);
}
