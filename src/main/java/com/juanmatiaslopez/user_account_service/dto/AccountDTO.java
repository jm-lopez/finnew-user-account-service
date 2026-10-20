package com.juanmatiaslopez.user_account_service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.juanmatiaslopez.user_account_service.entity.User;
import com.juanmatiaslopez.user_account_service.enums.AccountStatus;
import com.juanmatiaslopez.user_account_service.enums.AccountType;
import com.juanmatiaslopez.user_account_service.enums.Currency;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AccountDTO {

    private Long id;

    private String accountNumber;

    private BigDecimal balance;

    private Currency currency;

    private AccountType accountType;

    private AccountStatus accountStatus;

    private String ownerEmail;

    private LocalDateTime createdAt;
}
