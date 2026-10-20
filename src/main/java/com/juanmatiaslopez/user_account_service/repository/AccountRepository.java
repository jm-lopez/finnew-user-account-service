package com.juanmatiaslopez.user_account_service.repository;

import com.juanmatiaslopez.user_account_service.entity.Account;
import com.juanmatiaslopez.user_account_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByAccountNumber(String accountNumber);
    boolean existsByAccountNumber(String accountNumber);
    Optional<Account> findByUser(User user);
}
