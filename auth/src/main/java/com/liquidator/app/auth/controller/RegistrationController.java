package com.liquidator.app.auth.controller;

import com.liquidator.app.auth.dto.UserRegistrationDto;
import com.liquidator.app.auth.entity.User;
import com.liquidator.app.auth.event.OnRegistrationCompleteEvent;
import com.liquidator.app.auth.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserRegistrationDto());
        return "registration";
    }

    @PostMapping("/register")
    public String registerUserAccount(@ModelAttribute("user") UserRegistrationDto userDto, Model model) {
        User registered = userService.registerNewUserAccount(userDto);
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered));
        return "redirect:/verify-email";
    }

    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam("token") String token, Model model) {
        log.info("Verifying email for token: " + token);
        String result = userService.validateVerificationToken(token);
        if (result.equals("valid")) {
            model.addAttribute("message", "Your account has been verified successfully.");
            return "verified";
        } else {
            model.addAttribute("message", "Invalid verification token.");
            return "verified";
        }
    }
}
