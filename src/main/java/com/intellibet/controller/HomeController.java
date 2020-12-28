package com.intellibet.controller;

import com.intellibet.dto.BetForm;
import com.intellibet.dto.PlacedBetForm;
import com.intellibet.dto.DepositForm;
import com.intellibet.dto.EventForm;
import com.intellibet.dto.UserForm;
import com.intellibet.dto.WithdrawForm;
import com.intellibet.service.BetService;
import com.intellibet.service.EventService;
import com.intellibet.service.UserService;
import com.intellibet.validator.BetFormValidator;
import com.intellibet.validator.DespositFormValidator;
import com.intellibet.validator.WithdrawFormValidator;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Controller
@SpringBootApplication
public class HomeController {

    @Autowired
    private EventService eventService;
    @Autowired
    private UserService userService;
    @Autowired
    private DespositFormValidator despositFormValidator;
    @Autowired
    private WithdrawFormValidator withdrawFormValidator;
    @Autowired
    private BetFormValidator betFormValidator;
    @Autowired
    private BetService betService;


    @GetMapping({"/login", "/home", "/"})
    public String getHomePage(@RequestParam(value="category", required = false) String category,
                              @RequestParam(value = "search", required = false) String search,
                                          Model model, Authentication authentication) {

        model.addAttribute("userForm", new UserForm());

        if(search == null) {
            List<EventForm> eventFormList = category == null
                    ? eventService.retrieveFutureEvents()
                    : eventService.retrieveFutureEventsByCategory(category);
            model.addAttribute("eventFormList", eventFormList);
        } else {
            List<EventForm> eventFormList = search == null
                    ? eventService.retrieveFutureEvents()
                    : eventService.retrieveFutureEventsBySearch(search);
            model.addAttribute("eventFormList", eventFormList);
        }

//        List<EventForm> pastEventFormList = eventService.retrievePastEvents();
//        model.addAttribute("pastEventFormList", pastEventFormList);

        BetForm betForm = betService.getBetFormFor(authentication);
        model.addAttribute("betForm", betForm);

        return "home";
    }


    @GetMapping({"/myAccount"})
    public String getMyAccountPage(Model model, Authentication authentication) {

        DepositForm depositForm = userService.getDepositFormBy(authentication.getName());
        model.addAttribute("depositForm", depositForm);

        WithdrawForm withdrawForm = userService.getWithdrawFormBy(authentication.getName());
        model.addAttribute("withdrawForm", withdrawForm);

        return "myAccount";
    }

    @PostMapping({"/deposit"})
    public String postDepositRequest(Model model, @ModelAttribute("depositForm") DepositForm depositForm,
                                     BindingResult bindingResult, Authentication authentication) {
        despositFormValidator.validate(depositForm, bindingResult);
        if (bindingResult.hasErrors()) {
            depositForm = userService.getDepositFormBy(authentication.getName());
            model.addAttribute("depositForm", depositForm);
            return "myAccount";
        }

        String authenticatedUserEmail = authentication.getName();

        userService.deposit(depositForm, authenticatedUserEmail);

        return "redirect:/myAccount";

    }

    @PostMapping({"/home"})
    public String postBetRequest(@ModelAttribute("betForm") BetForm betForm,
                                 BindingResult bindingResult, Model model, Authentication authentication) {


        List<EventForm> eventFormList = eventService.retrieveFutureEvents();

        model.addAttribute("eventFormList", eventFormList);

        betFormValidator.fullValidation(betForm, bindingResult, authentication.getName());
        if (bindingResult.hasErrors()) {
            return "home";
        }
        //aici sigur avem un betForm valid
        betService.placeBet(betForm, authentication.getName());


        return "redirect:/home";
    }

    @PostMapping({"/withdraw"})
    public String postWithdrawRequest(Model model, @ModelAttribute("withdrawForm") WithdrawForm withdrawForm,
                                      BindingResult bindingResult, Authentication authentication) {
        withdrawFormValidator.validate(withdrawForm, bindingResult);
        if (bindingResult.hasErrors()) {
            withdrawForm = userService.getWithdrawFormBy(authentication.getName());
            model.addAttribute("withdrawForm", withdrawForm);
            return "myAccount";
        }

        String authenticatedUserEmail = authentication.getName();

        userService.withdraw(withdrawForm, authenticatedUserEmail);

        return "redirect:/myAccount";

    }

    @GetMapping({"/myBets"})
    public String getMyBetsPage(@RequestParam(value="category", required = false) String category,
                                @RequestParam(value = "search", required = false) String search,
                                Model model, Authentication authentication) {

        String userEmail = authentication.getName();

        List<PlacedBetForm> decidedBets = betService.getDecidedBetsFor(userEmail, Optional.ofNullable(category));
        model.addAttribute("decidedBets", decidedBets);

        List<PlacedBetForm> pendingBets = betService.getPendingBetsFor(userEmail, Optional.ofNullable(category));
        model.addAttribute("pendingBets", pendingBets);

        return "myBets";
    }

    @GetMapping({"/about"})
    public String getAboutPage(){return "/about";}


}
