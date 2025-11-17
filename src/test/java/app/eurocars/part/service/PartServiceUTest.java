package app.eurocars.part.service;

import app.eurocars.category.model.Category;
import app.eurocars.category.service.CategoryService;
import app.eurocars.engine.model.Engine;
import app.eurocars.engine.service.EngineService;
import app.eurocars.exception.DomainException;
import app.eurocars.exception.EngineNotFound;
import app.eurocars.manufacturer.model.Manufacturer;
import app.eurocars.manufacturer.service.ManufacturerService;
import app.eurocars.part.model.Part;
import app.eurocars.part.repository.PartRepository;
import app.eurocars.web.dto.AddPartRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PartServiceUTest {
    @Mock
    private PartRepository partRepository;
    @Mock
    private EngineService engineService;
    @Mock
    private CategoryService categoryService;
    @Mock
    private ManufacturerService manufacturerService;

    @InjectMocks
    private PartService partService;

    @Captor
    private ArgumentCaptor<Part> partArgumentCaptor;

    @Test
    void givenCategoryIdAndEngineId_whenGetAllFilteredParts_thenListWithFilteredPartsIsReturned() {
        Long categoryId = 1L;
        Long engineId = 1L;

        Category category = Category.builder().id(categoryId).build();
        Engine engine = Engine.builder().id(engineId).build();
        Part targetedPart = Part.builder()
                .category(category)
                .engines(Set.of(engine))
                .build();
        Part part2 = Part.builder()
                .category(Category.builder().id(2L).build())
                .engines(Set.of(Engine.builder().id(2L).build()))
                .build();

        when(categoryService.getById(categoryId)).thenReturn(category);
        when(engineService.getById(engineId)).thenReturn(engine);
        when(partRepository.findAll()).thenReturn(List.of(targetedPart, part2));

        List<Part> filteredParts = partService.getAllFilteredParts(String.valueOf(engineId), String.valueOf(categoryId), null);

        assertEquals(1, filteredParts.size());
        verify(partRepository).findAll();
        verify(categoryService).getById(categoryId);
        verify(engineService).getById(engineId);
        verify(partRepository, never()).findPartsByInput(any());
    }

    @Test
    void givenCategoryId_whenGetAllFilteredParts_thenListWithFilteredPartsIsReturned() {
        Long categoryId = 1L;

        Category category = Category.builder().id(categoryId).build();
        Part targetedPart = Part.builder()
                .category(category)
                .build();
        Part part2 = Part.builder()
                .category(Category.builder().id(2L).build())
                .build();

        when(categoryService.getById(categoryId)).thenReturn(category);
        when(partRepository.findAll()).thenReturn(List.of(targetedPart, part2));

        List<Part> filteredParts = partService.getAllFilteredParts(null, String.valueOf(categoryId), null);

        assertEquals(1, filteredParts.size());
        verify(partRepository).findAll();
        verify(categoryService).getById(categoryId);
        verify(engineService, never()).getById(anyLong());
        verify(partRepository, never()).findPartsByInput(any());
    }

    @Test
    void givenInput_whenGetAllFilteredParts_thenListWithFilteredPartsByTheInputContainedInPartNameIsReturned() {
        String input = "str";

        Part targetedPart = Part.builder()
                .name("name-str-name")
                .build();
        Part part2 = Part.builder()
                .name("NameWithMissingInput")
                .build();

        when(partRepository.findAll()).thenReturn(List.of(targetedPart, part2));
        when(partRepository.findPartsByInput(input)).thenReturn(List.of(targetedPart));

        List<Part> filteredParts = partService.getAllFilteredParts(null, null, input);

        assertEquals(1, filteredParts.size());
        verify(partRepository).findAll();
        verify(categoryService, never()).getById(anyLong());
        verify(engineService, never()).getById(anyLong());
        verify(partRepository).findPartsByInput(input);
    }

    @Test
    void whenFindAllByModelWithNullParameters_thenReturnAllParts() {
        when(partRepository.findAll()).thenReturn(List.of(new Part(), new Part()));

        List<Part> filteredParts = partService.getAllFilteredParts(null, null, null);

        assertEquals(2, filteredParts.size());
        verify(partRepository).findAll();
        verify(categoryService, never()).getById(anyLong());
        verify(engineService, never()).getById(anyLong());
        verify(partRepository, never()).findPartsByInput(anyString());
    }

    @Test
    void givenExistingPartId_whenGetPartById_thenReturnCorrespondingPart(){
        UUID partId = UUID.randomUUID();
        Part targetedPart = Part.builder().id(partId).build();

        when(partRepository.findPartById(partId)).thenReturn(Optional.of(targetedPart));

        Part foundPart = partService.getPartById(String.valueOf(partId));

        assertEquals(targetedPart.getId(), foundPart.getId());
    }

    @Test
    void givenNonExistingPartId_whenGetById_thenThrowDomainException() {
        UUID partId = UUID.randomUUID();

        assertThrows(DomainException.class, () -> partService.getPartById(partId.toString()));
    }

    @Test
    void givenValidPart_whenFindCrossReferences_thenReturnListOfPartsWithSameCategoryAndEngine() {
        Part part = Part.builder()
                .id(UUID.randomUUID())
                .category(Category.builder().id(1L).build())
                .engines(Set.of(Engine.builder().id(1L).build()))
                .build();
        Part targetedPart = Part.builder()
                .id(UUID.randomUUID())
                .category(Category.builder().id(1L).build())
                .engines(Set.of(Engine.builder().id(1L).build()))
                .build();
        Part part3 = Part.builder()
                .id(UUID.randomUUID())
                .category(Category.builder().id(1L).build())
                .engines(Set.of(Engine.builder().id(2L).build()))
                .build();

        when(partRepository.findPartsByCategory(part.getCategory())).thenReturn(List.of(part, targetedPart, part3));

        List<Part> crossReferences = partService.findCrossReferences(part);

        assertEquals(1, crossReferences.size());
        verify(partRepository).findPartsByCategory(part.getCategory());
    }

    @Test
    void givenUniquePart_whenFindCrossReferences_thenReturnEmptyList(){
        Part part = Part.builder()
                .id(UUID.randomUUID())
                .category(Category.builder().id(1L).build())
                .engines(Set.of(Engine.builder().id(1L).build()))
                .build();
        Part targetedPart = Part.builder()
                .id(UUID.randomUUID())
                .category(Category.builder().id(2L).build()) //different category
                .engines(Set.of(Engine.builder().id(1L).build()))//same engine
                .build();
        Part part3 = Part.builder()
                .id(UUID.randomUUID())
                .category(Category.builder().id(1L).build()) //same category
                .engines(Set.of(Engine.builder().id(2L).build())) //different engine
                .build();

        when(partRepository.findPartsByCategory(part.getCategory())).thenReturn(List.of(part, part3));

        List<Part> crossReferences = partService.findCrossReferences(part);

        assertTrue(crossReferences.isEmpty());
        verify(partRepository).findPartsByCategory(part.getCategory());
    }

    @Test
    void givenNullPart_whenFindCrossReferences_thenReturnEmptyList() {
        List<Part> crossReferences = partService.findCrossReferences(null);

        assertTrue(crossReferences.isEmpty());
        verify(partRepository, never()).findPartsByCategory(any());
    }

    @Test
    void givenValidAddPartRequest_whenCreatePart() {
        AddPartRequest addPartRequest = AddPartRequest.builder()
                .name("name")
                .description("description")
                .weight(5.9)
                .price(BigDecimal.valueOf(4))
                .additionalInformation("Additional Information")
                .manufacturer(1L)
                .category(1L)
                .engine(1L)
                .imgUrls("ImgUrl1\nImgUrl2")
                .otherNumbers("OtherNumber1\nOtherNumber2")
                .build();

        Category category = Category.builder().id(addPartRequest.getCategory()).build();
        Engine engine = Engine.builder().id(addPartRequest.getEngine()).build();
        Manufacturer manufacturer = Manufacturer.builder().id(addPartRequest.getManufacturer()).build();

        when(categoryService.getById(anyLong())).thenReturn(category);
        when(engineService.getById(anyLong())).thenReturn(engine);
        when(manufacturerService.findById(anyLong())).thenReturn(manufacturer);

        partService.createPart(addPartRequest);

        verify(partRepository, times(1)).save(partArgumentCaptor.capture());
        Part savedPart = partArgumentCaptor.getValue();

        assertNotNull(savedPart, "The saved Part object should not be null.");

        assertEquals(addPartRequest.getName(), savedPart.getName());
        assertEquals(addPartRequest.getDescription(), savedPart.getDescription());
        assertEquals(addPartRequest.getWeight(), savedPart.getWeight());
        assertEquals(addPartRequest.getPrice(), savedPart.getPrice());

        assertEquals(category, savedPart.getCategory());
        assertEquals(manufacturer, savedPart.getManufacturer());
        assertTrue(savedPart.getEngines().contains(engine));
        assertEquals(1, savedPart.getEngines().size());

        assertEquals(Arrays.stream(addPartRequest.getImgUrls().split("\n")).collect(Collectors.toSet()).size(), savedPart.getImgUrls().size());
        assertEquals(Arrays.stream(addPartRequest.getOtherNumbers().split("\n")).collect(Collectors.toSet()).size(), savedPart.getOtherNumbers().size());

        assertNotNull(savedPart.getCreatedDate());
        assertNotNull(savedPart.getUpdatedOn());

        verify(categoryService, times(1)).getById(addPartRequest.getCategory());
        verify(engineService, times(1)).getById(addPartRequest.getEngine());
        verify(manufacturerService, times(1)).findById(addPartRequest.getManufacturer());
    }

    @Test
    void givenValidAddPartRequest_whenUpdatePart() {
        AddPartRequest editPartRequest = AddPartRequest.builder()
                .name("name")
                .description("description")
                .weight(5.9)
                .price(BigDecimal.valueOf(4))
                .additionalInformation("Additional Information")
                .manufacturer(1L)
                .category(1L)
                .engine(1L)
                .imgUrls("ImgUrl1\nImgUrl2")
                .otherNumbers("OtherNumber1\nOtherNumber2")
                .build();

        String partId = UUID.randomUUID().toString();
        Part part = Part.builder()
                .id(UUID.fromString(partId))
                .engines(new HashSet<>())
                .imgUrls(new HashSet<>())
                .otherNumbers(new HashSet<>())
                .build();

        Category category = Category.builder().id(editPartRequest.getCategory()).build();
        Engine engine = Engine.builder().id(editPartRequest.getEngine()).build();
        Manufacturer manufacturer = Manufacturer.builder().id(editPartRequest.getManufacturer()).build();

        when(partRepository.findPartById(UUID.fromString(partId))).thenReturn(Optional.of(part));
        when(categoryService.getById(anyLong())).thenReturn(category);
        when(engineService.getById(anyLong())).thenReturn(engine);
        when(manufacturerService.findById(anyLong())).thenReturn(manufacturer);

        partService.updatePart(editPartRequest, partId);

        verify(partRepository).findPartById(UUID.fromString(partId));
        verify(partRepository, times(1)).save(partArgumentCaptor.capture());
        Part editedPart = partArgumentCaptor.getValue();

        assertNotNull(editedPart, "The edited Part object should not be null.");

        assertEquals(editPartRequest.getName(), editedPart.getName());
        assertEquals(editPartRequest.getDescription(), editedPart.getDescription());
        assertEquals(editPartRequest.getWeight(), editedPart.getWeight());
        assertEquals(editPartRequest.getPrice(), editedPart.getPrice());

        assertEquals(category, editedPart.getCategory());
        assertEquals(manufacturer, editedPart.getManufacturer());
        assertTrue(editedPart.getEngines().contains(engine));
        assertEquals(1, editedPart.getEngines().size());

        assertEquals(Arrays.stream(editPartRequest.getImgUrls().split("\n")).collect(Collectors.toSet()).size(), editedPart.getImgUrls().size());
        assertEquals(Arrays.stream(editPartRequest.getOtherNumbers().split("\n")).collect(Collectors.toSet()).size(), editedPart.getOtherNumbers().size());

        assertNotNull(editedPart.getUpdatedOn());

        verify(categoryService, times(1)).getById(editPartRequest.getCategory());
        verify(engineService, times(1)).getById(editPartRequest.getEngine());
        verify(manufacturerService, times(1)).findById(editPartRequest.getManufacturer());
    }

    @Test
    public void deletePartById_ShouldCallRepositoryDeleteWithCorrectId() {
        UUID partId = UUID.randomUUID();

        partService.deletePartById(partId);

        verify(partRepository, times(1)).deleteById(partId);
    }
}
