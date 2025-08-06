package com.murphy.simplebank.service.impl;

import com.murphy.simplebank.config.JwtTokenProvider;
import com.murphy.simplebank.dto.*;
import com.murphy.simplebank.entity.Role;
import com.murphy.simplebank.entity.User;
import com.murphy.simplebank.repository.UserRepository;
import com.murphy.simplebank.utils.AccountUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{
   @Autowired
    UserRepository userRepository;
   @Autowired
    EmailService emailService;
   @Autowired
   TransactionService transactionService;
   @Autowired
    PasswordEncoder passwordEncoder;
   @Autowired
    AuthenticationManager authenticationManager;
   @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Override
    public BankResponse createAccount(UserRequest userRequest) {
//        Creating an account - saving a new user into the db
//        check if user already have account
        if (userRepository.existsByEmail(userRequest.getEmail())){
           return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_EXITS_MESSAGE)
                    .accountInfo(null)
                    .build();

        }
        User newUser = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .otherName(userRequest.getOtherName())
                .gender(userRequest.getGender())
                .address(userRequest.getAddress())
                .stateOfOrigin(userRequest.getStateOfOrigin())
                .accountNumber(AccountUtils.generateAccountNumber())
                .accountBalance(BigDecimal.ZERO)
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .phoneNumber(userRequest.getPhoneNumber())
                .alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
                .status("ACTIVE")
                .role(Role.valueOf("ROLE_ADMIN"))
                .build();
        User savedUser = userRepository.save(newUser);
        //Send email Alert
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(savedUser.getEmail())
                .subject("ACCOUNT CREATION")
                .messageBody("Congratulations Your Account Has been Successfully Created.\n Your account Detail:\n" +
                        "Account Name :"+savedUser.getFirstName()+" "+savedUser.getLastName()+" "+savedUser.getOtherName()+"\nAccount Number: "+savedUser.getAccountNumber())
                .build();
        emailService.sendEmailAlert(emailDetails);

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATION_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(savedUser.getAccountBalance())
                        .accountNumber(savedUser.getAccountNumber())
                        .accountName(savedUser.getFirstName()+" "+savedUser.getLastName()+" "+savedUser.getOtherName())
                        .build())
                .build();
    }
    //balance Enquiry, name Enquiry, credit,debit,transfer ,interest
    public BankResponse login(LoginDto loginDto){
        Authentication authentication = null;
        authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(),loginDto.getPassword())
        );
        EmailDetails loginAlert = EmailDetails.builder()
                .subject("You're logged in!")
                .recipient(loginDto.getEmail())
                .messageBody("You logged into your account. If you did not initiate this request, please contact your bank")
                .build();
        emailService.sendEmailAlert(loginAlert);
        return BankResponse.builder()
                .responseCode("Login Success")
                .responseMessage(jwtTokenProvider.generateToken(authentication))
                .build();
    }

    @Override
    public BankResponse balanceEnquiry(EnquiryRequest request) {
       //check if the provided account number exists in the db
        boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if(!isAccountExist){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
            }
        User foundUser = userRepository.findByAccountNumber(request.getAccountNumber());
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
                .responseMessage(AccountUtils.ACCOUNT_FOUND_SUCCESS)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(foundUser.getAccountBalance())
                        .accountNumber(request.getAccountNumber())
                        .accountName(foundUser.getFirstName()+" "+foundUser.getLastName()+" "+foundUser.getOtherName())
                        .build())

                .build();
    }

    @Override
    public String nameEnquiry(EnquiryRequest request) {
        boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if(!isAccountExist) {
            return AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE;
        }
    User foundUser = userRepository.findByAccountNumber(request.getAccountNumber());
    return foundUser.getFirstName()+" "+foundUser.getLastName()+" "+foundUser.getOtherName();
    }

    @Override
    public BankResponse creditAccount(CreditDebitRequest request) {
        boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if(!isAccountExist){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User userToCredit = userRepository.findByAccountNumber(request.getAccountNumber());
        userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(request.getAmount()));
        userRepository.save(userToCredit);
        //Save transaction
        TransactionDto transactionDto = TransactionDto.builder()
                .accountNumber(userToCredit.getAccountNumber())
                .transactionType("CREDIT")
                .amount(request.getAmount())

                .build();
        transactionService.saveTransaction(transactionDto);
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREDIT_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNT_CREDIT_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(userToCredit.getFirstName()+" "+userToCredit.getLastName()+" "+userToCredit.getOtherName())
                        .accountBalance(userToCredit.getAccountBalance())
                        .accountNumber(request.getAccountNumber())
                        .build())
                .build();
    }

    @Override
    public BankResponse debitAccount(CreditDebitRequest request) {
//        check withdraw amount is sufficient
        boolean isAccountExist = userRepository.existsByAccountNumber(request.getAccountNumber());
        if(!isAccountExist){
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User userToDebit = userRepository.findByAccountNumber(request.getAccountNumber());
        if (userToDebit.getAccountBalance().compareTo(BigDecimal.ZERO)<=0 &&
                userToDebit.getAccountBalance().compareTo(request.getAmount())<=0){
            return BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        else {
            userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(request.getAmount()));
            userRepository.save(userToDebit);
            TransactionDto transactionDto = TransactionDto.builder()
                    .accountNumber(userToDebit.getAccountNumber())
                    .transactionType("DEBIT")
                    .amount(request.getAmount())

                    .build();
            transactionService.saveTransaction(transactionDto);
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_DEBITED_SUCCESS)
                    .responseMessage(AccountUtils.ACCOUNT_DEBITED_MESSAGE)
                    .accountInfo(AccountInfo.builder()
                            .accountName(userToDebit.getFirstName()+" "+userToDebit.getLastName()+" "+userToDebit.getOtherName())
                            .accountBalance(userToDebit.getAccountBalance())
                            .accountNumber(request.getAccountNumber())
                            .build())
                    .build();
        }
    }

    @Override
    public BankResponse transfer(TransferRequest request) {
//       get the account to debit (check if it exists)
//       check if the amount debiting is not more than the current balance
//       debit the amount
//       get the account and credit it
        boolean isDestinationAccountExist = userRepository.existsByAccountNumber(request.getDestinationAccountNumber());
        if(!isDestinationAccountExist) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User sourceAccountUser = userRepository.findByAccountNumber(request.getSourceAccountNumber());
         if (request.getAmount().compareTo(sourceAccountUser.getAccountBalance())>0){
             return BankResponse.builder()
                     .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                     .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                     .accountInfo(null)
                     .build();
         }
         sourceAccountUser.setAccountBalance(sourceAccountUser.getAccountBalance().subtract(request.getAmount()));
         String sourceUsername = sourceAccountUser.getFirstName()+" "+sourceAccountUser.getLastName()+" "+sourceAccountUser.getOtherName();
         userRepository.save(sourceAccountUser);
         EmailDetails debitAlert = EmailDetails.builder()
                 .subject("DEBIT ALERT")
                 .recipient(sourceAccountUser.getEmail())
                 .messageBody("The sum of "+request.getAmount()+" has been deducted from your account! Your current balance is "+sourceAccountUser.getAccountBalance())
                 .build();
         emailService.sendEmailAlert(debitAlert);
         User destinationAccountUser = userRepository.findByAccountNumber(request.getDestinationAccountNumber());
         destinationAccountUser.setAccountBalance(destinationAccountUser.getAccountBalance().add(request.getAmount()));
//         String recipientUsername = destinationAccountUser.getFirstname()+" "+destinationAccountUser.getLastname()+" "+destinationAccountUser.getOtherName();
         userRepository.save(destinationAccountUser);
         EmailDetails creditAlert = EmailDetails.builder()
                .subject("CREDIT ALERT")
                .recipient(sourceAccountUser.getEmail())
                .messageBody("The sum of "+request.getAmount()+" has been sent to your account from " + sourceUsername +" Your current balance is "+destinationAccountUser.getAccountBalance())
                .build();
        emailService.sendEmailAlert(creditAlert);
        TransactionDto transactionDto = TransactionDto.builder()
                .accountNumber(destinationAccountUser.getAccountNumber())
                .transactionType("CREDIT")
                .amount(request.getAmount())

                .build();
        transactionService.saveTransaction(transactionDto);
         return BankResponse.builder()
                 .responseCode(AccountUtils.TRANSFER_SUCCESSFUL_CODE)
                 .responseMessage(AccountUtils.TRANSFER_SUCCESSFUL_MESSAGE)
                 .accountInfo(null)
                 .build();


    }


}
