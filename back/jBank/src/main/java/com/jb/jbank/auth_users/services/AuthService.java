package com.jb.jbank.auth_users.services;

import com.jb.jbank.auth_users.dto.LoginRequest;
import com.jb.jbank.auth_users.dto.LoginResponse;
import com.jb.jbank.auth_users.dto.RegistrationRequest;
import com.jb.jbank.auth_users.dto.ResetPasswordRequest;
import com.jb.jbank.res.Response;

public interface AuthService {

    Response<String> register(RegistrationRequest request);

    Response<LoginResponse> login(LoginRequest loginRequest);

    Response<?> forgetPassword(String email);

    Response<?> updatePasswordViaResetCode(ResetPasswordRequest resetPasswordRequest);


}
