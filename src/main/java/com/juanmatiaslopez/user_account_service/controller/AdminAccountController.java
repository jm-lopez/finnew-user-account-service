package com.juanmatiaslopez.user_account_service.controller;

import com.juanmatiaslopez.user_account_service.dto.AccountDTO;
import com.juanmatiaslopez.user_account_service.dto.ApiResponse;
import com.juanmatiaslopez.user_account_service.enums.AccountStatus;
import com.juanmatiaslopez.user_account_service.service.AccountServiceInt;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account/admin")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminAccountController {

    public final AccountServiceInt accountService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<Page<AccountDTO>>> getAllAccounts(@PageableDefault(page = 0, size = 100)Pageable pageable){
        return ResponseEntity.ok(accountService.getAllAccounts(pageable));
    }

    @PatchMapping("/status")
    public ResponseEntity<ApiResponse<AccountDTO>> changeAccountStatus(@RequestParam String accountNumber, @RequestParam AccountStatus accountStatus){
        return ResponseEntity.ok(accountService.changeAccountStatus(accountNumber, accountStatus));
    }

}
