package com.juanmatiaslopez.user_account_service.service.Impl;

import com.juanmatiaslopez.user_account_service.dto.*;
import com.juanmatiaslopez.user_account_service.entity.Account;
import com.juanmatiaslopez.user_account_service.entity.User;
import com.juanmatiaslopez.user_account_service.exception.BadRequestException;
import com.juanmatiaslopez.user_account_service.exception.NotFoundException;
import com.juanmatiaslopez.user_account_service.repository.AccountRepository;
import com.juanmatiaslopez.user_account_service.repository.UserRepository;
import com.juanmatiaslopez.user_account_service.service.UserServiceInt;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImp implements UserServiceInt {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;

    @Override
    public ApiResponse<UserWithAccountDTO> getMyDetails() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("User details for user {}", email);

        User user = userRepository.findByEmail(email).orElseThrow(()-> new NotFoundException("User not found"));

        Account account = accountRepository.findByUser(user).orElseThrow(()->new NotFoundException("Account not found"));

        UserWithAccountDTO userWithAccountDTO = mapUserWithAccount(user, account);

        return new ApiResponse<>(HttpStatus.SC_OK, "Profile returned", userWithAccountDTO);
    }

    @Override
    public ApiResponse<UserWithAccountDTO> searchUser(String email, String accountNumber) {
        log.info("Searching for user identified by {}", email!=null && !email.isBlank() ? email : accountNumber);

        User user;
        Account account;

        if (email != null && !email.isBlank()) {
            user = userRepository.findByEmail(email).orElseThrow(()-> new NotFoundException("User not found"));
            account = accountRepository.findByUser(user).orElseThrow(()-> new NotFoundException("User not found"));
        }
        else if (accountNumber != null && !accountNumber.isBlank()) {
            account = accountRepository.findByAccountNumber(accountNumber).orElseThrow(()-> new NotFoundException("User not found"));
            user = account.getUser();
        } else {
            throw new BadRequestException("No valid data was found");
        }

        UserWithAccountDTO userWithAccountDTO = mapUserWithAccount(user, account);

        return new ApiResponse<>(HttpStatus.SC_OK, "Profile returned", userWithAccountDTO);
    }

    @Override
    public ApiResponse<Page<UserDTO>> getAllUser(String roleName, Pageable pageable) {
        log.info("Get all users");

        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("createdAt").descending());

        Page<User> userPage;

        if (roleName != null && !roleName.isBlank()) {
            userPage = userRepository.findByRoleName(roleName.toUpperCase(), sortedPageable);
        } else {
            userPage = userRepository.findAll(sortedPageable);
        }

        Page<UserDTO> dtoPage = userPage.map(user -> modelMapper.map(user, UserDTO.class));

        return new ApiResponse<>(HttpStatus.SC_OK, "Users returned", dtoPage);
    }

    @Override
    public ApiResponse<UserStatisticsDTO> getUserStatistics() {
        long total = userRepository.count();
        long enabled = userRepository.countByEnabledTrue();

        UserStatisticsDTO userStatisticsDTO = UserStatisticsDTO.builder()
                .totalUsers(total)
                .activeUsers(enabled)
                .inactiveUsers(total-enabled)
                .totalAccounts(accountRepository.count())
                .averageAccountPerUser(accountRepository.count()/total)
                .customersCount(userRepository.countByRoleName("CUSTOMER"))
                .adminsCount(userRepository.countByRoleName("ADMIN"))
                .build();

        return new ApiResponse<>(HttpStatus.SC_OK, "Statistics returned", userStatisticsDTO);
    }

    @Override
    public ApiResponse<String> toggleUserStatus(Long userID) {
        User user = userRepository.findById(userID).orElseThrow(()-> new NotFoundException("User not found"));
        user.setEnabled(!user.isEnabled());
        userRepository.save(user);
        String message = user.isEnabled() ? "Enabled" : "Disabled";
        return new ApiResponse<>(HttpStatus.SC_OK, "User toggled", message);
    }

    private UserWithAccountDTO mapUserWithAccount(User user, Account account){
        return UserWithAccountDTO.builder()
                .user(modelMapper.map(user, UserDTO.class))
                .account(modelMapper.map(account, AccountDTO.class))
                .build();
    }
}
