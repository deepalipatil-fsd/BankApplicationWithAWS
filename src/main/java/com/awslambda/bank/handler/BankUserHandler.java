package com.awslambda.bank.handler;

import com.amazonaws.services.cloudwatchevents.model.PutEventsResultEntry;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.awslambda.bank.model.BankUser;
import com.awslambda.bank.model.BankUserRequest;
import com.amazonaws.services.cloudwatchevents.AmazonCloudWatchEvents;
import com.amazonaws.services.cloudwatchevents.AmazonCloudWatchEventsClientBuilder;
import com.amazonaws.services.cloudwatchevents.model.PutEventsRequest;
import com.amazonaws.services.cloudwatchevents.model.PutEventsRequestEntry;
import com.amazonaws.services.cloudwatchevents.model.PutEventsResult;

import java.util.Date;

public class BankUserHandler implements RequestHandler<BankUserRequest, BankUser> {
    public BankUser handleRequest(
            BankUserRequest request, Context context) {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.defaultClient();
        DynamoDBMapper mapper = new DynamoDBMapper(client);
        BankUser user = null;
        System.out.println("Inside handler ~ " + request);
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
                System.out.println("Registration done.");
                //putCWEvents();
                break;
            default:
                System.out.println("Inside Default ~ ");
                throw new RuntimeException("Invalid Http method, please send either GET/POST requests");
        }
        return user;
    }

    public static void putCWEvents() {
    final AmazonCloudWatchEvents cwe =
            AmazonCloudWatchEventsClientBuilder.defaultClient();
        System.out.println("Inside putCWEvents()");

    final String EVENT_DETAILS = "{\n" +
            "   \"httpMethod\": \"POST\",\n" +
            "   \"bankLoan\":{\n" +
            "    \"loanId\": \"lone123\",\n" +
            "    \"accountNumber\": \"234123453\",\n" +
            "    \"loanType\": \"Car\",\n" +
            "    \"loanAmount\":12345678,\n" +
            "    \"rateOfInterest\": 9.0,\n" +
            "    \"duration\": 5,\n" +
            "    \"applicationDate\": \"2019-08-24T14:45:15\",\n" +
            "    \"accountType\": \"Current\"\n" +
            "    }   \n" +
            "}";

        PutEventsRequestEntry requestEntry = new PutEventsRequestEntry()
                .withTime(new Date())
                .withSource("com.awslambda.bank")
                .withDetailType("loanApplied")
                .withResources("arn:aws:execute-api:us-east-2:389854172703:x03ikd2mtj/*/POST/bankuser/register")
                .withDetail(EVENT_DETAILS);
        PutEventsRequest request = new PutEventsRequest()
                .withEntries(requestEntry);

        PutEventsResult result = cwe.putEvents(request);
        System.out.println("PutEventsResult: " + result.getEntries().get(0));

        for (PutEventsResultEntry resultEntry : result.getEntries()) {
            if (resultEntry.getEventId() != null) {
                System.out.println("Event Id: " + resultEntry.getEventId());
            } else {
                System.out.println("Injection failed with Error Code: " + resultEntry.getErrorCode());
            }
        }
}
}