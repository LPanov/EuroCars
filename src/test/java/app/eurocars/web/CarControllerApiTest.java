package app.eurocars.web;

import app.eurocars.cart.client.dto.CartItem;
import app.eurocars.cart.service.CartService;
import app.eurocars.category.model.Category;
import app.eurocars.category.service.CategoryService;
import app.eurocars.engine.model.Engine;
import app.eurocars.engine.service.EngineService;
import app.eurocars.exception.EngineNotFound;
import app.eurocars.security.AuthenticationDetails;
import app.eurocars.user.model.Role;
import app.eurocars.user.model.User;
import app.eurocars.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static app.eurocars.util.TestDataProvider.getRandomUser;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CarController.class)
public class CarControllerApiTest {
    @MockitoBean
    private EngineService engineService;
    @MockitoBean
    private CategoryService categoryService;
    @MockitoBean
    private UserService userService;
    @MockitoBean
    private CartService cartService;
    @MockitoBean
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void whenGetVehicleMenu_returnCarViewWithSelectedEngineAndAllCategories() throws Exception {
        User user = getRandomUser();

        Long randomId = new Random().nextLong();

        Engine engine = Engine.builder().id(randomId).build();
        Category mainCategory = Category.builder().id(1L).build();
        Category subCategory = Category.builder().id(2L).category(mainCategory).build();

        when(userService.getById(any())).thenReturn(user);
        when(cartService.getCartItemsByUserId(user.getId())).thenReturn(new ArrayList<>());
        when(cartService.getWholePrice(new ArrayList<>())).thenReturn(BigDecimal.ZERO);
        when(engineService.getById(anyLong())).thenReturn(engine);
        when(categoryService.getAllMainCategories()).thenReturn(List.of(mainCategory));
        when(categoryService.getAllSubCategories()).thenReturn(List.of(subCategory));

        AuthenticationDetails principal = new AuthenticationDetails(user.getId(), user.getEmail(), user.getPassword(), user.getRole(), true);
        MockHttpServletRequestBuilder request = get("/cars/"+randomId+"/categories")
                .with(user(principal));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("car"))
                .andExpect(model().attributeExists("user", "items", "mainCategories", "subCategories", "engine"));

        verify(engineService).getById(anyLong());
    }

    @Test
    void getVehicleMenuWithNonExistingEngine_throwEngineNotFoundAndRedirectToErrorPage() throws Exception {
        User user = getRandomUser();

        when(userService.getById(any())).thenReturn(user);
        when(cartService.getCartItemsByUserId(user.getId())).thenReturn(new ArrayList<>());
        when(cartService.getWholePrice(new ArrayList<>())).thenReturn(BigDecimal.ZERO);
        when(engineService.getById(anyLong())).thenThrow(EngineNotFound.class);

        AuthenticationDetails principal = new AuthenticationDetails(user.getId(), user.getEmail(), user.getPassword(), user.getRole(), true);
        MockHttpServletRequestBuilder request = get("/cars/"+1L+"/categories")
                .with(user(principal));

        mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(view().name("not-found"));

        verify(engineService).getById(anyLong());
    }

    @Test
    void whenGetVehicleMenuWithEngineIdEqualToZero_returnCarViewWithoutSelectedEngine() throws Exception {
        User user = getRandomUser();

        Category mainCategory = Category.builder().id(1L).build();
        Category subCategory = Category.builder().id(2L).category(mainCategory).build();

        when(userService.getById(any())).thenReturn(user);
        when(cartService.getCartItemsByUserId(user.getId())).thenReturn(new ArrayList<>());
        when(cartService.getWholePrice(new ArrayList<>())).thenReturn(BigDecimal.ZERO);
        when(categoryService.getAllMainCategories()).thenReturn(List.of(mainCategory));
        when(categoryService.getAllSubCategories()).thenReturn(List.of(subCategory));

        AuthenticationDetails principal = new AuthenticationDetails(user.getId(), user.getEmail(), user.getPassword(), user.getRole(), true);
        MockHttpServletRequestBuilder request = get("/cars/0/categories")
                .with(user(principal));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("car"))
                .andExpect(model().attributeExists("user", "items", "wholePrice", "mainCategories", "subCategories", "engine"));


        verify(engineService, never()).getById(anyLong());
    }
}
