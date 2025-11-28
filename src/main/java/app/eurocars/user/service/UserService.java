package app.eurocars.user.service;

import app.eurocars.cart.service.CartService;
import app.eurocars.exception.DomainException;
import app.eurocars.exception.EmailAlreadyExistException;
import app.eurocars.exception.NotMatchingPasswords;
import app.eurocars.security.AuthenticationDetails;
import app.eurocars.user.model.Country;
import app.eurocars.user.model.Role;
import app.eurocars.user.model.User;
import app.eurocars.user.repository.UserRepository;
import app.eurocars.web.dto.*;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;
    private final CartService cartService;


    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, ApplicationEventPublisher eventPublisher, CartService cartService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.eventPublisher = eventPublisher;
        this.cartService = cartService;
    }

    @Transactional
    public User register(RegisterRequest registerRequest) {
        Optional<User> userOptional = userRepository.findByEmail(registerRequest.getEmail());

        if (userOptional.isPresent()) {
            throw new EmailAlreadyExistException("Email '%s' already exists".formatted(registerRequest.getEmail()));
        }

        User user = User.builder()
                .companyName(registerRequest.getCompanyName())
                .ownerName(registerRequest.getOwnerName())
                .companyAddress(registerRequest.getCompanyAddress())
                .email(registerRequest.getEmail())
                .phoneNumber(registerRequest.getPhoneNumber())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(getAllUsers().isEmpty() ? Role.ADMIN : Role.USER)
                .isActive(true)
                .country(Country.BULGARIA)
                .pricesWithVAT(true)
                .wholesalePrices(true)
                .productsOrder(true)
                .showWeight(true)
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();

        userRepository.save(user);
        cartService.createCart(user.getId());
        log.info("Successfully created new user with email: '%s'".formatted(user.getEmail()));

        return user;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getAllNonAdminUsers() {
        return getAllUsers().stream().filter(u -> !u.getRole().equals(Role.ADMIN)).toList();
    }

    public User getById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new DomainException("User with such ID:'%s' does not exist.".formatted(id)));
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new DomainException("User with this email does not exist"));

        return new AuthenticationDetails(user.getId(), user.getEmail(), user.getPassword(), user.getRole(), user.getIsActive());
    }

    @Transactional
    public User changePassword(ChangePasswordRequest changePasswordRequest) {

        if (!changePasswordRequest.getPassword().equals(changePasswordRequest.getConfirmPassword())) {
            throw new NotMatchingPasswords("Passwords do not match");
        }

        User user = userRepository.findByEmail(changePasswordRequest.getEmail()).orElseThrow(() -> new DomainException("Profile with such email does not exist"));

        user.setPassword(passwordEncoder.encode(changePasswordRequest.getPassword()));
        user.setUpdatedOn(LocalDateTime.now());
        log.info("Successfully changed password for profile with email: '%s'".formatted(user.getEmail()));

        ChangedPasswordEvent event = ChangedPasswordEvent.builder()
                .email(user.getEmail())
                .owner(user.getOwnerName())
                .changeDate(LocalDateTime.now())
                .build();

        eventPublisher.publishEvent(event);
        updateUser(user);

        return user;
    }

    public void deleteUserById(UUID id) {
        userRepository.deleteById(id);
    }

    public void editUser(EditUserRequest editUserRequest, UUID editUserId) {
        User editUser = getById(editUserId);

        editUser.setOwnerName(editUserRequest.getOwnerName());
        editUser.setCompanyName(editUserRequest.getCompanyName());
        editUser.setCompanyAddress(editUserRequest.getCompanyAddress());
        editUser.setRole(editUserRequest.getRole());
        editUser.setPhoneNumber(editUserRequest.getPhoneNumber());
        editUser.setEmail(editUserRequest.getEmail());
        editUser.setCountry(editUserRequest.getCountry());
        editUser.setPassword(passwordEncoder.encode(editUserRequest.getPassword()));
        editUser.setCity(editUserRequest.getCity());
        editUser.setZipCode(editUserRequest.getZipCode());
        editUser.setState(editUserRequest.getState());
        editUser.setUpdatedOn(LocalDateTime.now());

        updateUser(editUser);
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }

    public void updateUserProfile(UpdateProfileRequest updateProfileRequest, UUID userId) {
        User user = getById(userId);

        user.setOwnerName(updateProfileRequest.getOwnerName());
        user.setEmail(updateProfileRequest.getEmail());
        user.setCompanyAddress(updateProfileRequest.getCompanyAddress());
        if (updateProfileRequest.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(updateProfileRequest.getPassword()));
        }

        user.setPricesWithVAT(updateProfileRequest.getPricesWithVAT());
        user.setWholesalePrices(updateProfileRequest.getWholesalePrices());
        user.setProductsOrder(updateProfileRequest.getProductsOrder());
        user.setShowWeight(updateProfileRequest.getShowWeight());
        user.setUpdatedOn(LocalDateTime.now());

        updateUser(user);
    }
}
