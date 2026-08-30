package com.jb.jbank.auth_users.services.impl;

import com.jb.jbank.auth_users.dto.UpdatePasswordRequest;
import com.jb.jbank.auth_users.dto.UserDTO;
import com.jb.jbank.auth_users.entity.User;
import com.jb.jbank.auth_users.repo.UserRepository;
import com.jb.jbank.auth_users.services.UserService;
import com.jb.jbank.exceptions.specificExceptions.BadRequestException;
import com.jb.jbank.exceptions.specificExceptions.NotFoundException;
import com.jb.jbank.notifications.dto.NotificationDTO;
import com.jb.jbank.notifications.services.NotificationService;
import com.jb.jbank.res.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    private final String uploadDir = "uploads/profile-pictures/";


    @Override
    public User getCurrentLoggedInUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new NotFoundException("User is not authenticated");
        }
        String email = authentication.getName();
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User not found"));

    }

    @Override
    public Response<UserDTO> getMyProfile() {
        User user = getCurrentLoggedInUser();
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        return Response.<UserDTO>builder().statusCode(HttpStatus.OK.value()).message("User retrivied").data(userDTO).build();
    }

    @Override
    public Response<Page<UserDTO>> getAllUsers(int page, int size) {

        Page<User> users = userRepository.findAll(PageRequest.of(page, size));
        Page<UserDTO> userDTOS = users.map(user -> modelMapper.map(user, UserDTO.class));
        return Response.<Page<UserDTO>>builder().statusCode(HttpStatus.OK.value()).message("User retrivied").data(userDTOS).build();
    }

    @Override
    public Response<?> updatePassword(UpdatePasswordRequest updatePasswordRequest) {
        User user = getCurrentLoggedInUser();
        String newPassword = updatePasswordRequest.getNewPassword();
        String oldPassword = updatePasswordRequest.getOldPassword();

        if (oldPassword == null || newPassword == null) {
            throw new BadRequestException("Old and new Password required!");
        }

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BadRequestException("Password is not correct!");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        Map<String, Object> vars = new HashMap<>();
        vars.put("name", user.getFirstName());

        NotificationDTO notificationDTO = NotificationDTO.builder().recipient(user.getEmail()).subject("Password updated!").templateName("password-change").templateVariables(vars).build();

        notificationService.sendEmail(notificationDTO, user);

        return Response.builder().statusCode(HttpStatus.OK.value()).message("Password changed successfully!").build();
    }

    @Override
    public Response<?> uploadProfilePicture(MultipartFile file) {
        User user = getCurrentLoggedInUser();
        try {

            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            if (user.getProfilePictureUrl() != null && !user.getProfilePictureUrl().isEmpty()) {
                Path oldFile = Paths.get(user.getProfilePictureUrl());
                if (Files.exists(oldFile)) {
                    Files.delete(oldFile);
                }
            }
            String originalNameFIle = file.getOriginalFilename();
            String fileExtension = "";
            if (originalNameFIle != null && originalNameFIle.contains(".")) {
                fileExtension = originalNameFIle.substring(originalNameFIle.lastIndexOf("."));
            }
            String newFilename = UUID.randomUUID() + fileExtension;
            Path filePath = uploadPath.resolve(newFilename);

            Files.copy(file.getInputStream(), filePath);
            String fileUrl = uploadDir + newFilename;
            user.setProfilePictureUrl(fileUrl);
            userRepository.saveAndFlush(user);

            return Response.builder().statusCode(HttpStatus.OK.value()).message("Profile picture uploaded successfully!").build();

        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
