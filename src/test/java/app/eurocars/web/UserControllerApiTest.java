package app.eurocars.web;

import app.eurocars.cart.service.CartService;
import app.eurocars.security.AuthenticationDetails;
import app.eurocars.user.model.User;
import app.eurocars.user.service.UserService;
import app.eurocars.web.dto.EditUserRequest;
import app.eurocars.web.dto.UpdateProfileRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static app.eurocars.util.TestDataProvider.getRandomAdmin;
import static app.eurocars.util.TestDataProvider.getRandomUser;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerApiTest {
    @MockitoBean
    private UserService userService;
    @MockitoBean
    private CartService cartService;
    @MockitoBean
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @Autowired
    private MockMvc mockMvc;

    private User admin;
    private AuthenticationDetails principal;

    @BeforeEach
    void setUp() {
        admin = getRandomAdmin();
        principal = new AuthenticationDetails(admin.getId(), admin.getEmail(), admin.getPassword(), admin.getRole(), true);
        when(userService.getById(any())).thenReturn(admin);
        when(cartService.getCartItemsByUserId(admin.getId())).thenReturn(new ArrayList<>());
        when(cartService.getWholePrice(any())).thenReturn(BigDecimal.ZERO);
    }

    @Test
    void getUsersPage_ShouldReturnUsersViewWithNonAdminList() throws Exception {
        User user = getRandomUser();
        List<User> mockUsers = Arrays.asList(user, admin);

        when(userService.getAllNonAdminUsers()).thenReturn(List.of(user));

        MockHttpServletRequestBuilder request = get("/users")
                .with(user(principal));

        mockMvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(view().name("users"))
            .andExpect(model().attributeExists("users"))
            .andExpect(model().attribute("users", hasSize(1)));
    }

    @Test
    void deleteUser_ShouldCallServiceAndDeleteAndRedirect() throws Exception {
        UUID userId = UUID.randomUUID();

        doNothing().when(userService).deleteUserById(userId);

        MockHttpServletRequestBuilder request = delete("/users")
                .with(user(principal))
                .param("userId", userId.toString())
                .with(csrf());


        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/users"));

        verify(userService, times(1)).deleteUserById(userId);
    }

    @Test
    void getEditUserPage_returnEditUserViewWithUser() throws Exception {
        User user = getRandomUser();

        when(userService.getById(any())).thenReturn(user);

        MockHttpServletRequestBuilder request = get("/users/{id}", user.getId())
                .with(user(principal));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("edit-user"))
                .andExpect(model().attributeExists("editUserRequest"));
    }

    @Test
    void editUser_happyPath() throws Exception {
        UUID targetUserId = UUID.randomUUID();

        doNothing().when(userService).editUser(new EditUserRequest(), targetUserId);

        MockHttpServletRequestBuilder request = put("/users/{id}", targetUserId )
                .with(user(principal))
                .param("companyName", "companyName")
                .param("ownerName", "ownerName")
                .param("companyAddress", "companyAddress")
                .param("email", "new@email.com")
                .param("phoneNumber", "phoneNumber")
                .param("password", "NewPass123")
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("edit-user"))
                .andExpect(model().attributeExists("editUserRequest"));

    }

    @Test
    void editUser_withInvalidEditUserRequest_returnEditUserViewWithEmptyRequest() throws Exception {
        UUID targetUserId = UUID.randomUUID();

        MockHttpServletRequestBuilder request = put("/users/{id}", targetUserId )
                .with(user(principal))
                .param("companyName", "companyName")
                .param("ownerName", "ownerName")
                .param("companyAddress", "companyAddress")
                .param("email", "new@email.com")
                .param("phoneNumber", "phoneNumber")
                .param("password", "InvalidPass")
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("edit-user"))
                .andExpect(model().attributeExists("editUserRequest"));

        verify(userService, never()).editUser(any(), any());
    }

    @Test
    void getAccountSettingsPage_returnAccountSettingsViewWithUserAndUpdateProfileRequestObjects() throws Exception {
        User user = getRandomUser();

        when(userService.getById(any())).thenReturn(user);

        MockHttpServletRequestBuilder request = get("/users/{id}/account-settings", user.getId())
                .with(user(principal));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("account-settings"))
                .andExpect(model().attributeExists("updateProfileRequest", "user"));
    }

    @Test
    void updateUserProfile_happyPath() throws Exception {
        UUID targetUserId = UUID.randomUUID();

        doNothing().when(userService).updateUserProfile(new UpdateProfileRequest(), targetUserId);

        MockHttpServletRequestBuilder request = patch("/users/{id}/account-settings", targetUserId )
                .with(user(principal))
                .param("ownerName", "ownerName")
                .param("companyAddress", "companyAddress")
                .param("email", "new@email.com")
                .param("password", "NewPass123")
                .param("pricesWithVAT", "true")
                .param("wholesalePrices", "true")
                .param("showWeight", "true")
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/" + targetUserId + "/account-settings"));
    }

    @Test
    void updateUserProfile_whenDtoHasInvalidField_returnAccountSettingsPageWithUnchangedValues() throws Exception {
        UUID targetUserId = UUID.randomUUID();

        doNothing().when(userService).updateUserProfile(new UpdateProfileRequest(), targetUserId);

        MockHttpServletRequestBuilder request = patch("/users/{id}/account-settings", targetUserId )
                .with(user(principal))
                .param("ownerName", "ownerName")
                .param("companyAddress", "companyAddress")
                .param("email", "new@email.com")
                .param("password", "InvalidPass")
                .param("pricesWithVAT", "true")
                .param("wholesalePrices", "true")
                .param("showWeight", "true")
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("account-settings"))
                .andExpect(model().attributeExists("updateProfileRequest", "user"));
    }
}
