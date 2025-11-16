package app.eurocars.part;

import app.eurocars.category.service.CategoryService;
import app.eurocars.engine.service.EngineService;
import app.eurocars.manufacturer.service.ManufacturerService;
import app.eurocars.part.repository.PartRepository;
import app.eurocars.part.service.PartService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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


}
