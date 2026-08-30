package com.jb.jbank.account.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.jb.jbank.auth_users.dto.UserDTO;
import com.jb.jbank.enums.AccountStatus;
import com.jb.jbank.enums.AccountType;
import com.jb.jbank.enums.Currency;
import com.jb.jbank.transactions.dto.TransactionDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountDTO {


    private Long id;


    private String accountNumber;

    private BigDecimal balance;


    private AccountType accountType;

    @JsonBackReference
    private UserDTO user;


    private Currency currency;
    private AccountStatus status;

    @JsonManagedReference
    private List<TransactionDTO> transactions;

    private LocalDateTime closedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
