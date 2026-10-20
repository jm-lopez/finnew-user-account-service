package com.juanmatiaslopez.user_account_service.service.Impl;

import com.juanmatiaslopez.user_account_service.dto.*;
import com.juanmatiaslopez.user_account_service.entity.Account;
import com.juanmatiaslopez.user_account_service.entity.Role;
import com.juanmatiaslopez.user_account_service.entity.User;
import com.juanmatiaslopez.user_account_service.enums.AccountStatus;
import com.juanmatiaslopez.user_account_service.enums.AccountType;
import com.juanmatiaslopez.user_account_service.enums.Currency;
import com.juanmatiaslopez.user_account_service.exception.BadRequestException;
import com.juanmatiaslopez.user_account_service.exception.NotFoundException;
import com.juanmatiaslopez.user_account_service.kafka.DTO.UserRegistrationEvent;
import com.juanmatiaslopez.user_account_service.kafka.Service.AccountEventsPublisher;
import com.juanmatiaslopez.user_account_service.repository.AccountRepository;
import com.juanmatiaslopez.user_account_service.repository.RoleRepository;
import com.juanmatiaslopez.user_account_service.repository.UserRepository;
import com.juanmatiaslopez.user_account_service.security.JwtService;
import com.juanmatiaslopez.user_account_service.service.RegistrationLoginServiceInt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Slf4j
@RequiredArgsConstructor
public class RegistrationLoginServiceImp implements RegistrationLoginServiceInt {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final ModelMapper modelMapper;
    private final AccountEventsPublisher accountEventsPublisher;

    @Override
    public ApiResponse<AuthResponseDTO> registerUser(RegistrationRequestDTO registrationRequestDTO) {
        if (userRepository.existsByEmail(registrationRequestDTO.getEmail()))
            throw new BadRequestException("Account already exists");
        Set<Role> roles= new HashSet<>();
        String roleName = (registrationRequestDTO.getRole() != null && !registrationRequestDTO.getRole().isBlank() ? registrationRequestDTO.getRole().toUpperCase() : "CUSTOMER");
        //TODO Check why empty role gives an error
        Role assignedRole = roleRepository.findByName(roleName).orElseThrow(()-> new NotFoundException("Role not found"));
        roles.add(assignedRole);
        User newUser = User.builder()
                .email(registrationRequestDTO.getEmail())
                .password(passwordEncoder.encode(registrationRequestDTO.getPassword()))
                .firstName(registrationRequestDTO.getFirstName())
                .lastName(registrationRequestDTO.getLastName())
                .enabled(true)
                .roles(roles)
                .build();
        User savedUser = userRepository.save(newUser);

        //Generate unique account number and create account
        String accountNumber = generateAccountNumber();

        Account accountToSave = Account.builder()
                .accountNumber(accountNumber)
                .balance(BigDecimal.ZERO)
                .currency(Currency.USD)
                .accountType(AccountType.SAVINGS)
                .accountStatus(AccountStatus.ACTIVE)
                .user(savedUser)
                .build();

        accountRepository.save(accountToSave);

        //Publish event to the topic
        UserRegistrationEvent event = UserRegistrationEvent.builder()
                .firstName(savedUser.getFirstName()).lastName(savedUser.getLastName()).email(savedUser.getEmail()).accountNumber(accountNumber).bankName("Finnnew").build();

        accountEventsPublisher.publishUserRegistrationEvent(event);

        UserDTO userDTO = modelMapper.map(savedUser, UserDTO.class);

        AuthResponseDTO response = AuthResponseDTO.builder()
                .user(userDTO)
                .build();

        return new ApiResponse<>(HttpStatus.SC_CONTINUE, "User Account created successfully", response);
    }

    @Override
    public ApiResponse<AuthResponseDTO> loginUser(LoginRequestDTO loginRequestDTO) {
        User user = userRepository.findByEmail(loginRequestDTO.getEmail()).orElseThrow(()-> new BadRequestException("Username is not registered"));
        if (!passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())) throw new BadRequestException("User details are incorrect");
        if (!user.isEnabled()) throw new BadRequestException("User is disabled");

        List<String> roles = user.getRoles().stream().map(Role::getName).toList();

        String token = jwtService.generateToken(user.getEmail(), roles);
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        AuthResponseDTO response = AuthResponseDTO.builder()
                .token(token)
                .user(userDTO)
                .build();
        return new ApiResponse<>(HttpStatus.SC_OK, "User logged in successfully", response);
    }

    private String generateAccountNumber(){
        String generatedAccountNumber;
        ThreadLocalRandom random = ThreadLocalRandom.current();
        do{
            int randomPart = random.nextInt(100_000_000);
            generatedAccountNumber = String.format("00%08d", randomPart);
        }while(accountRepository.existsByAccountNumber(generatedAccountNumber));
        return generatedAccountNumber;
    }
}
