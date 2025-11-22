package app.eurocars.web;

import app.eurocars.cart.service.CartService;
import app.eurocars.exception.EmailAlreadyExistException;
import app.eurocars.exception.NotMatchingPasswords;
import app.eurocars.security.AuthenticationDetails;
import app.eurocars.user.model.Role;
import app.eurocars.user.model.User;
import app.eurocars.user.service.UserService;
import app.eurocars.web.dto.ChangePasswordRequest;
import app.eurocars.web.dto.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(IndexController.class)
public class IndexControllerApiTest {
    @MockitoBean
    private UserService userService;
    @MockitoBean
    private CartService cartService;
    @MockitoBean
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @Autowired
    private MockMvc mockMvc;

    @Captor
    private ArgumentCaptor<RegisterRequest> registerRequestCaptor;

    @Test
    void getRequestToIndexEndpoint_shouldReturnLoginView() throws Exception {

        MockHttpServletRequestBuilder request = get("/");

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/login"));
    }

    @Test
    void getRequestToLoginEndpoint_shouldReturnLoginView() throws Exception {
        MockHttpServletRequestBuilder request = get("/login");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void getRequestToLoginEndpointWithErrorParameter_shouldReturnLoginViewAndErrorMessageAttribute() throws Exception {
        MockHttpServletRequestBuilder request = get("/login").param("error", "");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("errorMessage"));
    }

    @Test
    void getRequestToRegisterEndpoint_shouldReturnRegisterView() throws Exception {
        MockHttpServletRequestBuilder request = get("/register");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("registerRequest"));
    }

    @Test
    void getRequestToForgottenPasswordEndpoint_shouldReturnForgottenPasswordView() throws Exception {
        MockHttpServletRequestBuilder request = get("/forgotten-password");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("forgot-password"))
                .andExpect(model().attributeExists("changerPassRequest"));
    }

    @Test
    void postRequestToRegisterEndpoint_happyPath() throws Exception {
        MockHttpServletRequestBuilder request = post("/register")
                .formField("companyName", "companyName")
                .formField("email", "test@email.com")
                .formField("ownerName", "ownerName")
                .formField("companyAddress", "companyAddress")
                .formField("phoneNumber", "phoneNumber")
                .formField("password", "Test123")
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        verify(userService, times(1)).register(registerRequestCaptor.capture());
        RegisterRequest registerRequest = registerRequestCaptor.getValue();

        assertEquals("companyName", registerRequest.getCompanyName());
        assertEquals("test@email.com", registerRequest.getEmail());
        assertEquals("ownerName", registerRequest.getOwnerName());
        assertEquals("companyAddress", registerRequest.getCompanyAddress());
        assertEquals("phoneNumber", registerRequest.getPhoneNumber());
        assertEquals("Test123", registerRequest.getPassword());
    }

    @Test
    void postRequestToRegisterEndpointWhenUsernameAlreadyExist_thenRedirectToRegisterWithFlashParameter() throws Exception {
        when(userService.register(any())).thenThrow(new EmailAlreadyExistException("Email already exist!"));
        MockHttpServletRequestBuilder request = post("/register")
                .formField("companyName", "companyName")
                .formField("email", "test@email.com")
                .formField("ownerName", "ownerName")
                .formField("companyAddress", "companyAddress")
                .formField("phoneNumber", "phoneNumber")
                .formField("password", "Test123")
                .with(csrf());


        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/register"))
                .andExpect(flash().attributeExists("EmailAlreadyExistMessage"));
        verify(userService, times(1)).register(any());
    }

    @Test
    void putRequestToChangePasswordEndpoint_happyPath() throws Exception {
        MockHttpServletRequestBuilder request = put("/forgotten-password")
                .formField("email", "test@email.com")
                .formField("password", "Test123")
                .formField("confirmPassword", "Test123")
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        verify(userService).changePassword(any());
    }

    @Test
    void putRequestToChangePasswordEndpointWhenPasswordsDoNotMatch_thenReturnForgotPasswordPageWithFlashAttribute() throws Exception {
        OngoingStubbing<User> userOngoingStubbing =
                when(userService.changePassword(any())).thenThrow(new NotMatchingPasswords("Passwords do not match!"));
        MockHttpServletRequestBuilder request = put("/forgotten-password")
                .formField("email", "test@email.com")
                .formField("password", "Test123")
                .formField("confirmPassword", "notMatchingPassword123")
                .with(csrf());


        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/forgotten-password"))
                .andExpect(flash().attributeExists("notMatchingPasswords"));
        verify(userService, times(1)).changePassword(any());
    }

    @Test
    void getAuthenticatedRequestToHome_returnsHomeView() throws Exception {
        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .id(userId)
                .companyName("companyName")
                .ownerName("ownerName")
                .companyAddress("companyAddress")
                .phoneNumber("phoneNumber")
                .email("test@email.com")
                .password("Test123")
                .role(Role.USER)
                .build();

        when(userService.getById(any())).thenReturn(user);

        AuthenticationDetails principal = new AuthenticationDetails(userId, "test@email.com", "Test123", user.getRole(), true);
        MockHttpServletRequestBuilder request = get("/home")
                .with(user(principal));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("user"));
        verify(userService, times(1)).getById(userId);
    }

    @Test
    void getUnauthenticatedRequestToHome_redirectToLogin() throws Exception {

        MockHttpServletRequestBuilder request = get("/home");

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection());
        verify(userService, never()).getById(any());
    }

    @Test
    void getErrorPageWithoutRequestParamContinue_RedirectToHome() throws Exception {
        MockHttpServletRequestBuilder request = get("/error");

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void getErrorPageWithRequestParamContinue_RedirectToLogin() throws Exception {
        MockHttpServletRequestBuilder request = get("/error")
                .param("continueParam", "continue");

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

}
