package app.eurocars.vehicle.type.service;

import app.eurocars.exception.DomainException;
import app.eurocars.vehicle.type.model.VehicleType;
import app.eurocars.vehicle.type.repository.TypeRepository;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
public class VehicleTypeService {

    private final TypeRepository typeRepository;

    public VehicleTypeService(TypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }

}
