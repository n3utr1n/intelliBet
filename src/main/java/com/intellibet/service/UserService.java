package com.intellibet.service;

import com.intellibet.dto.BetForm;
import com.intellibet.dto.DepositForm;
import com.intellibet.dto.UserForm;
import com.intellibet.dto.WithdrawForm;
import com.intellibet.mapper.UserMapper;
import com.intellibet.model.Role;
import com.intellibet.model.User;
import com.intellibet.repository.RoleRepository;
import com.intellibet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private static DecimalFormat decimalFormat = new DecimalFormat("#.##");

    public void save(UserForm userForm) {
        User user = userMapper.map(userForm);
        assignRoles(user);
        encodePassword(user);
        userRepository.save(user);
    }

    private void assignRoles(User user) {

        final List<Role> allRoles = roleRepository.findAll();
        user.setRoles(new HashSet<>(allRoles));
    }

    private void encodePassword(User user) {
        String passwordInPlainText = user.getPassword();
        String passwordEncoded = bCryptPasswordEncoder.encode(passwordInPlainText);
        user.setPassword(passwordEncoded);

    }

    public void markRegistrationSuccessful(UserForm userForm) {

        userForm.setPageSection("section-4");

    }

    public void deposit(DepositForm depositForm, String authenticatedUserEmail) {
        User user = userRepository.findByEmail(authenticatedUserEmail);
        addAmountToUser(user, depositForm);
        userRepository.save(user);
    }

    private void addAmountToUser(User user, DepositForm depositForm) {
        Double amountToBeAdded = Double.parseDouble(depositForm.getDepositAmount());

//      Double existingAmount = user.getBalance() == null ? 0 : user.getBalance();
//      mai sus e varianta cu ternary operator, care e echivalenta cu cea de mai jos
        Double existingAmount = user.getBalance();
        if (existingAmount == null) {
            existingAmount = (double) 0;
        }

        user.setBalance(existingAmount + amountToBeAdded);
    }

    public DepositForm getDepositFormBy(String userEmail) {
        User user = userRepository.findByEmail(userEmail);
        DepositForm result = new DepositForm();
        Double existingAmount = user.getBalance();
        if (existingAmount == null) {
            existingAmount = (double) 0;
        }
        result.setBalance(decimalFormat.format(existingAmount));
        return result;
    }

    public void withdraw(WithdrawForm withdrawForm, String authenticatedUserEmail) {
        User user = userRepository.findByEmail(authenticatedUserEmail);
        subtractAmountFromUser(user, withdrawForm);
        userRepository.save(user);
    }

    private void subtractAmountFromUser(User user, WithdrawForm withdrawForm) {

        Double amountToBeSubstracted = Double.parseDouble(withdrawForm.getWithdrawAmount());

//      Double existingAmount = user.getBalance() == null ? 0 : user.getBalance();
//      mai sus e varianta cu ternary operator, care e echivalenta cu cea de mai jos
        Double existingAmount = user.getBalance();
        if (existingAmount == null) {
            existingAmount = (double) 0;
        }
        if (existingAmount >= amountToBeSubstracted) {
            user.setBalance(existingAmount - amountToBeSubstracted);
        }
    }

    public WithdrawForm getWithdrawFormBy(String userEmail) {
        User user = userRepository.findByEmail(userEmail);
        WithdrawForm result = new WithdrawForm();
        Double existingAmount = user.getBalance();
        if (existingAmount == null) {
            existingAmount = (double) 0;
        }
        result.setBalance(existingAmount.toString());
        return result;
    }

}
