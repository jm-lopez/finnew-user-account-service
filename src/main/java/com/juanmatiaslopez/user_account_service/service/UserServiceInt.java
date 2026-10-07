package com.juanmatiaslopez.user_account_service.service;

import com.juanmatiaslopez.user_account_service.dto.ApiResponse;
import com.juanmatiaslopez.user_account_service.dto.UserDTO;
import com.juanmatiaslopez.user_account_service.dto.UserStatisticsDTO;
import com.juanmatiaslopez.user_account_service.dto.UserWithAccountDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserServiceInt {

    ApiResponse<UserWithAccountDTO> getMyDetails();
    ApiResponse<UserWithAccountDTO> searchUser(String email, String accountNumber);
    ApiResponse<Page<UserDTO>> getAllUser(String roleName, Pageable pageable);
    ApiResponse<UserStatisticsDTO> getUserStatistics();
    ApiResponse<String> toggleUserStatus(Long userID);
}
