package com.juanmatiaslopez.user_account_service.service.Impl;

import com.juanmatiaslopez.user_account_service.dto.AccountDTO;
import com.juanmatiaslopez.user_account_service.dto.ApiResponse;
import com.juanmatiaslopez.user_account_service.entity.Account;
import com.juanmatiaslopez.user_account_service.entity.User;
import com.juanmatiaslopez.user_account_service.enums.AccountStatus;
import com.juanmatiaslopez.user_account_service.exception.NotFoundException;
import com.juanmatiaslopez.user_account_service.repository.AccountRepository;
import com.juanmatiaslopez.user_account_service.repository.UserRepository;
import com.juanmatiaslopez.user_account_service.service.AccountServiceInt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImp implements AccountServiceInt {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public ApiResponse<AccountDTO> getMyAccount() {
        log.info("Account for logged user");
        String email = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        User user = userRepository.findByEmail(email).orElseThrow(()-> new NotFoundException("User not found"));
        Account account = accountRepository.findByUser(user).orElseThrow(()-> new NotFoundException("Account not found"));
        AccountDTO accountDTO = modelMapper.map(account, AccountDTO.class);
        accountDTO.setOwnerEmail(account.getUser().getEmail());
        return new ApiResponse<>(HttpStatus.SC_OK, "Account Retrieved", accountDTO);
    }

    @Override
    public ApiResponse<AccountDTO> getAccountNumber(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber).orElseThrow(()-> new NotFoundException("Account not found"));
        AccountDTO accountDTO = modelMapper.map(account, AccountDTO.class);
        accountDTO.setOwnerEmail(account.getUser().getEmail());
        return new ApiResponse<>(HttpStatus.SC_OK, "Account Retrieved", accountDTO);
    }

    @Override
    public ApiResponse<AccountDTO> changeAccountStatus(String accountNumber, AccountStatus status) {
        Account account = accountRepository.findByAccountNumber(accountNumber).orElseThrow(()-> new NotFoundException("Account not found"));
        account.setAccountStatus(status);
        Account savedAccount = accountRepository.save(account);
        AccountDTO accountDTO = modelMapper.map(savedAccount, AccountDTO.class);
        return new ApiResponse<>(HttpStatus.SC_OK, "Account Details updated successfully", accountDTO);
    }

    @Override
    public ApiResponse<Page<AccountDTO>> getAllAccounts(Pageable pageable) {
        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by("createdAt").descending()
        );
        Page<Account> accounts = accountRepository.findAll(sortedPageable);
        Page<AccountDTO> accountDTOS = accounts.map(account -> modelMapper.map(account, AccountDTO.class));
        return new ApiResponse<>(HttpStatus.SC_OK, "Accounts retrieved successfully", accountDTOS);
    }
}
