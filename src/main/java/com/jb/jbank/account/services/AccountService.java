package com.jb.jbank.account.services;

import com.jb.jbank.account.dto.AccountDTO;
import com.jb.jbank.account.entity.Account;
import com.jb.jbank.auth_users.entity.User;
import com.jb.jbank.enums.AccountType;
import com.jb.jbank.res.Response;

import java.util.List;

public interface AccountService {

    Account createAccount(AccountType accountType, User user);

    Response<List<AccountDTO>> getMyAccounts();

    Response<?> closeAccount(String accountNumber);


}
