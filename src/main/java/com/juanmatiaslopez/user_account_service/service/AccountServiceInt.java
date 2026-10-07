package com.juanmatiaslopez.user_account_service.service;


import com.juanmatiaslopez.user_account_service.dto.AccountDTO;
import com.juanmatiaslopez.user_account_service.dto.ApiResponse;
import com.juanmatiaslopez.user_account_service.enums.AccountStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AccountServiceInt {

    ApiResponse<AccountDTO> getMyAccount();
    ApiResponse<AccountDTO> getAccountNumber(String accountNumber);
    ApiResponse<AccountDTO> changeAccountStatus(String accountNumber, AccountStatus status);
    ApiResponse<Page<AccountDTO>> getAllAccounts(Pageable pageable);

}
