package app.eurocars.user.service;

import app.eurocars.exception.DomainException;
import app.eurocars.user.model.Country;
import app.eurocars.user.model.Role;
import app.eurocars.user.model.User;
import app.eurocars.user.repository.UserRepository;
import app.eurocars.web.dto.LoginRequest;
import app.eurocars.web.dto.RegisterRequest;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;


    public UserService(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User register(RegisterRequest registerRequest) {
        Optional<User> userOptional = userRepository.findByEmail(registerRequest.getEmail());

        if (userOptional.isPresent()) {
            throw new DomainException("Email '%s' already exists".formatted(registerRequest.getEmail()));
        }

        User user = modelMapper.map(registerRequest, User.class);
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(Role.USER);
        user.setIsActive(true);
        user.setCountry(Country.BULGARIA);
        LocalDateTime now = LocalDateTime.now();
        user.setCreatedOn(now);
        user.setUpdatedOn(now);
        user.setPricesWithVAT(true);
        user.setWholesalePrices(true);
        user.setProductsOrder(true);
        user.setShowWeight(true);

        userRepository.save(user);
        log.info("Successfully created new user with email: '%s'".formatted(user.getEmail()));

        return user;
    }

    public User login(LoginRequest loginRequest) {

        Optional<User> optionalUser = userRepository.findByEmail(loginRequest.getEmail());

        if (optionalUser.isEmpty()) {
            throw new DomainException("Email or Password are incorrect");
        }

        User user = optionalUser.get();
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new DomainException("Email or Password are incorrect");
        }

        return user;
    }
}
