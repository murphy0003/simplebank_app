package com.murphy.simplebank.utils;

import java.time.Year;

public class AccountUtils {
    public static final String ACCOUNT_EXISTS_CODE = "001";

    public static final String ACCOUNT_EXITS_MESSAGE = "This user already have an account";

    public static final String ACCOUNT_CREATION_CODE = "OO2";

    public static final String ACCOUNT_CREATION_MESSAGE = "Account have been successfully created!";

    public static final String ACCOUNT_NOT_EXIST_CODE = "003";

    public static final String ACCOUNT_NOT_EXIST_MESSAGE = "User with the provided Account Number does  not exist";

    public static final String ACCOUNT_FOUND_CODE = "004";

    public static final String ACCOUNT_FOUND_SUCCESS = "User Account Found";

    public static final String ACCOUNT_CREDIT_SUCCESS = "005";

    public static final String ACCOUNT_CREDIT_SUCCESS_MESSAGE = "User account is credit successfully ";

    public static final String INSUFFICIENT_BALANCE_CODE = "006";

    public static final String INSUFFICIENT_BALANCE_MESSAGE = "Insufficient balance!";

    public static final String ACCOUNT_DEBITED_SUCCESS = "007";

    public static final String ACCOUNT_DEBITED_MESSAGE = "Account has been successfully debited";

    public static final String TRANSFER_SUCCESSFUL_CODE = "008";

    public static final String TRANSFER_SUCCESSFUL_MESSAGE ="Transfer successful";

    public static String generateAccountNumber(){
        // 2025 + randomSixDigits
        Year currentYear = Year.now();
        int min = 100000;
        int max = 999999;

// generate a random number between min and max
        int randomNumber =(int)Math.floor(Math.random()*(max-min+1)+min);
//convert the current year and randomNumber to string, then concatenated
        String year = String.valueOf(currentYear);
        String randomStringNumber = String.valueOf(randomNumber);
        StringBuilder accountNumber = new StringBuilder();
        return accountNumber.append(year).append(randomStringNumber).toString();

    }
}
