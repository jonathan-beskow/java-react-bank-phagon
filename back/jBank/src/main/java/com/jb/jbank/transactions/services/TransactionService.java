package com.jb.jbank.transactions.services;

import com.jb.jbank.res.Response;
import com.jb.jbank.transactions.dto.TransactionDTO;
import com.jb.jbank.transactions.dto.TransactionRequest;

import java.util.List;

public interface TransactionService {

    Response<?> createTransaction(TransactionRequest transactionRequest);
    Response<List<TransactionDTO>> getTransactionForMyAccount(String accountNumber, int page, int size);



}
