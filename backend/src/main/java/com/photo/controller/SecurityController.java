package com.photo.controller;

import com.photo.business.handlers.exceptions.EmailAlreadyInUseException;
import com.photo.business.handlers.exceptions.UsernameAlreadyTakenException;
import com.photo.business.service.UserService;
import com.photo.model.UserCreationDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SecurityController {

    private final UserService userService;

    public SecurityController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/api/auth/login")
    public String login() {
        return "login";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("userCreationDTO", new UserCreationDTO());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("userCreationDTO") @Valid UserCreationDTO userCreationDTO,
                               BindingResult result,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "register";
        }

        try {
            userService.registerUser(userCreationDTO);
        } catch (UsernameAlreadyTakenException | EmailAlreadyInUseException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }

        return "redirect:/login";
    }
}

