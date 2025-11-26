package app.eurocars.user.service;

import app.eurocars.cart.service.CartService;
import app.eurocars.exception.DomainException;
import app.eurocars.exception.EmailAlreadyExistException;
import app.eurocars.exception.NotMatchingPasswords;
import app.eurocars.part.model.Part;
import app.eurocars.security.AuthenticationDetails;
import app.eurocars.user.model.Country;
import app.eurocars.user.model.Role;
import app.eurocars.user.model.User;
import app.eurocars.user.repository.UserRepository;
import app.eurocars.web.dto.ChangePasswordRequest;
import app.eurocars.web.dto.ChangedPasswordEvent;
import app.eurocars.web.dto.EditUserRequest;
import app.eurocars.web.dto.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceUTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private ApplicationEventPublisher eventPublisher;
    @Mock
    private CartService cartService;

    @InjectMocks
    private UserService userService;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Captor
    private ArgumentCaptor<ChangedPasswordEvent> eventArgumentCaptor;

    @Test
    void givenRegisterRequestWithEmailThatAlreadyExist_whenRegister_thenThrowException() {
        RegisterRequest registerRequest = RegisterRequest.builder()
                .email("test@test.com")
                .build();

        when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.of(new User()));

        assertThrows(EmailAlreadyExistException.class, () -> userService.register(registerRequest));

        verify(userRepository).findByEmail(anyString());
    }

    @Test
    void whenGetAllNonAdminUsers_thenReturnListOfUsersWithRoleUSER() {
        User targetedUser = User.builder().role(Role.USER).build();
        User nonTargetedUser = User.builder().role(Role.ADMIN).build();
        List<User> users = List.of(targetedUser, nonTargetedUser);

        when(userRepository.findAll()).thenReturn(users);

        List<User> filteredUsers = userService.getAllNonAdminUsers();

        assertEquals(1, filteredUsers.size());
        assertEquals(targetedUser, filteredUsers.get(0));

        verify(userRepository).findAll();
    }

    @Test
    void givenValidChangePasswordRequest_whenChangePassword_thenRewritePasswordOfTheFoundUser() {
        User targetedUser = User.builder()
                .ownerName("ownerName")
                .email("test@test.com")
                .password("<PASSWORD>")
                .build();

        ChangePasswordRequest changePasswordRequest = ChangePasswordRequest.builder()
                .email("test@test.com")
                .password("newPassword")
                .confirmPassword("newPassword")
                .build();

        when(userRepository.findByEmail(targetedUser.getEmail())).thenReturn(Optional.of(targetedUser));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        userService.changePassword(changePasswordRequest);

        verify(userRepository, times(1)).save(userArgumentCaptor.capture());
        User updatedUser = userArgumentCaptor.getValue();

        assertEquals("encodedPassword", updatedUser.getPassword());
        assertEquals(targetedUser.getEmail(), updatedUser.getEmail());

        assertNotNull(updatedUser.getUpdatedOn());

        verify(passwordEncoder).encode(anyString());
        verify(userRepository).findByEmail(anyString());

        verify(eventPublisher).publishEvent(eventArgumentCaptor.capture());
        ChangedPasswordEvent event = eventArgumentCaptor.getValue();

        assertEquals(targetedUser.getEmail(), event.getEmail());
        assertEquals(targetedUser.getOwnerName(), event.getOwner());
        assertNotNull(event.getChangeDate());
    }

    @Test
    void whenChangePasswordIsWithUnmatchingPasswords_thenThrowException() {
        ChangePasswordRequest changePasswordRequest = ChangePasswordRequest.builder()
                .email("test@test.com")
                .password("newPassword")
                .confirmPassword("NotMatchingPassword")
                .build();

        assertThrows(NotMatchingPasswords.class, () -> userService.changePassword(changePasswordRequest));
    }

    @Test
    void whenChangePasswordToNonExistingProfile_thenThrowException() {
        ChangePasswordRequest changePasswordRequest = ChangePasswordRequest.builder()
                .email("non@existing.email")
                .password("newPassword")
                .confirmPassword("newPassword")
                .build();

        when(userRepository.findByEmail(changePasswordRequest.getEmail())).thenReturn(Optional.empty());

        assertThrows(DomainException.class, () -> userService.changePassword(changePasswordRequest));
    }

    @Test
    public void deleteUserById_ShouldCallRepositoryDeleteWithCorrectId() {
        UUID userId = UUID.randomUUID();

        userService.deleteUserById(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void givenValidEditUserRequest_whenEditUser_thenUpdateCorrespondingUser() {
        UUID editUserId = UUID.randomUUID();

        User targetedUser = User.builder()
                .id(editUserId)
                .build();

        EditUserRequest editUserRequest = EditUserRequest.builder()
                .ownerName("newOwnerName")
                .email("new@email.com")
                .phoneNumber("0000000000")
                .companyAddress("newCompanyAddress")
                .companyName("newCompanyName")
                .role(Role.USER)
                .country(Country.ROMANIA)
                .password("<PASSWORD>")
                .city("newCity")
                .zipCode("newZipCode")
                .state("newState")
                .build();

        when(userRepository.findById(editUserId)).thenReturn(Optional.of(targetedUser));
        when(passwordEncoder.encode(editUserRequest.getPassword())).thenReturn("encodedPassword");

        userService.editUser(editUserRequest, editUserId);

        verify(userRepository, times(1)).save(userArgumentCaptor.capture());
        User updatedUser = userArgumentCaptor.getValue();

        assertEquals(editUserRequest.getOwnerName(), updatedUser.getOwnerName());
        assertEquals(editUserRequest.getEmail(), updatedUser.getEmail());
        assertEquals(editUserRequest.getPhoneNumber(), updatedUser.getPhoneNumber());
        assertEquals(editUserRequest.getCompanyAddress(), updatedUser.getCompanyAddress());
        assertEquals(editUserRequest.getCompanyName(), updatedUser.getCompanyName());
        assertEquals("encodedPassword", updatedUser.getPassword());
        assertEquals(editUserRequest.getRole(), updatedUser.getRole());

        assertEquals(editUserRequest.getCountry(), updatedUser.getCountry());
        assertEquals(editUserRequest.getCity(), updatedUser.getCity());
        assertEquals(editUserRequest.getZipCode(), updatedUser.getZipCode());
        assertEquals(editUserRequest.getState(), updatedUser.getState());

        assertNotNull(updatedUser.getUpdatedOn());

        verify(passwordEncoder).encode(anyString());
        verify(userRepository).findById(any());
    }

    @Test
    void whenLoadUserByUsername_UserFound_ReturnsUserDetails() {
        User targetedUser = User.builder()
                .ownerName("ownerName")
                .email("test@test.com")
                .password("<PASSWORD>")
                .role(Role.USER)
                .isActive(true)
                .build();

        when(userRepository.findByEmail(targetedUser.getEmail())).thenReturn(Optional.of(targetedUser));

        UserDetails userDetails = userService.loadUserByUsername(targetedUser.getEmail());

        verify(userRepository, times(1)).findByEmail(targetedUser.getEmail());

        assertNotNull(userDetails);
        assertEquals(targetedUser.getEmail(), userDetails.getUsername());
        assertEquals(targetedUser.getPassword(), userDetails.getPassword());

    }

    @Test
    void whenLoadUserByUsername_UserNotFound_ThrowException() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

        assertThrows(DomainException.class, () -> userService.loadUserByUsername(anyString()));
    }

}
