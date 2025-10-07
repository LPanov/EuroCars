package app.eurocars.part.repository;

import app.eurocars.category.model.Category;
import app.eurocars.part.model.Part;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PartRepository extends JpaRepository<Part, UUID> {
    @Query("select p from Part p where lower(p.name) like lower(concat('%', :input, '%'))")
    List<Part> findPartsByInput(String input);
    
    Optional<Part> findPartById(UUID id);

    List<Part> findPartsByCategory(Category category);

    @Modifying
    @Transactional
    @Query("update Part p set p.price = p.price * :rate, p.updatedOn = now()")
    void updateAllPrices(double rate);
}
