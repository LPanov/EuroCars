package app.eurocars.web;

import app.eurocars.cart.service.CartService;
import app.eurocars.category.model.Category;
import app.eurocars.category.service.CategoryService;
import app.eurocars.engine.model.Engine;
import app.eurocars.manufacturer.model.Manufacturer;
import app.eurocars.manufacturer.service.ManufacturerService;
import app.eurocars.part.model.Part;
import app.eurocars.part.service.PartService;
import app.eurocars.security.AuthenticationDetails;
import app.eurocars.user.model.User;
import app.eurocars.user.service.UserService;
import app.eurocars.web.dto.ListedPart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.math.BigDecimal;
import java.util.*;

import static app.eurocars.util.TestDataProvider.getRandomAdmin;
import static app.eurocars.util.TestDataProvider.getRandomUser;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PartsController.class)
public class PartsControllerApiTest {
    @MockitoBean
    private ManufacturerService manufacturerService;
    @MockitoBean
    private PartService partService;
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

    @BeforeEach
    void setUp() {
        user = getRandomUser();
        principal = new AuthenticationDetails(user.getId(), user.getEmail(), user.getPassword(), user.getRole(), true);
        when(userService.getById(any())).thenReturn(user);
        when(cartService.getCartItemsByUserId(user.getId())).thenReturn(new ArrayList<>());
        when(cartService.getWholePrice(any())).thenReturn(BigDecimal.ZERO);
    }

    @Test
    void getPartsPage_withCategoryIdAndVehicleId_loadsFilteredParts() throws Exception {
        String vehicleId = "1L";
        String categoryId = "1L";
        Manufacturer manufacturer = Manufacturer.builder().id(1L).imgUrl("imgUrl").build();
        Part part = Part.builder()
                .id(UUID.randomUUID())
                .name("name")
                .description("description")
                .additionalInformation("additionalInfo")
                .weight(new Random().nextDouble())
                .price(BigDecimal.TEN)
                .category(Category.builder().id(1L).build())
                .engines(Set.of(Engine.builder().id(1L).build()))
                .manufacturer(manufacturer)
                .build();

        List<Part> mockParts = List.of(part);

        when(manufacturerService.getAllManufacturers()).thenReturn(List.of(manufacturer));
        when(partService.getAllFilteredParts(eq(vehicleId), eq(categoryId), any())).thenReturn(mockParts);

        MockHttpServletRequestBuilder request = get("/parts")
                .param("vehicleId", vehicleId)
                .param("categoryId", categoryId)
                .with(user(principal));
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("list-parts"))
                .andExpect(model().attributeExists("listedParts", "listedParts"));

        verify(partService, times(1)).getAllFilteredParts(vehicleId, categoryId, null);
    }

    @Test
    void getPartsPage_withInput_loadsFilteredParts() throws Exception {
        String input = "input";
        Part part = generatePart(input);

        List<Part> mockParts = List.of(part);

        when(manufacturerService.getAllManufacturers()).thenReturn(List.of(part.getManufacturer()));
        when(partService.getAllFilteredParts(null, null, input)).thenReturn(mockParts);

        MockHttpServletRequestBuilder request = get("/parts")
                .param("input", input)
                .with(user(principal));
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("list-parts"))
                .andExpect(model().attributeExists("listedParts", "listedParts"));

        verify(partService, times(1)).getAllFilteredParts(null, null, input);
    }

    @Test
    void getPartsSettingsPage_loadsAllPartsForAdminView() throws Exception {
        user = getRandomAdmin();
        principal = new AuthenticationDetails(user.getId(), user.getEmail(), user.getPassword(), user.getRole(), true);
        when(userService.getById(any())).thenReturn(user);

        List<Part> mockParts = List.of(generatePart("part1"), generatePart("part2"));

        when(partService.getAllParts()).thenReturn(mockParts);

        MockHttpServletRequestBuilder request = get("/parts-settings")
                .with(user(principal));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("parts"))
                .andExpect(model().attributeExists("parts"))
                .andExpect(model().attribute("parts", mockParts));

        verify(partService, times(1)).getAllParts();
    }


    private static Part generatePart(String name) {
        return Part.builder()
                .id(UUID.randomUUID())
                .name(name)
                .description("description")
                .additionalInformation("additionalInfo")
                .imgUrls(Set.of("imgUrl1", "imgUrl2"))
                .weight(new Random().nextDouble())
                .price(BigDecimal.TEN)
                .category(Category.builder().id(1L).build())
                .engines(Set.of(Engine.builder().id(1L).build()))
                .manufacturer(Manufacturer.builder().id(1L).imgUrl("imgUrl").build())
                .build();
    }

}
