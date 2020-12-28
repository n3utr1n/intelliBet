package com.intellibet.validator;

import com.intellibet.dto.DepositForm;
import com.intellibet.dto.WithdrawForm;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.math.NumberUtils.isNumber;

@Service
public class WithdrawFormValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(WithdrawForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        WithdrawForm withdrawForm = (WithdrawForm) o;
        if (isEmpty(withdrawForm.getWithdrawAmount()) || !isNumber(withdrawForm.getWithdrawAmount())) {
            errors.rejectValue("withdrawAmount", "withdrawForm.amount.error");
        }
        double amount = Double.parseDouble(withdrawForm.getWithdrawAmount());
        if (Double.compare(amount, 0) <= 0) {
            errors.rejectValue("withdrawAmount", "withdrawForm.amount.error");
        }

    }
}
