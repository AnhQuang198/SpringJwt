package com.example.authenservice.domain.services;

import com.example.authenservice.app.models.UserDTO;
import com.example.authenservice.app.response.UserResponse;
import com.example.authenservice.domain.entities.User;
import com.example.authenservice.domain.entities.types.Role;
import com.example.authenservice.domain.exceptions.NotFoundException;
import com.example.authenservice.domain.models.CustomUserDetails;
import com.example.authenservice.domain.models.TokenInfo;
import com.example.authenservice.domain.utils.PasswordGenerator;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@Log4j2
public class UserService extends BaseService implements UserDetailsService {

    public ResponseEntity<?> add(UserDTO userDTO) {
        User user = User.builder().email(userDTO.getEmail())
                .password(PasswordGenerator.encrytePassword(userDTO.getPassword()))
                .enabled(false).role(Role.ROLE_MEMBER).build();
        userRepository.save(user);
        sendEmailVerify(userDTO);
        return ResponseEntity.ok("Success");
    }

    public ResponseEntity<?> checkUserAuthen(UserDTO userDTO) {
        User user = userRepository.findUserByEmail(userDTO.getEmail());
        if (Objects.isNull(user)) {
            throw new NotFoundException("Email not found");
        }
        if (!user.getEnabled()){
            throw new NotFoundException("Account is not active. Click link in email to active account!");
        }
        //authentication username and password
        String accessToken = authenticationVerify(userDTO.getEmail(), userDTO.getPassword());

        //generate refreshtoken
        String refreshToken = PasswordGenerator.stringRandomGenerator();

        //save refreshtoken to cache redis
        cacheManagerService.setToken(refreshToken, user.getId(), userDTO.getEmail(),userDTO.getPassword());
        return ResponseEntity.ok(new UserResponse(accessToken, refreshToken));
    }

    public ResponseEntity<?> genNewAccessToken(String refreshToken){
        TokenInfo tokenInfo = cacheManagerService.getToken(refreshToken);
        if (Objects.isNull(tokenInfo)){
            throw new NotFoundException("Refresh Token Expired!");
        }
        String accessToken = authenticationVerify(tokenInfo.getEmail(), tokenInfo.getPassword());
        return ResponseEntity.ok(new UserResponse(accessToken, refreshToken));
    }

    public ResponseEntity<?> logout(String refreshToken){
        cacheManagerService.deleteToken(refreshToken);
        return ResponseEntity.ok("Logout Success!");
    }

    public ResponseEntity<?> getOneUser(int id) {
        User user = userRepository.getOne(id);
        if (Objects.isNull(user)) {
            throw new NotFoundException("Email not found");
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(user.getEmail());
        return ResponseEntity.ok(userDTO);
    }

    public ResponseEntity<?> activeAccount(int id) {
        User user = userRepository.findUserById(id);
        if (Objects.isNull(user)) {
            throw new NotFoundException("Email not found!");
        }
        user.setEnabled(true);
        userRepository.save(user);
        return ResponseEntity.ok("Account is actived");
    }

    public String authenticationVerify(String username, String password){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = jwtTokenProvider.generateToken((CustomUserDetails) authentication.getPrincipal());
        return accessToken;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(username);
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("Not found");
        }
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().name()));
        CustomUserDetails customUserDetails = new CustomUserDetails(user, authorities);
        return customUserDetails;
    }

    public UserDetails loadUserById(int id) {
        User user = userRepository.findUserById(id);
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole().name()));
        return new CustomUserDetails(user, authorities);
    }
}
