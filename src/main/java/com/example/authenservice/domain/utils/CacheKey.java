package com.example.authenservice.domain.utils;

import lombok.Data;

@Data
public class CacheKey {
    private static final String TOKEN_PREFIX = "cz:";

    public static String getTokenKey(String token) {
        return TOKEN_PREFIX + "refreshToken:" + token;
    }
}
