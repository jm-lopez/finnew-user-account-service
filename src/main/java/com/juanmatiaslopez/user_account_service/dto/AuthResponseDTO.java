package com.juanmatiaslopez.user_account_service.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDTO {

    private String token;
    private UserDTO user;
}
