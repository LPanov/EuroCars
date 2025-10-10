package app.eurocars.engine.repository;

import app.eurocars.engine.model.Engine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EngineRepository extends JpaRepository<Engine, Long> {
    List<Engine> findAllByModelId(Long modelId);
}
