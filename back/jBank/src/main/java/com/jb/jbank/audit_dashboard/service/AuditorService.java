package com.jb.jbank.audit_dashboard.service;

import com.jb.jbank.account.dto.AccountDTO;
import com.jb.jbank.auth_users.dto.UserDTO;
import com.jb.jbank.transactions.dto.TransactionDTO;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AuditorService {

    Map<String, Long> getSystemTotals();

    Optional<UserDTO> findUserByEmail(String email);

    Optional<AccountDTO> findAccountDetailsByAccountNumber(String accountNumber);

    List<TransactionDTO> findTransactionsByAccountNumber(String accountNumber);

    Optional<TransactionDTO> findTransactionById(Long transactionId);


}
