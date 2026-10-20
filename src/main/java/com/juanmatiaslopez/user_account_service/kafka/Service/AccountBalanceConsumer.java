package com.juanmatiaslopez.user_account_service.kafka.Service;

import com.juanmatiaslopez.user_account_service.entity.Account;
import com.juanmatiaslopez.user_account_service.enums.transaction.TransactionDirection;
import com.juanmatiaslopez.user_account_service.exception.NotFoundException;
import com.juanmatiaslopez.user_account_service.kafka.DTO.BalanceUpdateEvent;
import com.juanmatiaslopez.user_account_service.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountBalanceConsumer {

    private final AccountRepository accountRepository;
    private final AccountEventsPublisher accountEventsPublisher;

    @KafkaListener(topics = "balance-update-event", groupId = "account-group")
    @Transactional
    public void consumerBalanceUpdate(BalanceUpdateEvent event){
        log.info("Processing balance update");

        Account account = accountRepository.findByAccountNumber(event.getAccountNumber()).orElseThrow(()-> new NotFoundException("Account not found"));

        //Update Balance
        if (event.getTransactionDirection().equals(TransactionDirection.CREDIT)){
            account.setBalance(account.getBalance().add(event.getAmount()));
        } else if (event.getTransactionDirection().equals(TransactionDirection.DEBIT)) {
            account.setBalance(account.getBalance().subtract(event.getAmount()));
        }

        accountRepository.save(account);

        BalanceUpdateEvent balanceUpdateEvent = BalanceUpdateEvent.builder()
                .email(account.getUser().getEmail())
                .firstName(account.getUser().getFirstName())
                .accountNumber(account.getAccountNumber())
                .amount(event.getAmount())
                .transactionDirection(event.getTransactionDirection())
                .transactionReference(event.getTransactionReference())
                .description(event.getDescription())
                .currentBalance(account.getBalance())
                .build();

        accountEventsPublisher.publishTransactionNotificationEvent(balanceUpdateEvent);
    }
}
