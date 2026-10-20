package com.juanmatiaslopez.user_account_service.controller;

import com.juanmatiaslopez.user_account_service.dto.AccountDTO;
import com.juanmatiaslopez.user_account_service.dto.ApiResponse;
import com.juanmatiaslopez.user_account_service.dto.UserWithAccountDTO;
import com.juanmatiaslopez.user_account_service.service.AccountServiceInt;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {

    public final AccountServiceInt accountService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<AccountDTO>> getMyAccount(){
        return ResponseEntity.ok(accountService.getMyAccount());
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<ApiResponse<AccountDTO>> getAccountByNumber(@PathVariable String accountNumber){
        return ResponseEntity.ok(accountService.getAccountNumber(accountNumber));
    }

}
