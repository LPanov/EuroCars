package app.eurocars.brand.service;

import app.eurocars.brand.repository.BrandRepository;
import org.springframework.stereotype.Service;

@Service
public class BrandService {


    private final BrandRepository brandRepository;

    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }
}
