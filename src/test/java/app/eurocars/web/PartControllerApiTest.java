package app.eurocars.web;

import app.eurocars.brand.model.Brand;
import app.eurocars.brand.service.BrandService;
import app.eurocars.car.models.model.Model;
import app.eurocars.car.models.service.ModelService;
import app.eurocars.cart.service.CartService;
import app.eurocars.category.model.Category;
import app.eurocars.category.service.CategoryService;
import app.eurocars.engine.model.Engine;
import app.eurocars.engine.service.EngineService;
import app.eurocars.manufacturer.model.Manufacturer;
import app.eurocars.manufacturer.service.ManufacturerService;
import app.eurocars.part.model.Part;
import app.eurocars.part.service.PartService;
import app.eurocars.security.AuthenticationDetails;
import app.eurocars.user.model.User;
import app.eurocars.user.service.UserService;
import app.eurocars.vehicle.type.model.VehicleType;
import app.eurocars.web.dto.AddPartRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static app.eurocars.util.TestDataProvider.getRandomAdmin;
import static app.eurocars.util.TestDataProvider.getRandomUser;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PartController.class)
public class PartControllerApiTest {
    @MockitoBean
    private ManufacturerService manufacturerService;
    @MockitoBean
    private CategoryService categoryService;
    @MockitoBean
    private PartService partService;
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
        when(cartService.getCartItemsByUserId(user.getId())).thenReturn(new ArrayList<>());
        when(cartService.getWholePrice(any())).thenReturn(BigDecimal.ZERO);
    }

    @Test
    void getPartPage_returnPartViewWithAttributes() throws Exception {
        UUID partId = UUID.randomUUID();
        Engine engine = Engine.builder().id(1L)
                .model(Model.builder().id(1L).brand(Brand.builder().id(1L).name("name").build()).build())
                .build();
        Manufacturer manufacturer = Manufacturer.builder().id(1L).build();
        Part part = Part.builder()
                .id(partId)
                .category(Category.builder().id(1L).build())
                .engines(Set.of(engine))
                .manufacturer(manufacturer)
                .price(BigDecimal.TEN)
                .build();

        Part crossReference = Part.builder()
                .id(UUID.randomUUID())
                .category(Category.builder().id(1L).build())
                .engines(Set.of(engine))
                .manufacturer(manufacturer)
                .price(BigDecimal.TEN)
                .build();

        when(partService.getPartById(any())).thenReturn(part);
        when(partService.findCrossReferences(part)).thenReturn(List.of(crossReference));

        MockHttpServletRequestBuilder request = get("/part")
                .param("partId", partId.toString())
                .with(user(principal));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("part"))
                .andExpect(model().attributeExists("part", "crossReferences", "cartItemRequest"));


        verify(partService).getPartById(any());
        verify(partService).findCrossReferences(part);
    }

    @Test
    void getToAddPage_whenUserUserIsAdmin_returnCreatePartViewWithAttributes() throws Exception {
        user = getRandomAdmin();
        principal = new AuthenticationDetails(user.getId(), user.getEmail(), user.getPassword(), user.getRole(), true);
        when(userService.getById(any())).thenReturn(user);

        Manufacturer manufacturer = Manufacturer.builder().id(1L).build();
        Category category = Category.builder().id(1L).build();
        Engine engine = Engine.builder().id(1L).model(Model.builder().id(1L).brand(Brand.builder().id(1L).build()).build()).build();

        when(manufacturerService.getAllManufacturers()).thenReturn(List.of(manufacturer));
        when(categoryService.getAllCategories()).thenReturn(List.of(category));
        when(engineService.getAllEngines()).thenReturn(List.of(engine));

        MockHttpServletRequestBuilder request = get("/parts-settings/new-part")
                .with(user(principal));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("add-part"))
                .andExpect(model().attributeExists("createPart", "allManufacturers", "allCategories", "allEngines"));
    }


    @Test
    void getToAddPage_whenUserUserIsNotAuthorized_interceptAndReturnStatusOkWithEmptyView() throws Exception {
        MockHttpServletRequestBuilder request = get("/parts-settings/new-part")
                .with(user(principal))
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().isOk());
    }

    @Test
    void createPart_whenUserIsAdmin_redirectToAddPartViewWith() throws Exception {
        user = getRandomAdmin();
        principal = new AuthenticationDetails(user.getId(), user.getEmail(), user.getPassword(), user.getRole(), true);
        when(userService.getById(any())).thenReturn(user);

        AddPartRequest randomPart = createPartRequest();
        String existingPartId = "12345";

        MockHttpServletRequestBuilder request = post("/parts-settings/new-part") // Changed from put("/{partId}") to post("/new-part")
                .with(user(principal))
                .param("id", randomPart.getId().toString())
                .param("name", randomPart.getName())
                .param("description", randomPart.getDescription())
                .param("imgUrls", randomPart.getImgUrls())
                .param("additionalInformation", randomPart.getAdditionalInformation())
                .param("otherNumbers", randomPart.getOtherNumbers())
                .param("price", randomPart.getPrice().toString())
                .param("weight", String.valueOf(randomPart.getWeight()))
                .param("manufacturer", randomPart.getManufacturer().toString())
                .param("category", randomPart.getCategory().toString())
                .param("engine", randomPart.getEngine().toString())
                .with(csrf());


        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/parts-settings/add-part"))
                .andExpect(model().attributeExists("createPart"));

        verify(partService, times(1)).createPart(any(AddPartRequest.class));
    }

    @Test
    void getEditPartPage_whenUserUserIsAdmin_returnEditPartViewWithAttributes() throws Exception {
        user = getRandomAdmin();
        principal = new AuthenticationDetails(user.getId(), user.getEmail(), user.getPassword(), user.getRole(), true);
        when(userService.getById(any())).thenReturn(user);

        Manufacturer manufacturer = Manufacturer.builder().id(1L).build();
        Category category = Category.builder().id(1L).build();
        Engine engine = Engine.builder().id(1L).model(Model.builder().id(1L).brand(Brand.builder().id(1L).build()).build()).build();
        Part part = Part.builder()
                .id(UUID.randomUUID())
                .name("name")
                .description("description")
                .additionalInformation("additionalInfo")
                .weight(new Random().nextDouble())
                .price(BigDecimal.TEN)
                .category(category)
                .engines(Set.of(engine))
                .manufacturer(manufacturer)
                .build();

        when(partService.getPartById(any())).thenReturn(part);
        when(manufacturerService.getAllManufacturers()).thenReturn(List.of(manufacturer));
        when(categoryService.getAllCategories()).thenReturn(List.of(category));
        when(engineService.getAllEngines()).thenReturn(List.of(engine));

        MockHttpServletRequestBuilder request = get("/parts-settings/" + part.getId())
                .with(user(principal));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("edit-part"))
                .andExpect(model().attributeExists("part", "editPart", "allManufacturers", "allCategories", "allEngines"));
    }

    @Test
    void editPart_whenUserIsAdminAndRequestIsValid_updatesPartAndRedirects() throws Exception {
        user = getRandomAdmin();
        principal = new AuthenticationDetails(user.getId(), user.getEmail(), user.getPassword(), user.getRole(), true);
        when(userService.getById(any())).thenReturn(user);

        AddPartRequest randomPart = createPartRequest();
        String existingPartId = randomPart.getId().toString();

        MockHttpServletRequestBuilder request = put("/parts-settings/" + existingPartId)
                .with(user(principal))
                .param("id", randomPart.getId().toString())
                .param("name", randomPart.getName())
                .param("description", randomPart.getDescription())
                .param("imgUrls", randomPart.getImgUrls())
                .param("additionalInformation", randomPart.getAdditionalInformation())
                .param("otherNumbers", randomPart.getOtherNumbers())
                .param("price", randomPart.getPrice().toString())
                .param("weight", String.valueOf(randomPart.getWeight()))
                .param("manufacturer", randomPart.getManufacturer().toString())
                .param("category", randomPart.getCategory().toString())
                .param("engine", randomPart.getEngine().toString())
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/parts-settings/" + existingPartId))
                .andExpect(model().attributeExists("editPart"));

        verify(partService, times(1)).updatePart(any(AddPartRequest.class), eq(existingPartId));
    }

    @Test
    void deletePart_whenUserIsAdmin_deletesPartAndRedirects() throws Exception {
        user = getRandomAdmin();
        principal = new AuthenticationDetails(user.getId(), user.getEmail(), user.getPassword(), user.getRole(), true);
        when(userService.getById(any())).thenReturn(user);
        UUID partIdToDelete = UUID.randomUUID();

        MockHttpServletRequestBuilder request = delete("/parts-settings")
                .with(user(principal))
                .param("partId", partIdToDelete.toString())
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/parts-settings"));

        verify(partService, times(1)).deletePartById(eq(partIdToDelete));
    }

    public static AddPartRequest createPartRequest() {
        ThreadLocalRandom random = ThreadLocalRandom.current();

        UUID randomId = UUID.randomUUID();

        String randomName = "Part-" + random.nextInt(1000, 9999);
        String randomDescription = "High-quality replacement part for " + randomName;
        String randomImgUrls = "https://placehold.co/600x400/000000/FFFFFF/png?text=Image+of+" + randomName.replace(" ", "+");
        String randomInfo = "Material: Steel alloy; Warranty: 12 months";
        String randomOtherNumbers = "OEM-" + random.nextInt(10000, 99999) + ", PN-" + random.nextInt(1000, 9999);

        BigDecimal randomPrice = BigDecimal.valueOf(random.nextDouble(10.00, 500.00)).setScale(2, BigDecimal.ROUND_HALF_UP);

        double randomWeight = random.nextDouble(0.5, 10.0);

        long randomManufacturerId = random.nextLong(1, 6);
        long randomCategoryId = random.nextLong(1, 6);
        long randomEngineId = random.nextLong(1, 6);

        return AddPartRequest.builder()
                .id(randomId)
                .name(randomName)
                .description(randomDescription)
                .imgUrls(randomImgUrls)
                .additionalInformation(randomInfo)
                .otherNumbers(randomOtherNumbers)
                .price(randomPrice)
                .weight(randomWeight)
                .manufacturer(randomManufacturerId)
                .category(randomCategoryId)
                .engine(randomEngineId)
                .build();
    }
}
