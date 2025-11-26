package app.eurocars.web;

import app.eurocars.cart.client.dto.CartItemRequest;
import app.eurocars.cart.service.CartService;
import app.eurocars.category.model.Category;
import app.eurocars.category.service.CategoryService;
import app.eurocars.engine.model.Engine;
import app.eurocars.engine.service.EngineService;
import app.eurocars.security.AuthenticationDetails;
import app.eurocars.user.model.Role;
import app.eurocars.user.model.User;
import app.eurocars.user.service.UserService;
import app.eurocars.util.TestDataProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static app.eurocars.util.TestDataProvider.getRandomUser;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CartController.class)
public class CartControllerApiTest {
    @MockitoBean
    private UserService userService;
    @MockitoBean
    private CartService cartService;
    @MockitoBean
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @Autowired
    private MockMvc mockMvc;

    private User user;
    private AuthenticationDetails principal;

    @Captor
    private ArgumentCaptor<CartItemRequest> cartItemRequestArgumentCaptor;

    @BeforeEach
    void setUp() {
        user = getRandomUser();
        principal = new AuthenticationDetails(user.getId(), user.getEmail(), user.getPassword(), user.getRole(), true);
        when(userService.getById(any())).thenReturn(user);
    }

    @Test
    void whenGetCartPage_returnViewWithAttributesCartItemsAndUser() throws Exception {
        when(userService.getById(any())).thenReturn(user);
        when(cartService.getCartItemsByUserId(user.getId())).thenReturn(new ArrayList<>());
        when(cartService.getWholePrice(new ArrayList<>())).thenReturn(BigDecimal.ZERO);

        MockHttpServletRequestBuilder request = get("/cart")
                .with(user(principal));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("cart"))
                .andExpect(model().attributeExists("user", "items", "wholePrice"));
    }

    @Test
    void addToCart_happyPath() throws Exception {
        UUID partId = UUID.randomUUID();
        int quantity = new Random().nextInt(100);
        UUID userId = user.getId();

        MockHttpServletRequestBuilder request = post("/cart/item")
                .with(user(principal))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("partId", partId.toString())
                .param("quantity", String.valueOf(quantity))
                .with(csrf());


        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection());

        verify(cartService).addToCart(cartItemRequestArgumentCaptor.capture());
        CartItemRequest addedPart = cartItemRequestArgumentCaptor.getValue();

        assertThat(addedPart.getPartId()).isEqualTo(partId);
        assertThat(addedPart.getQuantity()).isEqualTo(quantity);
        assertThat(addedPart.getUserId()).isEqualTo(userId);
    }

    @Test
    void addToCartWithInvalidCartItemRequest_thenRedirectToHome() throws Exception {
        MockHttpServletRequestBuilder request = post("/cart/item")
                .with(user(principal))
                .with(csrf());


        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));

        verify(cartService, never()).addToCart(cartItemRequestArgumentCaptor.capture());
    }

    @Test
    void deleteCartItem_SuccessfulDeletion_RedirectsToCart() throws Exception {
        UUID testItemId = UUID.randomUUID();

        MockHttpServletRequestBuilder request = delete("/cart/item")
                .with(user(principal))
                .param("itemId", testItemId.toString())
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"));

        verify(cartService).removeFromCart(testItemId);
    }


}
