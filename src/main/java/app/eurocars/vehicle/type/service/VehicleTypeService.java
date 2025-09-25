package app.eurocars.vehicle.type.service;

import app.eurocars.exception.DomainException;
import app.eurocars.vehicle.type.model.VehicleType;
import app.eurocars.vehicle.type.repository.TypeRepository;
import app.eurocars.web.dto.VehicleTypeRequest;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
public class VehicleTypeService {

    private final TypeRepository typeRepository;
    @Setter
    private VehicleTypeRequest selectedVehicleType;

    public VehicleTypeService(TypeRepository typeRepository) {
        this.typeRepository = typeRepository;
        this.selectedVehicleType = VehicleTypeRequest.builder().category("cars-btn").build();
    }

    public VehicleType getSelectedVehicleType() {
        String category = this.selectedVehicleType.getCategory();

        String searchedTypeName = switch (category) {
            case "cars-btn" -> "Car";
            case "vans-btn" -> "Van";
            case "trucks-btn" -> "Buses/Trucks";
            case "motorcycles-btn" -> "Motorcycle/ATV/UTV";
            case "vin-btn" -> "VIN";
            default -> "";
        };

        return typeRepository.findByName(searchedTypeName).orElseThrow(() -> new DomainException("No such vehicle type found"));
    }

}
