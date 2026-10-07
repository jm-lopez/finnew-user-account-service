package com.juanmatiaslopez.user_account_service.controller;

import com.juanmatiaslopez.user_account_service.dto.ApiResponse;
import com.juanmatiaslopez.user_account_service.dto.UserWithAccountDTO;
import com.juanmatiaslopez.user_account_service.service.UserServiceInt;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor

public class UserController {

    private final UserServiceInt userService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserWithAccountDTO>> getMyProfile(){
        return ResponseEntity.ok(userService.getMyDetails());
    }
}
