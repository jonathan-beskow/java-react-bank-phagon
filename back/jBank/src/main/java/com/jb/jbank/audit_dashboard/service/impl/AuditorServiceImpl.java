package com.jb.jbank.audit_dashboard.service.impl;

import com.jb.jbank.account.dto.AccountDTO;
import com.jb.jbank.account.repo.AccountRepository;
import com.jb.jbank.audit_dashboard.service.AuditorService;
import com.jb.jbank.auth_users.dto.UserDTO;
import com.jb.jbank.auth_users.repo.UserRepository;
import com.jb.jbank.transactions.dto.TransactionDTO;
import com.jb.jbank.transactions.repo.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuditorServiceImpl implements AuditorService {


    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final ModelMapper modelMapper;


    @Override
    public Map<String, Long> getSystemTotals() {

        long totalUsers = userRepository.count();
        long totalAccounts = accountRepository.count();
        long totalTransactions = transactionRepository.count();

        return Map.of(
                "totalUsers", totalUsers,
                "totalAccounts", totalAccounts,
                "totalTransactions", totalTransactions
        );
    }

    @Override
    public Optional<UserDTO> findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(user -> modelMapper.map(user, UserDTO.class));
    }

    @Override
    public Optional<AccountDTO> findAccountDetailsByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .map(account -> modelMapper.map(account, AccountDTO.class));
    }

    @Override
    public List<TransactionDTO> findTransactionsByAccountNumber(String accountNumber) {
        return transactionRepository.findByAccount_AccountNumber(accountNumber)
                .stream().map(
                        transaction -> modelMapper.map(transaction, TransactionDTO.class)
                ).toList();
    }

    @Override
    public Optional<TransactionDTO> findTransactionById(Long transactionId) {
        return transactionRepository.findById(transactionId)
                .map(tran -> modelMapper.map(tran, TransactionDTO.class));
    }
}
