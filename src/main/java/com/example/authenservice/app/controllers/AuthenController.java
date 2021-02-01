package com.example.authenservice.app.controllers;

import com.example.authenservice.app.models.UserDTO;
import com.example.authenservice.domain.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/v1/auth")
public class AuthenController {

    @Autowired
    private UserService userService;

    @PostMapping(path = "register")
    public ResponseEntity<?> register(@RequestBody @Valid UserDTO userDTO) {
        return userService.add(userDTO);
    }

    @PostMapping(path = "login")
    public ResponseEntity<?> login(@RequestBody @Valid UserDTO userDTO){
        return userService.checkUserAuthen(userDTO);
    }

    @GetMapping(path = "refresh-token")
    public ResponseEntity<?> refreshToken(@RequestHeader("refreshToken") String refreshToken){
        return userService.genNewAccessToken(refreshToken);
    }

    @GetMapping(path = "logout")
    public ResponseEntity<?> logout(@RequestHeader("refreshToken") String refreshToken){
        return userService.logout(refreshToken);
    }

    @GetMapping(path = "active-account/{id}")
    public ResponseEntity<?> activeAccount(@PathVariable("id") int id){
        return userService.activeAccount(id);
    }

    @GetMapping(path = "/member/profile/{id}")
    public ResponseEntity<?> getProfile(@PathVariable("id") Integer id){
        return userService.getOneUser(id);
    }

    @GetMapping(path = "/admin/profile/{id}")
    public ResponseEntity<?> getProfileAdmin(@PathVariable("id") Integer id){
        return userService.getOneUser(id);
    }

}
