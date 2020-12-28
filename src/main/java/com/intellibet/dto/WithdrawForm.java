package com.intellibet.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class WithdrawForm {

    private String withdrawAmount;
    private String balance;

}
