package com.awslambda.bank.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankLoanRequest {
    private String httpMethod;
    private String loanId;
    private BankLoan bankLoan;

    public BankLoanRequest(String httpMethod, String loanId) {
        this.httpMethod = httpMethod;
        this.loanId = loanId;
    }

    public BankLoanRequest(String httpMethod, BankLoan bankLoan) {
        this.httpMethod = httpMethod;
        this.bankLoan = bankLoan;
    }

//    {
//        "httpMethod": "$context.httpMethod",
//        "bankUser": $input.json('$')
//    }
}
