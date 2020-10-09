package com.example.authenservice.domain.services;

import com.example.authenservice.domain.models.TokenInfo;
import com.example.authenservice.domain.utils.CacheKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Log4j2
public class CacheManagerService {
    private final long EXPIRE_TIME_TOKEN = 2592000000L; //30 days

    private final RedisTemplate<String, TokenInfo> redisTemplate;

    public void setToken(String token, Integer userId, String email, String password) {
        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setUserId(userId);
        tokenInfo.setEmail(email);
        tokenInfo.setPassword(password);
        log.info(tokenInfo + " ");
        redisTemplate
                .opsForValue()
                .set(CacheKey.getTokenKey(token), tokenInfo, Duration.ofMillis(EXPIRE_TIME_TOKEN));
    }

    public TokenInfo getToken(String token) {
        String key = CacheKey.getTokenKey(token);
        return redisTemplate.opsForValue().get(key);
    }

    public void deleteToken(String token) {
        String key = CacheKey.getTokenKey(token);
        redisTemplate.delete(key);
    }
}
