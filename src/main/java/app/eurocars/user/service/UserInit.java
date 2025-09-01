package app.eurocars.user.service;

import app.eurocars.web.dto.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class UserInit implements CommandLineRunner {


    private final UserService userService;

    public UserInit(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {

        if (!userService.getAllUsers().isEmpty()) {
            return;
        }

        RegisterRequest registerRequest = RegisterRequest.builder()
                .companyName("AdminOOD")
                .ownerName("Admin Admin")
                .companyAddress("Sofia, Bulgaria")
                .email("admin@admin.com")
                .phoneNumber("phone number")
                .password("Admin123")
                .build();

        userService.register(registerRequest);
    }
}
