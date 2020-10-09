package com.example.authenservice.domain.models;

import lombok.Data;

@Data
public class TokenInfo {
    private Integer userId;
    private String email;
    private String password;
}
