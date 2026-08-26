package com.phegon.phegonbank;

import com.phegon.phegonbank.auth_users.entity.User;
import com.phegon.phegonbank.enums.NotificationType;
import com.phegon.phegonbank.notifications.dto.NotificationDTO;
import com.phegon.phegonbank.notifications.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@RequiredArgsConstructor
public class PhegonbankApplication {

    private final NotificationService notificationService;

    public static void main(String[] args) {
        SpringApplication.run(PhegonbankApplication.class, args);
    }

//    @Bean
//    CommandLineRunner runner() {
//        return args -> {
//            NotificationDTO notificationDTO = NotificationDTO.builder()
//                    .recipient("j.beskow16@gmail.com")
//                    .subject("Hello testing email")
//                    .body("Test email")
//                    .notificationType(NotificationType.EMAIL)
//                    .build();
//
//            notificationService.sendEmail(notificationDTO, new User());
//        };
//    }

}
