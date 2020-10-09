package com.example.authenservice.domain.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Random;

public class PasswordGenerator {
    public static final int STR_LENGTH = 15;

    public static String encrytePassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(STR_LENGTH);
        return encoder.encode(password);
    }

    public static String stringRandomGenerator(){
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 15;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return generatedString;
    }

    public static void main(String[] args) {
        String password = "123456";
        String encrytedPassword = encrytePassword(password);

        System.out.println("Encryted Password: " + encrytedPassword);

    }

}
