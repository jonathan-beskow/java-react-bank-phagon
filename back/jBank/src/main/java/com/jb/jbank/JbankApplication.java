package com.jb.jbank;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@RequiredArgsConstructor
public class JbankApplication {

//    private final AuthService authService;
//    private final RoleService roleService;

    public static void main(String[] args) {
        SpringApplication.run(JbankApplication.class, args);
    }

//    @Bean
//    CommandLineRunner runner() {
//        return args -> {
//
//            Role role = new Role();
//            role.setName("ADMIN");
//            roleService.createRole(role);
//
//            RegistrationRequest user1 = RegistrationRequest.builder()
//                    .email("j.beskow@sistran.com.br")
//                    .roles(List.of("ADMIN"))
//                    .password("123")
//                    .firstName("Jonathan")
//                    .lastName("Beskow")
//                    .phoneNumber("123456789")
//                    .build();
//
//            RegistrationRequest user2 = RegistrationRequest.builder()
//                    .email("j.beskow16@gmail.com")
//                    .roles(List.of("ADMIN"))
//                    .password("123")
//                    .firstName("Jonathan")
//                    .lastName("Beskow")
//                    .phoneNumber("123456789")
//                    .build();
//
//            authService.register(user1);
//            authService.register(user2);
//
//        };
//    }

}
