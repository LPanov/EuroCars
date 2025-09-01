package app.eurocars.user.service;

import app.eurocars.cart.model.Cart;
import app.eurocars.exception.DomainException;
import app.eurocars.security.AuthenticationDetails;
import app.eurocars.user.model.Country;
import app.eurocars.user.model.Role;
import app.eurocars.user.model.User;
import app.eurocars.user.repository.UserRepository;
import app.eurocars.web.dto.RegisterRequest;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
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
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;


    public UserService(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

//    @CacheEvict(value = "users", allEntries = true)
    @Transactional
    public User register(RegisterRequest registerRequest) {
        Optional<User> userOptional = userRepository.findByEmail(registerRequest.getEmail());

        if (userOptional.isPresent()) {
            throw new DomainException("Email '%s' already exists".formatted(registerRequest.getEmail()));
        }

        User user = modelMapper.map(registerRequest, User.class);
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCart(new Cart());
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

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new DomainException("User with such ID:'%s' does not exist.".formatted(id)));
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new DomainException("User with this email does not exist"));

        return new AuthenticationDetails(user.getId(), user.getEmail(), user.getPassword(), user.getRole(), user.getIsActive());
    }
}
