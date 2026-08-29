package com.jb.jbank.auth_users.services;

import com.jb.jbank.auth_users.dto.UpdatePasswordRequest;
import com.jb.jbank.auth_users.dto.UserDTO;
import com.jb.jbank.auth_users.entity.User;
import com.jb.jbank.res.Response;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    User getCurrentLoggedInUser();

    Response<UserDTO> getMyProfile();

    Response<Page<UserDTO>> getAllUsers(int page, int size);

    Response<?> updatePassword(UpdatePasswordRequest updatePasswordRequest);

    Response<?> uploadProfilePicture(MultipartFile file);


}
