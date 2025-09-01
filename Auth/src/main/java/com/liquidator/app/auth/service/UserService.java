package com.liquidator.app.auth.service;

import com.liquidator.app.auth.dto.UserRegistrationDto;
import com.liquidator.app.auth.entity.User;
import com.liquidator.app.auth.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Service
@Slf4j
public class UserService {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepository userRepository;

    public User registerNewUserAccount(UserRegistrationDto userDto) {
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setPassword(new BCryptPasswordEncoder().encode(userDto.getPassword()));
        user.setEnabled(false);
        userRepository.save(user);

        return user;
    }


    public String validateVerificationToken(String token) {
        log.debug("Attempting to validate token: {}", token);
        User user = userRepository.findByVerificationToken(token).orElse(null);
        if (user == null) {
            log.debug("No user found for token: {}", token);
            return "invalid";
        }
        log.debug("Found user: {} with token: {}", user.getEmail(), user.getVerificationToken());

        user.setEnabled(true);
        userRepository.save(user);
        return "valid";
    }
}
