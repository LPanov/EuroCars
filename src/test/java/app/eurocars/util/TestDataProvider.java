package app.eurocars.util;

import app.eurocars.user.model.Role;
import app.eurocars.user.model.User;

import java.util.UUID;

public class TestDataProvider {
    public static User getRandomUser() {
        return User.builder()
                .id(UUID.randomUUID())
                .companyName("companyName")
                .ownerName("ownerName")
                .companyAddress("companyAddress")
                .phoneNumber("phoneNumber")
                .email("test@email.com")
                .password("Test123")
                .role(Role.USER)
                .pricesWithVAT(true)
                .build();
    }

    public static User getRandomAdmin() {
        return User.builder()
                .id(UUID.randomUUID())
                .companyName("companyName")
                .ownerName("ownerName")
                .companyAddress("companyAddress")
                .phoneNumber("phoneNumber")
                .email("admin@email.com")
                .password("Admin123")
                .role(Role.ADMIN)
                .build();
    }
}
