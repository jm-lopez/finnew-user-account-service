package com.juanmatiaslopez.user_account_service.controller;

import com.juanmatiaslopez.user_account_service.dto.ApiResponse;
import com.juanmatiaslopez.user_account_service.dto.AuthResponseDTO;
import com.juanmatiaslopez.user_account_service.dto.LoginRequestDTO;
import com.juanmatiaslopez.user_account_service.dto.RegistrationRequestDTO;
import com.juanmatiaslopez.user_account_service.service.RegistrationLoginServiceInt;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final RegistrationLoginServiceInt registrationLoginService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponseDTO>> register(@RequestBody @Valid RegistrationRequestDTO registrationRequest){
        return ResponseEntity.ok(registrationLoginService.registerUser(registrationRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponseDTO>> login(@RequestBody @Valid LoginRequestDTO loginRequest){
        return ResponseEntity.ok(registrationLoginService.loginUser(loginRequest));
    }
}
