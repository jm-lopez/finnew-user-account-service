package com.juanmatiaslopez.user_account_service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDTO {

    @NotNull(message = "Email must be present")
    private String email;
    @NotNull(message = "Password must be present")
    private String password;
}
