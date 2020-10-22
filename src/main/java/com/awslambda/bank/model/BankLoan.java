package com.awslambda.bank.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamoDBTable(tableName = "BankLoan")
public class BankLoan {
    @DynamoDBHashKey
    private String loanId;
    @DynamoDBAttribute
    private String accountNumber;
    @DynamoDBAttribute
    private String loanType; //Home, Car, Education, Personal
    @DynamoDBAttribute
    private Long loanAmount;
    @DynamoDBAttribute
    private double rateOfInterest;
    @DynamoDBAttribute
    private double duration;
    @DynamoDBAttribute
    private Date applicationDate;
    public static final String ACCESS_KEY = "";
    public static final String SECRET_KEY = "";

}
