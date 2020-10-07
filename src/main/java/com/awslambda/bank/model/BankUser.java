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
@DynamoDBTable(tableName = "BankUser")
public class BankUser {
    @DynamoDBHashKey
    private String accountNumber;
    @DynamoDBAttribute
    private String name;
    @DynamoDBAttribute
    private String userName;
    @DynamoDBAttribute
    private String password;
    @DynamoDBAttribute
    private String address;
    @DynamoDBAttribute
    private String state;
    @DynamoDBAttribute
    private String loanId;
    private String country;
    private String email;
    private String pan;
    private String contactNo;
    private Date dob;
    private String accountType;
}
