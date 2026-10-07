package com.juanmatiaslopez.user_account_service.kafka.Service;

import com.juanmatiaslopez.user_account_service.kafka.DTO.BalanceUpdateEvent;
import com.juanmatiaslopez.user_account_service.kafka.DTO.UserRegistrationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountEventsPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    //Topics
    private static final String USER_TOPIC = "user-register-event";
    private static final String BALANCE_UPDATE_TOPIC = "balance-update-notification-event";

    public void publishUserRegistrationEvent(UserRegistrationEvent event){
        try{
            kafkaTemplate.send(USER_TOPIC, event.getEmail(), event);
            log.info("Event sent (user), event {}", event.getEmail());
        } catch (Exception e) {
            log.info("Failed to publish user registration event {}", e.getMessage());
        }
    }

    public void publishTransactionNotificationEvent(BalanceUpdateEvent event){
        try{
            kafkaTemplate.send(BALANCE_UPDATE_TOPIC, event.getEmail(), event);
            log.info("Event sent (balance), event {}", event.getEmail());
        } catch (Exception e) {
            log.info("Failed to publish balance update event {}", e.getMessage());
        }
    }
}
