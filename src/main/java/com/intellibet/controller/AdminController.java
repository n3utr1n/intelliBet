package com.intellibet.controller;

import com.intellibet.dto.EventForm;
import com.intellibet.service.EventService;
import com.intellibet.validator.EventFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@SpringBootApplication
public class AdminController {

  @Autowired
  private EventFormValidator eventFormValidator;

  @Autowired
  private EventService eventService;

  @GetMapping({"/admin"})
  public String getAdminPage(Model model) {
    model.addAttribute("eventForm", new EventForm());
    return "admin";
  }

  @PostMapping({"/admin"})
  public String postAdminPage(@ModelAttribute("eventForm") EventForm eventForm, BindingResult bindingResult) {

    eventFormValidator.validate(eventForm, bindingResult);
    if (bindingResult.hasErrors()) {
      return "admin";
    }

    eventService.save(eventForm);

    return "redirect:/home";
  }
}
