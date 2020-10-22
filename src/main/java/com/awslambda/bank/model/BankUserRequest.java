package com.awslambda.bank.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankUserRequest {
    private String httpMethod;
    private String accountNumber;
    private BankUser bankUser;

    public BankUserRequest(String httpMethod, String accountNumber) {
        this.httpMethod = httpMethod;
        this.accountNumber = accountNumber;
    }

    public BankUserRequest(String httpMethod, BankUser bankUser) {
        this.httpMethod = httpMethod;
        this.bankUser = bankUser;
    }

//    {
//        "httpMethod":"$.detail.httpMethod",
//            "loanId":"$.detail.loanId",
//            "bankLoan":"$.detail.bankLoan"
//    }
//    {
//        "httpMethod": "$context.httpMethod",
//        "bankUser": $input.json('$')
//    }
}
