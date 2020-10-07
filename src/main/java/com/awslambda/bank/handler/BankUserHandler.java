package com.awslambda.bank.handler;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.awslambda.bank.model.BankUser;
import com.awslambda.bank.model.BankUserRequest;

public class BankUserHandler implements RequestHandler<BankUserRequest, BankUser> {
    public BankUser handleRequest(
            BankUserRequest request, Context context) {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.defaultClient();
        DynamoDBMapper mapper = new DynamoDBMapper(client);
        BankUser user = null;
        System.out.println("Inside handler ~ ");
        switch (request.getHttpMethod().toUpperCase()) {
            case "GET":
                System.out.println("Inside GET ~ " + request.getAccountNumber());
                String accountNumber = String.valueOf(request.getAccountNumber());
                user = mapper.load(BankUser.class, accountNumber);
                if (user == null)
                    throw new ResourceNotFoundException("No account exists with account number " + accountNumber);
                break;
            case "POST":
                System.out.println("Inside POST ~ " + request.getBankUser());
                user = request.getBankUser();
                mapper.save(user);
                break;
            default:
                System.out.println("Inside Default ~ ");
                throw new RuntimeException("Invalid Http method, please send either GET/POST requests");
        }
        return user;
    }

}
