package com.awslambda.bank.handler;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;
import com.amazonaws.services.lambda.model.ServiceException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.awslambda.bank.model.BankLoan;
import com.awslambda.bank.model.BankLoanRequest;
import com.awslambda.bank.model.BankUser;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;


public class BankLoanHandler implements RequestHandler<BankLoanRequest, BankLoan> {
    public BankLoan handleRequest(
            BankLoanRequest request, Context context) {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.defaultClient();
        DynamoDBMapper mapper = new DynamoDBMapper(client);
        //List<BankLoan> loanList = new ArrayList<>();
        BankLoan loan = null;
        System.out.println("Inside handler ~ ");
        switch (request.getHttpMethod().toUpperCase()) {
            case "GET" :
                System.out.println("Inside Loan GET ~ " + request.getLoanId());
                String loanId = String.valueOf(request.getLoanId());
                loan = mapper.load(BankLoan.class,loanId);
                if(loan == null)
                    throw new ResourceNotFoundException("No loan applied with loan id "+ loanId);
                break;
            case "POST" :
                System.out.println("Inside Loan POST ~ " + request.getBankLoan());
                loan = request.getBankLoan();
                mapper.save(loan);
                break;
            case "UPDATE" :
                try {
                System.out.println("Inside Loan Update ~ " + request.getBankLoan());
                BankUser bankUser = mapper.load(BankUser.class,request.getBankLoan().getAccountNumber());
                bankUser.setLoanId( request.getBankLoan().getLoanId());
                ObjectMapper objMapper = new ObjectMapper();
                String jsonString = objMapper.writeValueAsString(bankUser);
                    System.out.println(jsonString);
                InvokeRequest invokeRequest = new InvokeRequest()
                        .withFunctionName("BankApplicationLambdaFunction")
                        .withPayload("{\n" +
                                "\t\"httpMethod\": \"POST\",\n" +
                                "\t\"bankUser\" :" + jsonString +
                                "}");
                InvokeResult invokeResult = null;

                    AWSLambda awsLambda = AWSLambdaClientBuilder.standard()
                            .withCredentials(new AWSCredentialsProvider() {
                                @Override
                                public AWSCredentials getCredentials() {
                                    return new AWSCredentials() {
                                        @Override
                                        public String getAWSAccessKeyId() {
                                            return "";
                                        }

                                        @Override
                                        public String getAWSSecretKey() {
                                            return "";
                                        }
                                    };
                                }

                                @Override
                                public void refresh() {
                                    //Do nothing
                                }
                            })
                            .withRegion(Regions.US_EAST_2).build();

                    invokeResult = awsLambda.invoke(invokeRequest);

                    String ans = new String(invokeResult.getPayload().array(), StandardCharsets.UTF_8);

                    //write out the return value
                    System.out.println(ans);

                System.out.println(invokeResult.getStatusCode());
                System.out.println("Inside Loan Update in Bank User ~ " + bankUser);
                } catch (JsonParseException e) {
                    System.out.println(e);
                } catch (ServiceException e) {
                    System.out.println(e);
                } catch (Exception e) {
                    System.out.println(e);
                }
               // mapper.save(bankUser);
                break;
            default:
                System.out.println("Inside Loan Default ~ ");
                throw new RuntimeException("Invalid Http method, please send either GET/POST requests");
        }
        return loan;
    }
    public static void main(String[] args) {
        System.out.println("inside Main..");
        //handleRequest(new BankLoanRequest("UPDATE", "1234567890", new BankLoan()));
        InvokeRequest invokeRequest = new InvokeRequest()
                .withFunctionName("BankApplicationLambdaFunction")
                .withPayload("{\n" +
                        "\t\"httpMethod\": \"POST\",\n" +
                        "\t\"accountNumber\": \"1234567891\",\n" +
                        "\t\"bankUser\" :{\n" +
                        "\t\t\"accountNumber\": \"1234567891\",\n" +
                        "\t\t\"loanId\": \"0\",\n" +
                        "        \"name\": \"Deepali\",\n" +
                        "        \"userName\": \"deepali15\",\n" +
                        "        \"password\": \"deepali\",\n" +
                        "        \"email\": \"deepali@gmail.com\",\n" +
                        "        \"pan\": \"TYHII0975D\",\n" +
                        "        \"contactNo\": \"9878965432\",\n" +
                        "        \"dob\": \"2019-04-28T14:45:15\",\n" +
                        "        \"accountType\": \"Savings\"\n" +
                        "\t}\n" +
                        "}");
        InvokeResult invokeResult = null;

        try {
            AWSLambda awsLambda = AWSLambdaClientBuilder.standard()
                    .withCredentials(new ProfileCredentialsProvider())
                    .withRegion(Regions.US_EAST_2).build();
            invokeResult = awsLambda.invoke(invokeRequest);

            String ans = new String(invokeResult.getPayload().array(), StandardCharsets.UTF_8);

            //write out the return value
            System.out.println(ans);

        } catch (ServiceException e) {
            System.out.println(e);
        }
    }
}

