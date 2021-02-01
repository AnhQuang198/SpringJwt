package com.example.authenservice.domain.jwt;

import com.example.authenservice.domain.entities.User;
import com.example.authenservice.domain.models.CustomUserDetails;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtTokenProvider {
    @Value("${jwt.secret.key}")
    private String JWT_SECRET;

    @Value("${jwt.secret.time}")
    private long JWT_TOKEN_VALIDITY; //5 minutes

    public String generateToken(CustomUserDetails userDetails) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_TOKEN_VALIDITY);
        return Jwts.builder()
                .setHeader(header())
                .setClaims(claims(userDetails))
                .setSubject(Long.toString(userDetails.getUser().getId()))
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET)
                .compact();
    }

    public Integer getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();
        return Integer.parseInt(claims.getSubject());
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }

    private Map<String, Object> claims(CustomUserDetails customUserDetails){
        Map<String, Object> claimsMap = new HashMap<>();
        claimsMap.put("role", "admin");
        return claimsMap;
    }

    private Map<String, Object> header(){
        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("typ","JWT");
        return headerMap;
    }

}
