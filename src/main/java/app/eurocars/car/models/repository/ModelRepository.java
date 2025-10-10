package app.eurocars.car.models.repository;

import app.eurocars.car.models.model.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModelRepository extends JpaRepository<Model, Long> {
    List<Model> findAllByBrandId(Long brandId);
}
