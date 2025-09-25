package app.eurocars.web;

import app.eurocars.vehicle.type.service.VehicleTypeService;
import app.eurocars.web.dto.VehicleTypeRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping
public class VehicleTypeController {


    private final VehicleTypeService vehicleTypeService;

    public VehicleTypeController(VehicleTypeService vehicleTypeService) {
        this.vehicleTypeService = vehicleTypeService;
    }

    @PostMapping("/catalogue")
    public ResponseEntity<Void> logCategoryClick(@RequestBody VehicleTypeRequest request) {
        vehicleTypeService.setSelectedVehicleType(request);

        System.out.println("Act 1 - " + request.getCategory());

        return ResponseEntity.ok().build();
    }
}
