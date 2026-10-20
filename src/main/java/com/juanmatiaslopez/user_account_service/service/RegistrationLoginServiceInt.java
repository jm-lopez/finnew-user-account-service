package com.juanmatiaslopez.user_account_service.service;

import com.juanmatiaslopez.user_account_service.dto.ApiResponse;
import com.juanmatiaslopez.user_account_service.dto.AuthResponseDTO;
import com.juanmatiaslopez.user_account_service.dto.LoginRequestDTO;
import com.juanmatiaslopez.user_account_service.dto.RegistrationRequestDTO;

public interface RegistrationLoginServiceInt {

    ApiResponse<AuthResponseDTO> registerUser(RegistrationRequestDTO registrationRequestDTO);
    ApiResponse<AuthResponseDTO> loginUser(LoginRequestDTO loginRequestDTO);
}
