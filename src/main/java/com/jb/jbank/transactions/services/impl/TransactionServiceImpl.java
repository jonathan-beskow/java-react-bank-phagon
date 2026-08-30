package com.jb.jbank.transactions.services.impl;

import com.jb.jbank.account.entity.Account;
import com.jb.jbank.account.repo.AccountRepository;
import com.jb.jbank.auth_users.entity.User;
import com.jb.jbank.auth_users.services.UserService;
import com.jb.jbank.enums.TransactionStatus;
import com.jb.jbank.enums.TransactionType;
import com.jb.jbank.exceptions.specificExceptions.InsufficientBalanceException;
import com.jb.jbank.exceptions.specificExceptions.InvalidTransactionException;
import com.jb.jbank.exceptions.specificExceptions.NotFoundException;
import com.jb.jbank.notifications.dto.NotificationDTO;
import com.jb.jbank.notifications.services.NotificationService;
import com.jb.jbank.res.Response;
import com.jb.jbank.transactions.dto.TransactionDTO;
import com.jb.jbank.transactions.dto.TransactionRequest;
import com.jb.jbank.transactions.entity.Transaction;
import com.jb.jbank.transactions.repo.TransactionRepository;
import com.jb.jbank.transactions.services.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final NotificationService notificationService;
    private final UserService userService;
    private final ModelMapper modelMapper;


    @Override
    public Response<?> createTransaction(TransactionRequest transactionRequest) {

        Transaction transaction = new Transaction();
        transaction.setTransactionType(transactionRequest.getTransactionType());
        transaction.setAmount(transactionRequest.getAmount());
        transaction.setDescription(transactionRequest.getDescription());

        switch (transactionRequest.getTransactionType()) {
            case DEPOSIT -> handleDeposit(transactionRequest, transaction);
            case WITHDRAW -> handleWithDraw(transactionRequest, transaction);
            case TRANSFER -> handleTransfer(transactionRequest, transaction);
            default -> throw new InvalidTransactionException("Invalid Transaction Type");
        }

        transaction.setStatus(TransactionStatus.SUCCESS);
        Transaction savedTran = transactionRepository.save(transaction);


        sendTransactionNotification(savedTran);

        return Response.builder()
                .statusCode(200)
                .message("Transaction finished successfully")
                .build();
    }


    @Override
    public Response<List<TransactionDTO>> getTransactionForMyAccount(String accountNumber, int page, int size) {
        return null;
    }

    private void handleDeposit(TransactionRequest request, Transaction transaction) {
        Account account = accountRepository.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new NotFoundException("Account not found"));

        account.setBalance(account.getBalance().add(request.getAmount()));
        transaction.setAccount(account);
        accountRepository.saveAndFlush(account);
    }

    private void handleWithDraw(TransactionRequest request, Transaction transaction) {

        Account account = accountRepository.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new NotFoundException("Account not found"));

        if ((account.getBalance().compareTo(request.getAmount())) < 0) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        account.setBalance(account.getBalance().subtract(request.getAmount()));
        transaction.setAccount(account);
        accountRepository.saveAndFlush(account);
    }

    private void handleTransfer(TransactionRequest request, Transaction transaction) {

        Account sourceAccount = accountRepository.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new NotFoundException("Account not found"));

        Account destinationAccount = accountRepository.findByAccountNumber(request.getDestinationAccountNumber())
                .orElseThrow(() -> new NotFoundException("Account not found"));

        if ((sourceAccount.getBalance().compareTo(request.getAmount())) < 0) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        sourceAccount.setBalance(sourceAccount.getBalance().subtract(request.getAmount()));
        accountRepository.saveAndFlush(sourceAccount);

        destinationAccount.setBalance(destinationAccount.getBalance().add(request.getAmount()));
        accountRepository.saveAndFlush(destinationAccount);

        transaction.setAccount(sourceAccount);
        transaction.setSourceAccount(sourceAccount.getAccountNumber());
        transaction.setDestinationAccount(destinationAccount.getAccountNumber());

        transactionRepository.saveAndFlush(transaction);
    }

    private void sendTransactionNotification(Transaction savedTran) {

        User user = savedTran.getAccount().getUser();
        String subject;
        String template;

        Map<String, Object> templateVariables = new HashMap<>();
        templateVariables.put("name", user.getFirstName());
        templateVariables.put("amount", savedTran.getAmount());
        templateVariables.put("accountNumber", savedTran.getAccount().getAccountNumber());
        templateVariables.put("date", savedTran.getTransactionDate());
        templateVariables.put("balance", savedTran.getAccount().getBalance());

        if (savedTran.getTransactionType() == TransactionType.DEPOSIT) {
            subject = "Credit Alert";
            template = "credit-alert";

            NotificationDTO notificationEmail = NotificationDTO
                    .builder()
                    .recipient(user.getEmail())
                    .subject(subject)
                    .templateName(template)
                    .templateVariables(templateVariables)
                    .build();
            notificationService.sendEmail(notificationEmail, user);

        } else if (savedTran.getTransactionType() == TransactionType.WITHDRAW) {
            subject = "Debit Alert";
            template = "debit-alert";

            NotificationDTO notificationEmail = NotificationDTO
                    .builder()
                    .recipient(user.getEmail())
                    .subject(subject)
                    .templateName(template)
                    .templateVariables(templateVariables)
                    .build();

            notificationService.sendEmail(notificationEmail, user);
        } else if (savedTran.getTransactionType() == TransactionType.TRANSFER) {

            subject = "Debit Alert";
            template = "debit-alert";

            NotificationDTO notificationEmail = NotificationDTO
                    .builder()
                    .recipient(user.getEmail())
                    .subject(subject)
                    .templateName(template)
                    .templateVariables(templateVariables)
                    .build();
            notificationService.sendEmail(notificationEmail, user);

            Account destinationAccount = accountRepository.findByAccountNumber(savedTran.getDestinationAccount())
                    .orElseThrow(() -> new NotFoundException("Account not found"));

            User receiver = destinationAccount.getUser();


            Map<String, Object> receiverVariables = new HashMap<>();
            receiverVariables.put("name", user.getFirstName());
            receiverVariables.put("amount", savedTran.getAmount());
            receiverVariables.put("accountNumber", savedTran.getAccount().getAccountNumber());
            receiverVariables.put("date", savedTran.getTransactionDate());
            receiverVariables.put("balance", savedTran.getAccount().getBalance());

            NotificationDTO receiverEmail = NotificationDTO
                    .builder()
                    .recipient(receiver.getEmail())
                    .subject("Credit Alert")
                    .templateName("credit-alert")
                    .templateVariables(receiverVariables)
                    .build();

            notificationService.sendEmail(receiverEmail, receiver);


        }


    }
}
