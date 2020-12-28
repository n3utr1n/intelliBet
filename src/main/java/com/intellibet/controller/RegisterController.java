package com.intellibet.controller;


import com.intellibet.dto.EventForm;
import com.intellibet.dto.UserForm;
import com.intellibet.service.EventService;
import com.intellibet.service.UserService;
import com.intellibet.validator.UserFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@SpringBootApplication
public class RegisterController {

    @Autowired
    private UserFormValidator userFormValidator;

    @Autowired
    private UserService userService;

    @Autowired
    private EventService eventService;

    @GetMapping({"/register"})
    public String getRegisterPage(Model model) {
        model.addAttribute("userForm", new UserForm());
        return "register";
    }

    @PostMapping({"/register"})
    public String postRegisterPage(@ModelAttribute("userForm") UserForm userForm, BindingResult bindingResult) {
        userFormValidator.validate(userForm, bindingResult);
        if (bindingResult.hasErrors()) {
            return "register";
        }
        userService.save(userForm);
        userService.markRegistrationSuccessful(userForm);
        System.out.println(userForm);
        return "register";

    }




}
