package com.example.authenservice.domain.services;

import com.example.authenservice.app.models.UserDTO;
import com.example.authenservice.domain.entities.User;
import com.example.authenservice.domain.exceptions.NotFoundException;
import com.example.authenservice.domain.jwt.JwtTokenProvider;
import com.example.authenservice.domain.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class BaseService {

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected JwtTokenProvider jwtTokenProvider;

    @Autowired
    protected AuthenticationManager authenticationManager;

    @Autowired
    protected JavaMailSender javaMailSender;

    @Autowired
    protected CacheManagerService cacheManagerService;

    protected void sendEmailVerify(UserDTO userDTO) {
        User user = userRepository.findUserByEmail(userDTO.getEmail());
        if (Objects.isNull(user)){
            throw new NotFoundException("Email not found!");
        }
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(userDTO.getEmail());
        msg.setSubject("Verify account to login!");
        msg.setText("http://localhost:8080/v1/api/authen/active-account/" + user.getId());
        javaMailSender.send(msg);
    }
}
