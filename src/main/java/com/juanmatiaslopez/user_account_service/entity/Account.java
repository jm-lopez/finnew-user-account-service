package com.juanmatiaslopez.user_account_service.entity;

import com.juanmatiaslopez.user_account_service.enums.AccountStatus;
import com.juanmatiaslopez.user_account_service.enums.AccountType;
import com.juanmatiaslopez.user_account_service.enums.Currency;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.repository.cdi.Eager;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String accountNumber;

    @Column(nullable = false)
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private final LocalDateTime createdAt = LocalDateTime.now();
}
