package app.eurocars;

import app.eurocars.cart.service.CartService;
import app.eurocars.user.model.Country;
import app.eurocars.user.model.Role;
import app.eurocars.user.model.User;
import app.eurocars.user.repository.UserRepository;
import app.eurocars.user.service.UserService;
import app.eurocars.web.dto.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ActiveProfiles("test")
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class RegisterUserITest {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartService cartService;

    @Test
    void registerUser_happyPath() {
        RegisterRequest registerRequest = RegisterRequest.builder()
                .companyName("companyName")
                .email("test@test.com")
                .password("Test123")
                .ownerName("ownerName")
                .phoneNumber("0401234567")
                .companyAddress("companyAddress")
                .build();

        User registeredUser = userService.register(registerRequest);

        assertNotNull(registeredUser);

        assertEquals(registerRequest.getEmail(), registeredUser.getEmail());
        assertEquals(registerRequest.getCompanyName(), registeredUser.getCompanyName());
        assertEquals(registerRequest.getOwnerName(), registeredUser.getOwnerName());
        assertEquals(registerRequest.getPhoneNumber(), registeredUser.getPhoneNumber());
        assertEquals(registerRequest.getCompanyAddress(), registeredUser.getCompanyAddress());
        assertTrue(passwordEncoder.matches(registerRequest.getPassword(), registeredUser.getPassword()));

        assertEquals(Country.BULGARIA, registeredUser.getCountry());
        assertTrue(registeredUser.getIsActive());

        assertTrue(registeredUser.getPricesWithVAT());
        assertTrue(registeredUser.getWholesalePrices());
        assertTrue(registeredUser.getShowWeight());
        assertTrue(registeredUser.getProductsOrder());

        assertNotNull(registeredUser.getCreatedOn());
        assertNotNull(registeredUser.getUpdatedOn());
    }

}
