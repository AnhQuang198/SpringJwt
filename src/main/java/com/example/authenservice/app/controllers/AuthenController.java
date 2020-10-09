package com.example.authenservice.app.controllers;

import com.example.authenservice.app.models.UserDTO;
import com.example.authenservice.domain.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/v1/api")
public class AuthenController {

    @Autowired
    private UserService userService;

    @PostMapping("/authen/register")
    public ResponseEntity<?> register(@RequestBody @Valid UserDTO userDTO) {
        return userService.add(userDTO);
    }

    @PostMapping("/authen/login")
    public ResponseEntity<?> login(@RequestBody @Valid UserDTO userDTO){
        return userService.checkUserAuthen(userDTO);
    }

    @GetMapping("/authen/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestHeader("refreshToken") String refreshToken){
        return userService.genNewAccessToken(refreshToken);
    }

    @GetMapping("/authen/logout")
    public ResponseEntity<?> logout(@RequestHeader("refreshToken") String refreshToken){
        return userService.logout(refreshToken);
    }

    @GetMapping("/authen/active-account/{id}")
    public ResponseEntity<?> activeAccount(@PathVariable("id") int id){
        return userService.activeAccount(id);
    }

    @GetMapping("/member/profile/{id}")
    public ResponseEntity<?> getProfile(@PathVariable("id") Integer id){
        return userService.getOneUser(id);
    }

}
