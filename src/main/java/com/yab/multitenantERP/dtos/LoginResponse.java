package com.yab.multitenantERP.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private String accessToken;
    private final String tokenType = "Bearer";
    private UserDTO user;

    // Custom constructor for accessToken and UserDTO
    public LoginResponse(String accessToken, UserDTO user) {
        this.accessToken = accessToken;
        this.user = user;
    }
}
