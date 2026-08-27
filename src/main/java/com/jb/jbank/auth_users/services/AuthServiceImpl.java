package com.jb.jbank.auth_users.services;

import com.jb.jbank.account.entity.Account;
import com.jb.jbank.auth_users.dto.LoginRequest;
import com.jb.jbank.auth_users.dto.LoginResponse;
import com.jb.jbank.auth_users.dto.RegistrationRequest;
import com.jb.jbank.auth_users.dto.ResetPasswordRequest;
import com.jb.jbank.auth_users.entity.User;
import com.jb.jbank.auth_users.repo.UserRepository;
import com.jb.jbank.enums.AccountType;
import com.jb.jbank.enums.Currency;
import com.jb.jbank.exceptions.specificExceptions.BadRequestException;
import com.jb.jbank.exceptions.specificExceptions.NotFoundException;
import com.jb.jbank.notifications.dto.NotificationDTO;
import com.jb.jbank.notifications.services.NotificationService;
import com.jb.jbank.res.Response;
import com.jb.jbank.role.entity.Role;
import com.jb.jbank.role.repo.RoleRepository;
import com.jb.jbank.security.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final NotificationService notificationService;
    private final AccountService accountService;

    @Override
    public Response<String> register(RegistrationRequest request) {
        List<Role> roles;

        if (request.getRoles() == null || request.getRoles().isEmpty()) {
            Role defaultRole = roleRepository.findByName("CUSTOMER").orElseThrow(() -> new NotFoundException("Customer role not found!"));
            roles = Collections.singletonList(defaultRole);
        } else {
            roles = request.getRoles().stream().map(roleName -> roleRepository.findByName(roleName).orElseThrow(() -> new NotFoundException("Role not found " + roleName))).toList();
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BadRequestException("Email is already present");
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(roles)
                .activate(true)
                .build();

        User savedUser = userRepository.saveAndFlush(user);

        //TODO AUTO GENERATE AN ACCOUNT NUMBER FOR THE USER
        Account savedAccount = accountService.createAccount(AccountType.SAVINGS, savedUser);

        //SEND WELCOME EMAIL
        Map<String, Object> vars = new HashMap<>();
        vars.put("name", user.getFirstName());

        NotificationDTO notificationDTO = NotificationDTO.builder()
                .recipient(savedUser.getEmail())
                .subject("Welcome to JBank! 😎")
                .templateName("welcome")
                .templateVariables(vars)
                .build();

        notificationService.sendEmail(notificationDTO, savedUser);

        Map<String, Object> accountVariables = new HashMap<>();
        accountVariables.put("name", user.getFirstName());
        accountVariables.put("accountNumber", savedAccount.getAccountNumber());
        accountVariables.put("accountType", AccountType.SAVINGS.name());
        accountVariables.put("currency", Currency.USD);

        NotificationDTO accountCreatedEmail = NotificationDTO.builder()
                .recipient(savedUser.getEmail())
                .subject("Your new Bank account has been created! ✔")
                .templateName("account-created")
                .templateVariables(accountVariables)
                .build();

        notificationService.sendEmail(accountCreatedEmail, savedUser);

        return Response.<String>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Your account has been created Successfully")
                .data("Email of your account has been set to you. You account number is: "+savedAccount.getAccountNumber())
                .build();
    }

    @Override
    public Response<LoginResponse> Login(LoginRequest loginRequest) {
        return null;
    }

    @Override
    public Response<?> forgetPassword(String email) {
        return null;
    }

    @Override
    public Response<?> updatePasswordViaResetCode(ResetPasswordRequest resetPasswordRequest) {
        return null;
    }


}
