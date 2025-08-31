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
                .companyName("CompanyOOD")
                .ownerName("John Doe")
                .companyAddress("Sofia, Bulgaria")
                .email("company@company.com")
                .phoneNumber("+34 587 3423 54")
                .password("John123")
                .build();

        userService.register(registerRequest);
    }
}
