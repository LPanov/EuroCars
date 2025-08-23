package app.eurocars.part.repository;

import app.eurocars.part.model.Part;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PartRepository extends JpaRepository<Part, UUID> {
}
