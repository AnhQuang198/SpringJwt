package com.example.authenservice.app.models;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class UserDTO {
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    private Boolean enabled;

    private String refreshToken;
}
