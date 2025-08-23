package app.eurocars.vehicle.type.repository;

import app.eurocars.vehicle.type.model.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeRepository extends JpaRepository<VehicleType, Long> {
}
