package app.eurocars.web;

import app.eurocars.brand.model.Brand;
import app.eurocars.brand.service.BrandService;
import app.eurocars.car.models.model.Model;
import app.eurocars.car.models.service.ModelService;
import app.eurocars.cart.client.dto.CartItemRequest;
import app.eurocars.cart.service.CartService;
import app.eurocars.engine.model.Engine;
import app.eurocars.engine.service.EngineService;
import app.eurocars.security.AuthenticationDetails;
import app.eurocars.user.model.User;
import app.eurocars.user.service.UserService;
import app.eurocars.vehicle.type.model.VehicleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;
import java.util.Random;

import static app.eurocars.util.TestDataProvider.getRandomUser;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CatalogueController.class)
public class CatalogueControllerApiTest {
    @MockitoBean
    private BrandService brandService;
    @MockitoBean
    private ModelService modelService;
    @MockitoBean
    private EngineService engineService;
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
    }

    @Test
    void getBrands_returnBrandsViewWithBrandsSeparatedByType() throws Exception {
        Brand car = Brand.builder().id(1L).build();
        Brand truck = Brand.builder().id(2L).build();
        Brand van = Brand.builder().id(3L).build();
        Brand motorcycles = Brand.builder().id(4L).build();

        when(brandService.findALlByType(VehicleType.builder().id(1L).build())).thenReturn(List.of(car));
        when(brandService.findALlByType(VehicleType.builder().id(2L).build())).thenReturn(List.of(van));
        when(brandService.findALlByType(VehicleType.builder().id(3L).build())).thenReturn(List.of(truck));
        when(brandService.findALlByType(VehicleType.builder().id(4L).build())).thenReturn(List.of(motorcycles));

        MockHttpServletRequestBuilder request = get("/brands")
                .with(user(principal));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("brands"))
                .andExpect(model().attributeExists("cars", "vans", "trucks", "motorcycles"));

    }

    @Test
    void getModels_returnModelsViewWithModelsAttribute() throws Exception {
        long brandId = new Random().nextLong();
        Brand brand = Brand.builder().id(brandId).build();
        Model model = Model.builder().id(1L).brand(brand).build();

        when(modelService.findAllByBrand(any())).thenReturn(List.of(model));

        MockHttpServletRequestBuilder request = get("/models")
                .with(user(principal))
                .param("brandId", String.valueOf(brandId));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("models"))
                .andExpect(model().attributeExists("models"));
    }

    @Test
    void getEngines_returnEnginesViewWithEnginesAndBrandIdAttribute() throws Exception {
        long brandId = new Random().nextLong();
        long modelId = new Random().nextLong();
        Brand brand = Brand.builder().id(brandId).build();
        Model model = Model.builder().id(modelId).brand(brand).build();
        Engine engine = Engine.builder().id(1L).model(model).build();

        when(engineService.findAllByModel(any())).thenReturn(List.of(engine));
        when(modelService.getById(modelId)).thenReturn(model);

        MockHttpServletRequestBuilder request = get("/engines")
                .with(user(principal))
                .param("modelId", String.valueOf(modelId));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("engines"))
                .andExpect(model().attributeExists("engines", "brandId"));

        verify(modelService).getById(any());
    }
}
